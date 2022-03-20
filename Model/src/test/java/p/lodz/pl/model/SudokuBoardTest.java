package p.lodz.pl.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardTest {

  private final int[][] completedBoard = { //
      { 3, 1, 6, 5, 7, 8, 4, 9, 2 }, //
      { 5, 2, 9, 1, 3, 4, 7, 6, 8 }, //
      { 4, 8, 7, 6, 2, 9, 5, 3, 1 }, //
      { 2, 6, 3, 4, 1, 5, 9, 8, 7 }, //
      { 9, 7, 4, 8, 6, 3, 1, 2, 5 }, //
      { 8, 5, 1, 7, 9, 2, 6, 4, 3 }, //
      { 1, 3, 8, 9, 4, 7, 2, 5, 6 }, //
      { 6, 9, 2, 3, 5, 1, 8, 7, 4 }, //
      { 7, 4, 5, 2, 8, 6, 3, 1, 9 } };

  @Test
  public void test_board_returning() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    int[][] b1 = sb.getBoard(); // fresh board
    int sav = b1[0][0]; // saving some value
    assertTrue(sav != 85491); // to prevent other monkey from chating
    b1[0][0] = 85491; // changing own copy
    int[][] b2 = sb.getBoard(); // and comparing fresh one
    assertEquals(sav, b2[0][0]); // to the saved value
  } // gdyby nie TDD, to ten test by przechodził

  @Test
  public void test_board_size() {
    BacktrackingSudokuSolver algorithm = new BacktrackingSudokuSolver();
    int[][] b = new SudokuBoard(algorithm).getBoard();
    assertEquals(9, b.length);
    assertEquals(9, b[0].length);
  }

  @Test
  public void test_board_filling_doesnt_result_in_catastrophic_failure() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.solveGame();
    System.out.println(sb);
  }

  @Test
  public void test_copy_board() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    int[][] returnedBoard = sb.getBoard();
    for (int x = 0; x < 9; ++x) {
      for (int y = 0; y < 9; ++y) {
        assertEquals(returnedBoard[x][y], completedBoard[x][y]);
      }
    }
  }

  @Test
  public void test_get() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    assertEquals(sb.getBoard()[6][6], 2);
  }

  @Test
  public void test_small_get() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    assertEquals(sb.get(6, 6), 2);
  }

  @Test
  public void test_set_positive() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb.set(0, 0, 1);
    assertEquals(1, sb.getBoard()[0][0]);
  }

  @Test
  public void test_set_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb.set(0, 0, 1000);
    assertNotEquals(1000, sb.getBoard()[0][0]);
  }

  @Test
  public void test_set_negative2() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb.set(0, 0, -1);
    assertNotEquals(-1, sb.getBoard()[0][0]);
  }

  @Test
  public void test_check_board_positive() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    assertTrue(sb.checkBoard());
  }

  @Test
  public void test_check_board_row_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb.set(0, 1, 6);
    assertFalse(sb.checkBoard());
  }

  @Test // todo:: repair
  public void test_check_board_column_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb.set(1, 0, 3);
    assertFalse(sb.checkBoard());
  }

  @Test // todo:: repair
  public void test_check_board_box_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb.set(2, 2, 3);
    assertFalse(sb.checkBoard());
  }

  @Test
  public void test_solve_game() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    assertTrue(sb.solveGame());
  }

  @Test
  public void test_equals_positive() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    SudokuBoard sb2 = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb2.setBoard(completedBoard);

    assertEquals(sb, sb);
    assertEquals(sb, sb2);
  }

  @Test
  public void test_equals_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    SudokuBoard sb2 = SudokuBoardPrototypeFactory.create();
    sb.solveGame();
    sb2.solveGame();
    assertNotEquals(sb, sb2);
    assertNotEquals(sb, null);
  }

  @Test
  void test_sudoku_board_not_being_a_cat() {
    Object test = SudokuBoardPrototypeFactory.create();
    assertNotEquals(test, "kitty");
  }

  @Test
  public void test_hashCode_positive() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    SudokuBoard sb2 = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb2.setBoard(completedBoard);

    assertEquals(sb.hashCode(), sb2.hashCode());
  }

  @Test
  public void test_hashCode_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    SudokuBoard sb2 = SudokuBoardPrototypeFactory.create();
    sb.setBoard(completedBoard);
    sb2.solveGame();
    assertNotEquals(sb.hashCode(), sb2.hashCode());
  }
}
