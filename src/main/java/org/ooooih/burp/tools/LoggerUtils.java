package org.ooooih.burp.tools;

import org.ooooih.burp.extension.SuperPlugin;

/**
 * @author ooooooih
 */
public class LoggerUtils {

    public static boolean enable = true;
    public static void logInfo(String message, Object... args) {
        if (enable) {
            SuperPlugin.getApi().logging().logToOutput(message.formatted(args));
        }
    }

    public static void logInfo(String message) {
        if (enable) {
            SuperPlugin.getApi().logging().logToOutput(message);
        }
    }

    public static void logError(String message, Throwable e) {
        if (enable) {
            SuperPlugin.getApi().logging().logToError(message, e);
        }
    }
}
