//    sudoku thing; projekt na zajęcia z PROKOM 2021-2022
//    Copyright (C) 2021  Antoni Jończyk, Wiktor Pawlak
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as published
//    by the Free Software Foundation, either version 3 of the License, or (at
//    your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package p.lodz.pl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity(name = "sudokuboard")
@Table(name = "SUDOKUBOARD")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SudokuBoard implements IObserver, IObservable, Serializable, Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Integer id;
  private String boardTitle;
  private boolean valid = false;

  @Transient
  public static final int SIZE = 9;
  @Transient
  private ISudokuSolver sudokuSolver;
  @Transient
  private final ArrayList<IObserver> observers = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "board_id")
  private final List<SudokuField> fields = Arrays.asList(new SudokuField[SIZE * SIZE]);

  @Transient
  private final List<SudokuSection> columns = Arrays.asList(new SudokuSection[SIZE]);

  @Transient
  private final List<SudokuSection> rows = Arrays.asList(new SudokuSection[SIZE]);

  @Transient
  private final List<SudokuSection> boxes = Arrays.asList(new SudokuSection[SIZE]);

  protected SudokuBoard() {
  }

  public SudokuBoard(ISudokuSolver algorithm) {
    sudokuSolver = algorithm;
    for (int i = 0; i < SIZE * SIZE; i++) {
      fields.set(i, new SudokuField());
    }

    for (int i = 0; i < SIZE; i++) {
      columns.set(i, new SudokuColumn(this, i));
      rows.set(i, new SudokuRow(this, i));
      boxes.set(i, new SudokuBox(this, i));
    }
  }

  public boolean solveGame() {
    return sudokuSolver.solve(this);
  }

  public int[][] getBoard() {
    return copyBoard();
  }

  private SudokuField getField(int x, int y) {
    return fields.get(x + y * SIZE);
  }

  public void giveSudokuField(SudokuSection section, int i, int x, int y) {
    section.receiveSudokuField(i, getField(x, y));
  }

  public void registerObserver(IObserver o, int x, int y) {
    getField(x, y).registerObserver(o);
  }

  public void registerObserver(IObserver observer) {
    observers.add(observer);
  }

  @Override
  public void notifyObservers() {
    for (IObserver o : observers) {
      o.update();
    }
  }

  public int get(int x, int y) {
    return getField(x, y).getFieldValue();
  }

  public boolean getEditable(int x, int y) {
    return getField(x, y).isEditable();
  }

  public void setEditable(int x, int y, boolean edit) {
    getField(x, y).setEditable(edit);
  }

  public void set(int x, int y, int value) {
    SudokuField field = getField(x, y);
    int old = field.getFieldValue();
    field.setFieldValue(value);
    if (old != value) {
      notifyObservers();
    }
  }

  public void setBoard(int[][] replacement) {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        set(i, j, replacement[i][j]);
      }
    }
  }

  private int[][] copyBoard() {
    int[][] copy = new int[SIZE][SIZE];
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        copy[i][j] = get(i, j);
      }
    }
    return copy;
  }

  public boolean getValid() {
    return valid;
  }

  public boolean checkBoard() {
    return getValid();
  }

  private boolean checkSections(List<SudokuSection> sections) {
    for (SudokuSection section : sections) {
      if (!section.verify()) {
        return false;
      }
    }
    return true;
  }

  public SudokuColumn getColumn(int column) {
    return (SudokuColumn) columns.get(column);
  }

  public SudokuRow getRow(int row) {
    return (SudokuRow) rows.get(row);
  }

  public SudokuBox getBox(int col, int row) {
    return (SudokuBox) boxes.get(row - row % 3 + (col / 3));
  }

  @Override
  public void update() {
    valid = checkSections(rows) && checkSections(boxes) && checkSections(columns);
  }

  public int hashCode() {
    return Arrays.deepHashCode(this.getBoard());
  }

  @Override
  public String toString() {
    StringBuilder ans = new StringBuilder("board '" + boardTitle + "' is ");
    if (valid) {
      ans.append("valid");
    } else {
      ans.append("invalid");
    }
    ans.append(", contains:\n");

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 8; j++) {
        ans.append(getField(i, j)).append(", ");
      }
      ans.append(getField(i, 8)).append("\n");
    }
    ans.append("\nthe following sections:\n\n");

    ans.append("Rows:\n");
    for (SudokuSection s : rows) {
      ans.append(s).append("\n");
    }

    ans.append("\nColumns:\n");
    for (SudokuSection s : columns) {
      ans.append(s).append("\n");
    }
    ans.append("\nBoxes:\n");
    for (SudokuSection s : boxes) {
      ans.append(s).append("\n");
    }
    ans.append("========================================");
    return ans.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    return EqualsBuilder.reflectionEquals(this.getBoard(), ((SudokuBoard) obj).getBoard());
  }

  @Override
  public SudokuBoard clone() {
    ByteDao<SudokuBoard> byteDao = new ByteDao<>();

    SudokuBoard clone;
    try {
      byteDao.write(this);
      clone = byteDao.read();
    } catch (Exception e) {
      clone = new SudokuBoard(new BacktrackingSudokuSolver());
      clone.setBoard(getBoard());
      clone.copyEditability(this);
    }
    return clone;
  }

  public void makeEmptyFieldsEditable() {
    for (SudokuField field : fields) {
      field.setEditable(field.getFieldValue() == 0);
    }
  }

  public void copyEditability(SudokuBoard other) {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        setEditable(i, j, other.getEditable(i, j));
      }
    }
  }

  public Integer getId() {
    return id;
  }

  public String getBoardTitle() {
    return boardTitle;
  }

  public void setBoardTitle(String boardTitle) {
    this.boardTitle = boardTitle;
  }

  public void absorbSudokuBoard(SudokuBoard sb) {
    id = sb.getId();
    boardTitle = sb.getBoardTitle();
    setBoard(sb.getBoard());
    copyEditability(sb);
  }
}
