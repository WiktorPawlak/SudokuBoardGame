package p.lodz.pl.persistence;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.Test;
import p.lodz.pl.model.SudokuBoard;
import p.lodz.pl.model.SudokuBoardPrototypeFactory;
import p.lodz.pl.persistence.exceptions.ReadFromDatabaseException;
import p.lodz.pl.persistence.exceptions.SaveToDatabaseException;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuBoardDaoTest {
  String title = "test_board_title";

  @Test
  void test_write_to_db() throws SaveToDatabaseException {
    SudokuBoard sb = SudokuBoardPrototypeFactory.create();
    assertTrue(sb.solveGame());
    sb.setBoardTitle(title);
    SudokuBoardDao dao = new SudokuBoardDao();

    assertTrue(dao.write(sb));
    assertEquals(dao.getTransactionStatus(), TransactionStatus.COMMITTED);
  }

  @Test
  void test_read_from_db_positive() throws ReadFromDatabaseException, SaveToDatabaseException {
    SudokuBoard sudokuBoard = SudokuBoardPrototypeFactory.create();
    SudokuBoardDao dao = new SudokuBoardDao();

    sudokuBoard.solveGame();
    sudokuBoard.setBoardTitle(title);
    dao.write(sudokuBoard);
    dao.setBoardTitle(title);

    sudokuBoard.absorbSudokuBoard(dao.read());

    assertTrue(sudokuBoard.checkBoard());
    assertEquals(dao.getTransactionStatus(), TransactionStatus.COMMITTED);
    assertEquals(sudokuBoard, dao.read());
    assertNotSame(sudokuBoard, dao.read());
  }

  @Test
  void test_read_from_db_negative() {
    SudokuBoardDao dao = new SudokuBoardDao();
    dao.setBoardTitle("no_available_board_title");
    assertThrows(ReadFromDatabaseException.class, dao::read);
  }
}
