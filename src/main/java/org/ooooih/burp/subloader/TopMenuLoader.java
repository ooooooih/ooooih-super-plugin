package org.ooooih.burp.subloader;

import burp.api.montoya.MontoyaApi;
import org.ooooih.burp.context.BurpContext;
import org.ooooih.burp.extension.ISubPlugins;
import org.ooooih.burp.extension.SuperPlugin;
import org.ooooih.burp.topmenu.TopMenuItemProvider;

import javax.swing.*;

public class TopMenuLoader implements ISubPlugins {

    @Override
    public void initial(MontoyaApi api) {
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
