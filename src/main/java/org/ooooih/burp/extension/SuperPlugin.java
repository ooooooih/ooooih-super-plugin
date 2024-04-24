package org.ooooih.burp.extension;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import lombok.Getter;
import org.ooooih.burp.context.BurpContext;
import org.ooooih.burp.topmenu.TopMenuItemProvider;
import org.ooooih.burp.tools.LoggerUtils;

import javax.swing.*;

/**
 * @author oooooov
 */
public class SuperPlugin implements BurpExtension {

    @Getter
    private static MontoyaApi api;

    public SuperPlugin() {
    }

    @Override
    public void initialize(MontoyaApi api) {
        SuperPlugin.api = api;
        api.extension().setName("OoovSuperPlugin");
        LoggerUtils.logInfo("OoovSuperPlugin init");
        initTopMenu();
    }

    private void initTopMenu() {
        JMenu menu = new JMenu("TopEx");
        JMenuItem switchItem = new JMenuItem("Switch on");
        switchItem.addActionListener(e -> {
            int current = BurpContext.switchTopMenu();
            switchItem.setText(current == 0 ? "Switch on" : "Switch off");
        });
        menu.add(switchItem);
        api.userInterface().menuBar().registerMenu(menu);
        TopMenuItemProvider.registerSelf(api);
    }

}
