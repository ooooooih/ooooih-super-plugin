package org.ooooih.burp.topmenu;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import org.ooooih.burp.context.BurpContext;
import org.ooooih.burp.tools.LoggerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ooooooih
 */
public class TopMenuItemProvider implements ContextMenuItemsProvider {


    public static void registerSelf(MontoyaApi api) {
        //register the menu item provider
        api.userInterface().registerContextMenuItemsProvider(new TopMenuItemProvider());
        api.logging().logToOutput("register TopMenuItemProvider");
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {
        JMenuItem invisibleMenuItem = new JMenuItem("do nothing");
        topExtensionMenu(invisibleMenuItem, BurpContext.getTopMenuEnabled());
        return List.of(invisibleMenuItem);
    }


    private void topExtensionMenu(JMenuItem invisibleMenuItem, int hierarchy) {
        if (invisibleMenuItem == null) {
            return;
        }
        //add hierarchy listener to the bottom menu, so that we can modify the parent menu
        invisibleMenuItem.addHierarchyListener(new MenuHierarchyListener(hierarchy, Collections.emptyList(), 0));
    }

    private static class MenuHierarchyListener implements HierarchyListener {
        private final int hierarchy;
        private final AtomicInteger broadcast;
        private boolean performed = false;

        List<JPopupMenu> popupMenus = new LinkedList<>();

        public MenuHierarchyListener(int hierarchy, List<JPopupMenu> popupMenus, int broadcast) {
            this.hierarchy = hierarchy;
            this.broadcast = new AtomicInteger(broadcast);
            this.popupMenus.addAll(popupMenus);
        }

        @Override
        public void hierarchyChanged(HierarchyEvent e) {
            if (performed || (e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) == 0) {
                return;
            }
            performed = true;
            JMenuItem menuItem = (JMenuItem) e.getSource();
            JPopupMenu parentPopupMenu = getParentPopupMenu(menuItem);
            LoggerUtils.logInfo("level: %s, menuItem: %s, parentPopupMenu: %s", broadcast.get(), menuItem, parentPopupMenu);
            //if the parent menu is not a popup menu, return
            if (parentPopupMenu == null) {
                return;
            }
            popupMenus.add(0, parentPopupMenu);
            //if the parent menu item is not the extensions menu, return
            JMenuItem parentMenuItem = getParentMenuItem(parentPopupMenu);
            LoggerUtils.logInfo("parentMenuItem: %s, %s", parentPopupMenu.getInvoker(), parentMenuItem);
            if (broadcast.get() == 1 && !isExtensionsMenu(parentMenuItem)) {
                return;
            }
            if (broadcast.get() == 2) {
                //modify the popup menu
                LoggerUtils.logInfo("popupMenus: %s", popupMenus);
                modifyPopupMenu(hierarchy, popupMenus.get(0), popupMenus.get(1), popupMenus.get(popupMenus.size() - 1));
            }

            //max 3 hierarchy
            if (broadcast.get() < 2 && parentMenuItem != null) {
                //add hierarchy listener to the extension menu
                parentMenuItem.addHierarchyListener(new MenuHierarchyListener(hierarchy, popupMenus, broadcast.incrementAndGet()));
            }

        }

        private JPopupMenu getParentPopupMenu(Component component) {
            if (component == null) {
                return null;
            }
            return component.getParent() instanceof JPopupMenu ? (JPopupMenu) component.getParent() : null;
        }

        private JMenuItem getParentMenuItem(JPopupMenu popupMenu) {
            Component invoker = popupMenu.getInvoker();
            return invoker instanceof JMenuItem ? (JMenuItem) invoker : null;
        }

        /**
         * Check if the menu item is the extensions menu
         */
        private boolean isExtensionsMenu(JMenuItem menuItem) {
            return menuItem != null && "Extensions".equals(menuItem.getText());
        }

        private void modifyPopupMenu(int hierarchy, JPopupMenu topPopupMenu, JPopupMenu extensionPopupMenu, JPopupMenu self) {
            JMenuItem extensionParentItem = getParentMenuItem(extensionPopupMenu);
            JMenuItem selfItem = getParentMenuItem(self);
            LoggerUtils.logInfo("modifyPopupMenu: %s, topPopupMenu: %s, extensionParentItem: %s", hierarchy, topPopupMenu, extensionParentItem);
            if (extensionParentItem == null) {
                return;
            }
            //remove the self menu item from the extensions menu whatever the hierarchy is
            extensionParentItem.remove(selfItem);
            if (hierarchy == 0) {
                return;
            }
            //topPopupMenu -> extensionParentItem(Extensions) -> extensionMenu
            //get all the menus in the extensions menu
            List<JMenu> extensionMenus = new ArrayList<>();
            for (int i = 0; i < extensionPopupMenu.getComponentCount(); i++ ) {
                JMenu m = (JMenu) extensionPopupMenu.getComponent(i);
                extensionMenus.add(m);
            }

            LoggerUtils.logInfo("extensionMenus: %s", extensionMenus);

            //get Extension menu index
            int index = topPopupMenu.getComponentIndex(extensionParentItem);
            //add extension menus to the top popup menu after the extensions menu
            for (JMenu extensionMenu : extensionMenus) {
                topPopupMenu.add(extensionMenu, ++index);
                LoggerUtils.logInfo("add extensionMenu: %s", extensionMenu);
            }
        }
    }
}
