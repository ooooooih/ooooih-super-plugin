package org.ooooih.burp.u2chinese;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import org.ooooih.burp.tools.LoggerUtils;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class U2cResponseEditor implements ExtensionProvidedHttpResponseEditor {

    private static final Set<String> excludeSuffixes = new HashSet<>(Arrays.asList(".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".ico", ".woff", ".ttf", ".svg", ".map", ".less"));

    private HttpRequestResponse requestResponse;

    private final HttpResponseEditor responseEditor;

    public U2cResponseEditor(MontoyaApi api, EditorCreationContext creationContext) {
        if (creationContext.editorMode() == EditorMode.READ_ONLY)
        {
            responseEditor = api.userInterface().createHttpResponseEditor(EditorOptions.READ_ONLY);
        }
        else {
            responseEditor = api.userInterface().createHttpResponseEditor();
        }
    }

    @Override
    public HttpResponse getResponse() {
        return requestResponse.response();
    }

    @Override
    public void setRequestResponse(HttpRequestResponse requestResponse) {
        this.requestResponse = requestResponse;
        HttpResponse response = requestResponse.response();
        if (response.body() == null || response.body().length() == 0) {
            responseEditor.setResponse(response);
            return;
        }
        try {
            byte[] body = response.body().getBytes();
//        String bodyStr = new String(body, StandardCharsets.UTF_8);
//        LoggerUtils.logInfo("bodyStr: " + bodyStr);
            String encodeBody = UnicodeUtil.toString(new String(body, StandardCharsets.UTF_8));
//        LoggerUtils.logInfo("encodeBody: " + encodeBody);
            response = response.withBody(ByteArray.byteArray(encodeBody.getBytes(StandardCharsets.UTF_8)));
            responseEditor.setResponse(response);
        }catch (Exception e){
            LoggerUtils.logError("U2cResponseEditor setRequestResponse error: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse requestResponse) {
        HttpRequest request = requestResponse.request();
        String pureURL = StrUtil.emptyIfNull(request.url()).split("\\?")[0];
        return excludeSuffixes.stream().noneMatch(pureURL::endsWith);
    }

    @Override
    public String caption() {
        return "U2Chinese";
    }

    @Override
    public Component uiComponent() {
        return responseEditor.uiComponent();
    }

    @Override
    public Selection selectedData() {
        return responseEditor.selection().isPresent() ? responseEditor.selection().get() : null;
    }

    @Override
    public boolean isModified() {
        return responseEditor.isModified();
    }
}
