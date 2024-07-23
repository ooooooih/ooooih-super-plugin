package org.ooooih.burp.extension;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.ooooih.burp.subloader.TopMenuLoader;
import org.ooooih.burp.subloader.Unicode2ChineseLoader;
import org.ooooih.burp.tools.LoggerUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author oooooov
 */
public class SuperPlugin implements BurpExtension {

    @Getter
    private static MontoyaApi api;

    private static final List<Class<? extends ISubPlugins>> SUB_PLUGINS = Stream.of(
            TopMenuLoader.class,
            Unicode2ChineseLoader.class
    ).toList();

    public SuperPlugin() {
    }

    @Override
    public void initialize(MontoyaApi api) {
        SuperPlugin.api = api;
        api.extension().setName("OooihSuperPlugin");
        LoggerUtils.logInfo("OooihSuperPlugin init");
        loadSubPlugins();
    }

    private void loadSubPlugins() {
        LoggerUtils.logInfo("load sub plugins: " + SUB_PLUGINS.size());
        for (Class<?> aClass : SUB_PLUGINS) {
            ISubPlugins subPlugins = (ISubPlugins) ReflectUtil.newInstance(aClass);
            LoggerUtils.logInfo("load sub plugin: " + aClass.getName());
            subPlugins.initial(api);
        }
    }
}
