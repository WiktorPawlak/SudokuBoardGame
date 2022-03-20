package p.lodz.pl.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuSectionTest {

  private final int[][] completedBoard = { //
          {3, 1, 6, 5, 7, 8, 4, 9, 2}, //
          {5, 2, 9, 1, 3, 4, 7, 6, 8}, //
          {4, 8, 7, 6, 2, 9, 5, 3, 1}, //
          {2, 6, 3, 4, 1, 5, 9, 8, 7}, //
          {9, 7, 4, 8, 6, 3, 1, 2, 5}, //
          {8, 5, 1, 7, 9, 2, 6, 4, 3}, //
          {1, 3, 8, 9, 4, 7, 2, 5, 6}, //
          {6, 9, 2, 3, 5, 1, 8, 7, 4}, //
          {7, 4, 5, 2, 8, 6, 3, 1, 9}};
  private final int[][] testBoard = { //
          {1, 0, 0, 0, 0, 0, 0, 0, 0}, //
          {0, 2, 0, 0, 0, 0, 0, 0, 0}, //
          {0, 0, 3, 0, 0, 0, 0, 0, 0}, //
          {0, 0, 0, 4, 0, 0, 0, 0, 0}, //
          {0, 0, 0, 0, 5, 0, 0, 0, 0}, //
          {0, 0, 0, 0, 0, 6, 0, 0, 0}, //
          {0, 0, 0, 0, 0, 0, 7, 0, 0}, //
          {0, 0, 0, 0, 0, 0, 0, 8, 0}, //
          {0, 0, 0, 0, 0, 0, 0, 0, 9}};
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
  private SudokuBoard correct;
  private SudokuBoard test;
  private SudokuBoard wrong;

  SudokuSectionTest() {
    correct = new SudokuBoard(new BacktrackingSudokuSolver());
    test = new SudokuBoard(new BacktrackingSudokuSolver());
    wrong = new SudokuBoard(new BacktrackingSudokuSolver());
    correct.setBoard(completedBoard);
    test.setBoard(testBoard);
    wrong.setBoard(wrongBoard);
  }

  @Test
  public void columnTestNegative() {
    assertFalse(wrong.getColumn(1).verify());
    assertFalse(wrong.getColumn(0).verify());
  }

  @Test
  public void columnTestPositive() {
    assertTrue(correct.getColumn(1).verify());
  }

  @Test
  public void test_column_does_its_thing() {
    assertTrue(test.getColumn(5).contains(6));
    assertFalse(test.getColumn(5).contains(5));
  }

  @Test
  public void rowTestNegative() {
    assertFalse(wrong.getRow(1).verify());
    assertTrue(wrong.getRow(0).contains(1));
    assertTrue(wrong.getRow(0).contains(0));
    assertFalse(wrong.getRow(0).contains(2));
  }

  @Test
  public void rowTestPositive() {
    assertTrue(correct.getRow(1).verify());
  }

  @Test
  public void test_row_does_its_thing() {
    assertTrue(test.getRow(5).contains(6));
    assertFalse(test.getRow(5).contains(5));
  }

  @Test
  public void boxTestNegative() {
    assertFalse(wrong.getBox(1, 1).verify());
  }

  @Test
  public void boxTestPositive() {
    assertTrue(correct.getBox(7, 0).verify());
  }

  @Test
  public void boxTestNegative2() {
    SudokuBoard board = SudokuBoardPrototypeFactory.create();
    board.setBoard(completedBoard);
    board.set(1, 1, 8);

    assertFalse(board.getBox(0, 0).verify());
  }

  @Test
  public void test_box_does_its_thing() {
    assertTrue(test.getBox(5, 5).contains(5));
    assertTrue(wrong.getBox(1, 8).contains(8));
  }

  @Test
  public void test_equals_positive() {
    test.setBoard(completedBoard);
    for (int i = 0; i < 9; i++) {
      assertEquals(correct.getColumn(i), test.getColumn(i));
      assertEquals(correct.getRow(i), test.getRow(i));
      assertEquals(correct.getBox(i, i), test.getBox(i, i));
    }
  }

  @Test
  public void test_equals_negative() {
    test.setBoard(wrongBoard);
    for (int i = 0; i < 9; i++) {
      boolean b = correct.getColumn(i).equals(test.getColumn(i))
              && correct.getRow(i).equals(test.getRow(i))
              && correct.getBox(i, i).equals(test.getBox(i, i));
      assertFalse(b);
    }
  }

  @Test
  public void test_sudoku_section_not_being_a_cat() {
    Object test = new SudokuColumn(correct, 5);
    assertNotEquals(test, "kitty");
  }

  @Test
  public void test_hashCode_positive() {
    test.setBoard(completedBoard);
    for (int i = 0; i < 9; i++) {
      SudokuSection section1 = correct.getColumn(i);
      SudokuSection section2 = test.getColumn(i);

      assertEquals(section1.hashCode(), section2.hashCode());

      section1 = correct.getRow(i);
      section2 = test.getRow(i);

      assertEquals(section1.hashCode(), section2.hashCode());

      section1 = correct.getBox(i, i);
      section2 = test.getBox(i, i);

      assertEquals(section1.hashCode(), section2.hashCode());
    }
  }

  @Test
  public void test_hashCode_negative() {
    test.setBoard(wrongBoard);
    for (int i = 0; i < 9; i++) {
      SudokuSection section1 = correct.getColumn(i);
      SudokuSection section2 = test.getColumn(i);

      assertNotEquals(section1.hashCode(), section2.hashCode());

      section1 = correct.getRow(i);
      section2 = test.getRow(i);

      assertNotEquals(section1.hashCode(), section2.hashCode());

      section1 = correct.getBox(i, i);
      section2 = test.getBox(i, i);

      assertNotEquals(section1.hashCode(), section2.hashCode());
    }
  }
}
