package p.lodz.pl.model.exceptions;

public class UnspecifiedLevelException extends LocalizedException {
  private String level;

  public UnspecifiedLevelException(String level) {
    super(level);
    this.level = level;
  }

  @Override
  public String getLocalizedMessage() {
    return attemptToTranslate("unspecifiedLevel: " + level);
  }
}
