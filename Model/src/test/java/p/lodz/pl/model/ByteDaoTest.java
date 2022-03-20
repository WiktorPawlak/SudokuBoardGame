package p.lodz.pl.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ByteDaoTest {

  private final int[][] completedBoard = {
          {3, 1, 6, 5, 7, 8, 4, 9, 2},
          {5, 2, 9, 1, 3, 4, 7, 6, 8},
          {4, 8, 7, 6, 2, 9, 5, 3, 1},
          {2, 6, 3, 4, 1, 5, 9, 8, 7},
          {9, 7, 4, 8, 6, 3, 1, 2, 5},
          {8, 5, 1, 7, 9, 2, 6, 4, 3},
          {1, 3, 8, 9, 4, 7, 2, 5, 6},
          {6, 9, 2, 3, 5, 1, 8, 7, 4},
          {7, 4, 5, 2, 8, 6, 3, 1, 9}};

  private final int[][] wrongBoard = { //
          {0, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {1, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {2, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {3, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {4, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {5, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {6, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {7, 1, 1, 1, 1, 1, 1, 1, 1}, //
          {8, 1, 1, 1, 1, 1, 1, 1, 1}};

  @Test
  public void test_sudokuBoard_cloneable() throws CloneNotSupportedException {
    SudokuBoard clone;
    SudokuBoard sb = new SudokuBoard(new BacktrackingSudokuSolver());
    sb.setBoard(completedBoard);

    clone = sb.clone();
    assertEquals(clone, sb);

    sb.setBoard(wrongBoard);
    assertNotSame(clone, sb);
  }

  @Test
  public void test_field_cloneable() {
    SudokuField field = new SudokuField();
    field.setFieldValue(6);

    SudokuField clone = field.clone();
    assertEquals(field, clone);

    field.setFieldValue(9);
    assertNotSame(field, clone);
  }

  @Test
  public void test_sudokuSection_cloneable() throws CloneNotSupportedException {
    SudokuSection clone;
    SudokuBoard correct = new SudokuBoard(new BacktrackingSudokuSolver());

    correct.setBoard(completedBoard);
    SudokuSection sectionColumn = correct.getColumn(0);
    SudokuSection sectionRow = correct.getRow(0);
    SudokuSection sectionBox = correct.getBox(0,0);


    clone = sectionColumn.clone();
    assertEquals(clone, sectionColumn);
    assertNotSame(sectionColumn, clone);

    clone = sectionRow.clone();
    assertEquals(clone, sectionRow);
    assertNotSame(sectionRow, clone);

    clone = sectionBox.clone();
    assertEquals(clone, sectionBox);
    assertNotSame(sectionBox, clone);

    correct.setBoard(wrongBoard);
    sectionColumn = correct.getColumn(0);
    sectionRow = correct.getRow(0);
    sectionBox = correct.getBox(0,0);


    clone = sectionColumn.clone();
    assertEquals(clone, sectionColumn);
    assertNotSame(sectionColumn, clone);

    clone = sectionRow.clone();
    assertEquals(clone, sectionRow);
    assertNotSame(sectionRow, clone);

    clone = sectionBox.clone();
    assertEquals(clone, sectionBox);
    assertNotSame(sectionBox, clone);
  }
}
