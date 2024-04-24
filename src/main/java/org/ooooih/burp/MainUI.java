package org.ooooih.burp;

import javax.swing.*;
import java.awt.*;

/**
 * @author ooooooih
 */
public class MainUI {

    public static Component createMainUI() {
        JMenu menu = new JMenu("Menu");
        JMenuItem menuItem1 = new JMenuItem("Item 1");
        JMenuItem menuItem2 = new JMenuItem("Item 2");
        menu.add(menuItem1);
        menu.add(menuItem2);
        return menu;
    }
}
