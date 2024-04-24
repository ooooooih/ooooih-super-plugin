package org.ooooih.burp.context;

import lombok.Getter;

/**
 * @author ooooooih
 */
public class BurpContext {

    @Getter
    private static int topMenuEnabled = 0;

    public static int switchTopMenu() {
        return topMenuEnabled = 1 - topMenuEnabled;
    }
}
