package org.ooooih.burp.tools;

import org.ooooih.burp.extension.SuperPlugin;

/**
 * @author ooooooih
 */
public class LoggerUtils {

    public static boolean enable = false;
    public static void logInfo(String message, Object... args) {
        if (enable) {
            SuperPlugin.getApi().logging().logToOutput(message.formatted(args));
        }
    }
}
