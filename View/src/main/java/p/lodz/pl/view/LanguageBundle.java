package p.lodz.pl.view;

import java.util.*;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import p.lodz.pl.view.exceptions.NoAvailableTranslationException;
import p.lodz.pl.view.exceptions.NotaLabelException;

public class LanguageBundle {
  private Locale locale;
  private ResourceBundle bundle;
  private List<ILanguageDependent> listeners;
  private String languageCode = "en";

  private static final LanguageBundle languageBundle = new LanguageBundle();

  private LanguageBundle() {
  }

  public static LanguageBundle getInstance() {
    return languageBundle;
  }

  public void initialize() {
    setLanguage("en");
    listeners = new ArrayList<>();

  }

  public void setLanguage(String lang) {
    languageCode = lang;
    locale = new Locale.Builder().setLanguage(lang).build();
    bundle = ResourceBundle.getBundle("p.lodz.pl.view.app", locale);
  }

  public void switchLanguages() {
    for (ILanguageDependent it : listeners) {
      it.switchLanguage();
    }
  }

  public String get(String key) throws NoAvailableTranslationException {
    try {
      return bundle.getString(key);
    } catch (NullPointerException | MissingResourceException e) {
      throw new NoAvailableTranslationException(languageCode, key);
    }
  }

  public void addObserver(ILanguageDependent o) {
    listeners.add(o);
    o.switchLanguage();
  }

  public void removeObserver(ILanguageDependent o) {
    listeners.remove(o);
  }

  public void set(Node thing, String key) //
      throws NotaLabelException, NoAvailableTranslationException {
    try {
      Labeled l = (Labeled) thing;
      l.setText(get(key));
    } catch (ClassCastException e) {
      throw new NotaLabelException(thing.getId());
    }
  }

  public static void changeElementLanguage(String root, Node thing)
      throws NotaLabelException, NoAvailableTranslationException {
    String fullName = root + "." + thing.getId();
    getInstance().set(thing, fullName);
  }
}
