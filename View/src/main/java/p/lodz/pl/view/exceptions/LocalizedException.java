package p.lodz.pl.view.exceptions;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import p.lodz.pl.view.LanguageBundle;

public class LocalizedException extends Exception {
    static final String ROOT = "exception.";
    private static final Logger log = LoggerFactory.getLogger(LocalizedException.class);

    public LocalizedException() {
        log.error(LocalDateTime.now().toString() + ':' + getCause());
    }

    public LocalizedException(String message) {
        super(message);
    }

    public LocalizedException(String message, Throwable cause) {
        super(message, cause);
    }

    String attemptToTranslate(String str) {
        try {
            return LanguageBundle.getInstance().get(ROOT + str);
        } catch (NoAvailableTranslationException e) {
            return str;
        }
    }

    public void viewLog(String cause) {
        log.error(cause);
    }

    public String quote(String str) {
        return "`" + str + "'";
    }

    public String join(String... strings) {
        return String.join(" ", strings);
    }
}
