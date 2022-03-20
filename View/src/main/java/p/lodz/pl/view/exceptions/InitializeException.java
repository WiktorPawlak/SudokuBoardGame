package p.lodz.pl.view.exceptions;

public class InitializeException extends LocalizedException {
  public InitializeException(String message) {
    super(message);
  }

  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("cannotInitialize");
  }
}
