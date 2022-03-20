package p.lodz.pl.model;

import java.util.Locale;
import java.util.ResourceBundle;

public class ModelLanguageBundle {
  private Locale locale;
  private ResourceBundle bundle;
  private static ModelLanguageBundle languageBundle = new ModelLanguageBundle();

  private ModelLanguageBundle() {
  }

  public static ModelLanguageBundle getInstance() {
    return languageBundle;
  }

  public void setLanguage(String lang) {
    locale = new Locale.Builder().setLanguage(lang).build();
    bundle = ResourceBundle.getBundle("logic", locale);
  }

  public String get(String key) {
    return bundle.getString(key);
  }
}
