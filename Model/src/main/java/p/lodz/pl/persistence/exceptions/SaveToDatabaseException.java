package p.lodz.pl.persistence.exceptions;

import p.lodz.pl.model.exceptions.LocalizedException;

public class SaveToDatabaseException extends LocalizedException {
  Throwable exception;

  public SaveToDatabaseException(Exception e) {
    exception = e;
  }

  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("saveToDb")
            + " " + quote(exception.getLocalizedMessage());
  }
}
