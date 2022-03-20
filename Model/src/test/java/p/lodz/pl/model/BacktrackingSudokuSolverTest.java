package p.lodz.pl.model;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BacktrackingSudokuSolverTest {
  private final int[][] grid = { //
      { 3, 0, 6, 5, 0, 8, 4, 0, 0 }, //
      { 5, 2, 0, 0, 0, 0, 0, 0, 0 }, //
      { 0, 8, 7, 0, 0, 0, 0, 3, 1 }, //
      { 0, 0, 3, 0, 1, 0, 0, 8, 0 }, //
      { 9, 0, 0, 8, 6, 3, 0, 0, 5 }, //
      { 0, 5, 0, 0, 9, 0, 6, 0, 0 }, //
      { 1, 3, 0, 0, 0, 0, 2, 5, 0 }, //
      { 0, 0, 0, 0, 0, 0, 0, 7, 4 }, //
      { 0, 0, 5, 2, 0, 6, 3, 0, 0 } };

  private final int[][] unsolvableGrid = { //
      { 3, 0, 6, 5, 0, 8, 4, 0, 3 }, //
      { 5, 2, 0, 0, 0, 0, 0, 0, 0 }, //
      { 0, 8, 7, 0, 0, 0, 0, 3, 1 }, //
      { 0, 0, 3, 0, 1, 0, 0, 8, 0 }, //
      { 9, 0, 0, 8, 6, 3, 0, 0, 5 }, //
      { 0, 5, 0, 0, 9, 0, 6, 0, 0 }, //
      { 1, 3, 0, 0, 0, 0, 2, 5, 0 }, //
      { 0, 0, 0, 0, 0, 0, 0, 7, 4 }, //
      { 0, 0, 5, 2, 0, 6, 3, 0, 0 } };

  @Test
  public void test_solve_positive() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(grid);
    sb.solveGame();
    assertTrue(sb.checkBoard());
  }

  @Test
  public void test_solve_useless() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(unsolvableGrid);
    System.out.println(sb);
    assertFalse(sb.solveGame());
    assertFalse(sb.checkBoard());
  }

  @Test
  public void test_solve_negative() {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    sb.setBoard(grid);
    assertFalse(sb.checkBoard());
  }

  @Test
  @Description("Checks whether 2 consecutive fillments return different layouts")
  public void test_random_fill() {
    SudokuBoard sb1 = SudokuBoardPrototypeFactory.create();
    SudokuBoard sb2 = SudokuBoardPrototypeFactory.create();
    sb1.solveGame();
    sb2.solveGame();
    int[][] retBoard1 = sb1.getBoard();
    int[][] retBoard2 = sb2.getBoard();

    boolean identical = true;
    stopRunningAround: {
      for (int x = 0; x < 9; ++x) {
        for (int y = 0; y < 9; ++y) {
          if (retBoard1[x][y] != retBoard2[x][y]) {
            identical = false;
            break stopRunningAround;
          }
        }
      }
    }
    assertFalse(identical);
    // printCurrentState(sb1);
    // printCurrentState(sb2); //for debugging
  }
}
