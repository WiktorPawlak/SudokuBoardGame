package p.lodz.pl.view;

import javafx.stage.Stage;
import p.lodz.pl.view.exceptions.InitializeException;
import p.lodz.pl.view.exceptions.SceneNotFoundException;

public interface IMediator {
  IMediator initialize(Stage stage) throws InitializeException, SceneNotFoundException;

  String getChosenLevelName();

  void switchToBoard();

  void switchToMenu();
}
