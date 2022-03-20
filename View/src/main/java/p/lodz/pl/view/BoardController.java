package p.lodz.pl.view;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import p.lodz.pl.model.IObserver;
import p.lodz.pl.model.Level;
import p.lodz.pl.model.SudokuBoard;
import p.lodz.pl.model.SudokuBoardDaoFactory;
import p.lodz.pl.model.exceptions.DeserializationException;
import p.lodz.pl.model.exceptions.RequiredFileMissingException;
import p.lodz.pl.model.exceptions.SerializationException;
import p.lodz.pl.model.exceptions.UnspecifiedLevelException;
import p.lodz.pl.persistence.SudokuBoardDao;
import p.lodz.pl.persistence.exceptions.ReadFromDatabaseException;
import p.lodz.pl.persistence.exceptions.SaveToDatabaseException;
import p.lodz.pl.view.exceptions.NoAvailableTranslationException;
import p.lodz.pl.view.exceptions.StackEmptyException;

public class BoardController implements ILanguageDependent, IObserver {
  @FXML
  private GridPane sudokuBoardGrid;
  @FXML
  private TextField saveDbTextField;
  @FXML
  private TextField readDbTextField;
  private SudokuBoard sudokuBoard;
  private final Logger logger = LoggerFactory.getLogger(BoardController.class);

  private class History {
    private Stack<SudokuBoard> stack = new Stack<SudokuBoard>();

    private void start() {
      stack.clear();
      save();
    }

    private void save() {
      stack.push(sudokuBoard.clone());
    }

    private void discardTop() {
      if (!stack.empty() && stack.peek().equals(sudokuBoard)) {
        stack.pop();
        discardTop();
      }
    }

    private boolean undo() throws StackEmptyException {
      discardTop();
      if (!stack.empty()) {
        connectWithBoard(stack.pop());
        return true;
      } else {
        throw new StackEmptyException();
      }
    }
  }

  private History history = new History();
  private final List<CellController> cells = //
          Arrays.asList(new CellController[SudokuBoard.SIZE * SudokuBoard.SIZE]);

  @FXML
  protected void initBoard() throws UnspecifiedLevelException {
    Level level = new Level(StageController.getInstance().getChosenLevelName());
    connectWithBoard(sudokuBoard = level.prepareBoard());
    history.start();
  }

  private void connectWithBoard(SudokuBoard board) {
    sudokuBoard = board;
    fillGrid();
    sudokuBoard.registerObserver(this);
  }

  private void fillGrid() {
    for (int col = 0; col < SudokuBoard.SIZE; col++) {
      for (int row = 0; row < SudokuBoard.SIZE; row++) {
        CellController cell = new CellController(col, row, sudokuBoard);
        setCell(col, row, cell);
        sudokuBoardGrid.add(cell.getCell(), col, row);
      }
    }
  }

  private void setCell(int col, int row, CellController cell) {
    cells.set(col * SudokuBoard.SIZE + row, cell);
  }

  @FXML
  protected void onBackToMenuButtonClick() {
    StageController.getInstance().switchToMenu();
  }

  @FXML
  protected void onActionCheckBoard() throws NoAvailableTranslationException {
    Alert alert = new Alert(AlertType.NONE);
    if (sudokuBoard.checkBoard()) {
      alert.setAlertType(AlertType.INFORMATION);
      try {
        alert.setContentText(LanguageBundle.getInstance().get("correctState"));
      } catch (NoAvailableTranslationException e) {
        e.viewLog(e.getLocalizedMessage());
      }
    } else {
      alert.setAlertType(AlertType.WARNING);
      try {
        alert.setContentText(LanguageBundle.getInstance().get("wrongState"));
      } catch (NoAvailableTranslationException e) {
        e.viewLog(e.getLocalizedMessage());
      }
    }
    logger.info(LanguageBundle.getInstance().get("checkAssertion") + " "
            + alert.getContentText());
    alert.show();
  }

  @FXML
  protected void onActionSaveBoard() throws Throwable {
    SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
    try {
      sudokuBoardDaoFactory.getFileDao("savedSudokuGame").write(sudokuBoard);
    } catch (SerializationException e) {
      e.modelLog(e.getLocalizedMessage());
    }
    logger.info(LanguageBundle.getInstance().get("saveAssertion"));
  }

  @FXML
  protected void onActionReadSudokuBoard() throws Throwable {
    SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
    try {
      connectWithBoard(sudokuBoardDaoFactory.getFileDao("savedSudokuGame").read());
    } catch (DeserializationException | RequiredFileMissingException e) {
      e.modelLog(e.getLocalizedMessage());
    }
    logger.info(LanguageBundle.getInstance().get("readAssertion"));
    history.start(); //can be added to reading from database
  }

  @FXML
  protected void onActionSaveBoardToDb() throws NoAvailableTranslationException {
    try (SudokuBoardDao dao = new SudokuBoardDao();) {
      sudokuBoard.setBoardTitle(saveDbTextField.getText());
      dao.write(sudokuBoard);
      logger.info(LanguageBundle.getInstance().get("saveDbAssertion"));
      saveDbTextField.clear();
    } catch (SaveToDatabaseException e) {
      e.modelLog(e.getLocalizedMessage());
    }
  }

  @FXML
  protected void onActionReadSudokuBoardToDb() throws NoAvailableTranslationException {
    try (SudokuBoardDao dao = new SudokuBoardDao();) {
      dao.setBoardTitle(readDbTextField.getText());
      sudokuBoard = dao.read();
      connectWithBoard(sudokuBoard);
      logger.info(LanguageBundle.getInstance().get("readDbAssertion"));
      readDbTextField.clear();
    } catch (ReadFromDatabaseException e) {
      e.modelLog(e.getLocalizedMessage());
    }
  }

  @Override
  public void switchLanguage() {
    try {
      saveDbTextField.setPromptText(LanguageBundle.getInstance().get("board.saveDbTextField"));
      readDbTextField.setPromptText(LanguageBundle.getInstance().get("board.readDbTextField"));
    } catch (NoAvailableTranslationException e) {
      e.viewLog(e.getLocalizedMessage());
    }
  }

  @Override
  public void update() {
    history.save();
  }

  @FXML
  public void onActionUndo() {
    try {
      history.undo();
    } catch (StackEmptyException e) {
      e.viewLog(e.getLocalizedMessage());
      StageController.getInstance().wouldynya();
    }
  }
}
