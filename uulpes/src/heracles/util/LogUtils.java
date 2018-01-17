package heracles.util;

import org.apache.commons.logging.Log;

public final class LogUtils {
    public static void debug(Log logger, Object message, Throwable t) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, t);

        }
    }

    public static void debug(Log logger, Object message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
