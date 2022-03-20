package p.lodz.pl.view;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Parent;
import p.lodz.pl.model.ModelLanguageBundle;

class LanguageController {
    /**
     * Handles graphical side of switching menu.
     */

    private List<Node> languages;
    private int choosenLang = 0;

    public LanguageController(Parent languageIcons) {
        languages = languageIcons.getChildrenUnmodifiable();
    }

    public void cycle() {
        cyclePosition();
        adjustVisibility();
        LanguageBundle.getInstance().setLanguage(getCurrentLanguage());
        ModelLanguageBundle.getInstance().setLanguage(getCurrentLanguage());
        LanguageBundle.getInstance().switchLanguages();
    }

    private void cyclePosition() {
        choosenLang = (choosenLang + 1) % languages.size();
    }

    private String getCurrentLanguage() {
        return languages.get(choosenLang).getId();
    }

    private void adjustVisibility() {
        for (int i = 0; i < languages.size(); i++) {
            languages.get(i).setVisible(i == choosenLang);
        }
    }
}
