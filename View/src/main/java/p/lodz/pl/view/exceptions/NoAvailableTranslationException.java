package p.lodz.pl.view.exceptions;

public class NoAvailableTranslationException extends LocalizedException {
  private String lang;
  private String key;

  public NoAvailableTranslationException(String lang, String key) {
    super(lang + " " + key);
    this.lang = lang;
    this.key = key;
  }

  @Override
  public String getLocalizedMessage() {
    return join(quote(key), attemptToTranslate("notTranslatedTo"), quote(lang));

  }
}
