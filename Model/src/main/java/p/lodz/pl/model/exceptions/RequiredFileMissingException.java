package p.lodz.pl.model.exceptions;

public class RequiredFileMissingException extends LocalizedException {
  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("requiredFileMissing");
  }
}
