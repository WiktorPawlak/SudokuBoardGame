package p.lodz.pl.view;

import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import p.lodz.pl.model.exceptions.UnspecifiedLevelException;
import p.lodz.pl.view.exceptions.InitializeException;
import p.lodz.pl.view.exceptions.NoAvailableTranslationException;

public class StageController implements IMediator, ILanguageDependent {

  private Stage stage;

  private void switchTo(Scene scene) {
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void switchToMenu() {
    switchTo(menu.scene);
  }

  @Override
  public void switchToBoard() {
    switchTo(board.scene);
  }

  private SceneController<MenuController> menu = new SceneController<>();
  private SceneController<BoardController> board = new SceneController<>();
  private Media meow;

  public StageController initialize(Stage stage)//
      throws InitializeException {
    this.stage = stage;
    menu.initialize("menu.fxml", "menu");
    board.initialize("board.fxml", "board").addStyle("layout.css");
    LanguageBundle.getInstance().addObserver(this);
    initializeMeower();
    return this;
  }

  @Override
  public String getChosenLevelName() {
    return menu.controller.getDifficulty();
  }

  public void initializeBoardController() throws UnspecifiedLevelException {
    board.controller.initBoard();
  }

  public static StageController getInstance() {
    return ControllerMediatorHolder.INSTANCE;
  }

  private static class ControllerMediatorHolder {
    private static final StageController INSTANCE = new StageController();
  }

  @Override
  public void switchLanguage() {
    try {
      stage.setTitle(LanguageBundle.getInstance().get("window.title"));
    } catch (NoAvailableTranslationException e) {
      e.viewLog(e.getLocalizedMessage());
    }
  }

  public void wouldynya() {
    new MediaPlayer(meow).play();
  }

  private void initializeMeower() {
    String file = "neco_arc.wav";
    meow = new Media(getClass().getResource(file).toExternalForm());
  }
}
