package p.lodz.pl.view.exceptions;

public class NotaLabelException extends LocalizedException {
  public NotaLabelException(String id) {
    super(id);
  }

  @Override
  public String getLocalizedMessage() {
    return join(quote(getMessage()), attemptToTranslate("notALabel"));
  }

}
