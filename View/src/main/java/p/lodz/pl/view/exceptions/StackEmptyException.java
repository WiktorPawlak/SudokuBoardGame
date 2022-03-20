package p.lodz.pl.view.exceptions;

public class StackEmptyException extends LocalizedException {
  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("stackEmpty");
  }
}
