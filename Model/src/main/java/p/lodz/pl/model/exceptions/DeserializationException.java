package p.lodz.pl.model.exceptions;

public class DeserializationException extends LocalizedException {
  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("deserialization");
  }
}
