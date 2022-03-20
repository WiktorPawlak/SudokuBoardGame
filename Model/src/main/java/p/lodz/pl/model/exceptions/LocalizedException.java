package p.lodz.pl.model.exceptions;

import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import p.lodz.pl.model.ModelLanguageBundle;

public class LocalizedException extends Exception {
    static final String ROOT = "exception.";
    private static final Logger log = LoggerFactory.getLogger(LocalizedException.class);

    public LocalizedException() {
    }

    public LocalizedException(String message) {
        super(message);
    }

    public LocalizedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected String attemptToTranslate(String str) {
       return ModelLanguageBundle.getInstance().get(ROOT + str);
    }

    public void modelLog(String cause) {
        log.error(cause);
    }

    public String quote(String str) {
        return "{" + str + "}";
    }

    public String join(String... strings) {
        return Stream.of(strings).reduce("", (body, last) -> body + " " + last).substring(1);
    }
}
