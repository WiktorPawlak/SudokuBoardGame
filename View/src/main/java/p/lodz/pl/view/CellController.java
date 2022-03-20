package p.lodz.pl.view;

import java.util.Objects;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import p.lodz.pl.model.IObserver;
import p.lodz.pl.model.SudokuBoard;
import p.lodz.pl.view.exceptions.CharacterTypeException;

public class CellController implements IObserver {
  private transient StackPane cell;
  private transient TextField textField;
  private final int col;
  private final int row;
  private final SudokuBoard board;
  private static final int EMPTY = 0;

  transient EventHandler<KeyEvent> eventHandler = event -> {
    try {
      updateDisplayedText(event.getCharacter());
    } catch (CharacterTypeException e) {
      e.viewLog(e.getLocalizedMessage());
    }
    event.consume();
  };

  public CellController(int col, int row, SudokuBoard sudokuBoard) {
    this.col = col;
    this.row = row;
    this.board = sudokuBoard;
    setup();
    update();
  }

  private void setup() {
    initializeCell();
    initializeTextField();
    if (isEditable()) {
      listenToModel();
      listenToView();
    }
  }

  private void listenToView() {
    textField.addEventFilter(KeyEvent.KEY_TYPED, eventHandler);
  }

  private void listenToModel() {
    board.registerObserver(this, col, row);
  }

  @Override
  public void update() {
    if (Objects.nonNull(textField)) {
      textField.setText(getText());
    }
  }

  private String getText() {
    if (isEmpty()) {
      return "";
    } else {
      return String.valueOf(getValue());
    }
  }

  private void initializeTextField() {
    textField = new TextField();
    textField.setMinSize(50, 50);
    textField.setFont(Font.font(18));
    textField.setEditable(isEditable());
    textField.setAlignment(Pos.CENTER);
    cell.getChildren().add(textField);
    if (!isEditable()) {
      // to chyba powinno być w css, tak, żeby tutaj ustawić tylko klasę ツ
      textField.setStyle("-fx-background-color: lightgray;");
    }
  }

  private void initializeCell() {
    cell = new StackPane();
    cell.getStyleClass().add("cell");
    cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("right"), col == 2 || col == 5);
    cell.pseudoClassStateChanged(PseudoClass.getPseudoClass("bottom"), row == 2 || row == 5);
  }

  private int getValue() {
    return board.get(col, row);
  }

  private boolean isEditable() {
    return board.getEditable(col, row);
  }

  private void updateModelValue(int value) {
    board.set(col, row, value);
  }

  public StackPane getCell() {
    return cell;
  }

  private boolean isEmpty() {
    return getValue() == EMPTY;
  }

  private void updateDisplayedText(String character) throws CharacterTypeException {
    updateModelValue(parseUserInputToInt(character));
  }

  private int parseUserInputToInt(String character) throws CharacterTypeException {
    int entered;
    try {
      entered = Integer.parseInt(character);
    } catch (NumberFormatException e) {
      throw new CharacterTypeException(character);
    }
    return entered;
  }
}
