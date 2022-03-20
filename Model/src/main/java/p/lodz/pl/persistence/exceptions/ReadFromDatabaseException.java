package p.lodz.pl.persistence.exceptions;

import p.lodz.pl.model.exceptions.LocalizedException;

public class ReadFromDatabaseException extends LocalizedException {
  Throwable exception;

  public ReadFromDatabaseException(Exception e) {
    exception = e;
  }

  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("readFromDb")
            + " " + quote(exception.getLocalizedMessage());
  }
}
