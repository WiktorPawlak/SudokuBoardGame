package p.lodz.pl.model;

import org.junit.jupiter.api.Test;
import p.lodz.pl.model.exceptions.RequiredFileMissingException;

import static org.junit.jupiter.api.Assertions.*;

public class FileDaoTest {
  private SudokuBoardDaoFactory sudokuBoardDaoFactory;
  private SudokuBoard sudokuBoard;
  private SudokuBoard sudokuBoardCheck;

  private int[][] completedBoard = {
          {3, 1, 6, 5, 7, 8, 4, 9, 2},
          {5, 2, 9, 1, 3, 4, 7, 6, 8},
          {4, 8, 7, 6, 2, 9, 5, 3, 1},
          {2, 6, 3, 4, 1, 5, 9, 8, 7},
          {9, 7, 4, 8, 6, 3, 1, 2, 5},
          {8, 5, 1, 7, 9, 2, 6, 4, 3},
          {1, 3, 8, 9, 4, 7, 2, 5, 6},
          {6, 9, 2, 3, 5, 1, 8, 7, 4},
          {7, 4, 5, 2, 8, 6, 3, 1, 9}};

  FileDaoTest() {
    sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
    sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
    sudokuBoard.setBoard(completedBoard);
  }


  @Test
  public void test_write_positive() throws Throwable {
    assertTrue(sudokuBoardDaoFactory.getFileDao("testSerializing").write(sudokuBoard));
  }

  @Test
  public void test_write_after_solving() throws Throwable {
    SudokuBoard board2 = new SudokuBoard(new BacktrackingSudokuSolver());
    board2.solveGame();
    assertTrue(sudokuBoardDaoFactory.getFileDao("testSerializing").write(board2));
  }

  @Test
  public void test_read_positive() throws Throwable {
    FileDao<SudokuBoard> fileDao = (FileDao<SudokuBoard>) sudokuBoardDaoFactory.getFileDao("testSerializing");
    fileDao.write(sudokuBoard);
    sudokuBoardCheck = fileDao.read();
    assertEquals(sudokuBoard, sudokuBoardCheck);
  }

  @Test
  public void test_read_negative() {
    assertThrows(RequiredFileMissingException.class, () -> sudokuBoardDaoFactory.getFileDao("wrongFileName").read());
  }
}
