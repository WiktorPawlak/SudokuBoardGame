package p.lodz.pl.model.exceptions;

public class SerializationException extends LocalizedException {
  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("serialization");
  }
}
