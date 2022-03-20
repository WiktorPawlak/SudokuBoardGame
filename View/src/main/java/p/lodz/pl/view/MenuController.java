package p.lodz.pl.view;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import p.lodz.pl.model.exceptions.UnspecifiedLevelException;

public class MenuController implements ILanguageDependent {
  @FXML
  private ToggleGroup difficulty;
  @FXML
  private Pane languageIcons;
  LanguageController languageController;

  @FXML
  protected void onStartButtonClick() throws UnspecifiedLevelException {
    StageController stageController = StageController.getInstance();
    stageController.initializeBoardController();
    stageController.switchToBoard();
  }

  @FXML
  public void initialize() {
    languageController = new LanguageController(languageIcons);
    languageController.cycle();
  }

  public String getDifficulty() {
    RadioButton button = (RadioButton) difficulty.getSelectedToggle();
    return button.getId();
  }

  @FXML
  protected void onActionLanguageButton() {
    languageController.cycle();
  }

  @Override
  public void switchLanguage() {
  }
}
