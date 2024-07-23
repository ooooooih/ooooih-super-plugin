package org.ooooih.burp.subloader;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import burp.api.montoya.ui.editor.extension.HttpResponseEditorProvider;
import org.ooooih.burp.extension.ISubPlugins;
import org.ooooih.burp.u2chinese.U2cResponseEditor;

public class Unicode2ChineseLoader implements ISubPlugins, HttpResponseEditorProvider {

    private MontoyaApi api;
    @Override
    public void initial(MontoyaApi api) {
        this.api = api;
        api.userInterface().registerHttpResponseEditorProvider(this);
    }

    @Override
    public ExtensionProvidedHttpResponseEditor provideHttpResponseEditor(EditorCreationContext creationContext) {
        return new U2cResponseEditor(api, creationContext);
    }
}
