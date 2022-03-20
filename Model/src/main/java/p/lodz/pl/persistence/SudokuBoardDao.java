package p.lodz.pl.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import p.lodz.pl.model.IDao;
import p.lodz.pl.model.SudokuBoard;
import p.lodz.pl.model.SudokuBoardPrototypeFactory;
import p.lodz.pl.persistence.exceptions.ReadFromDatabaseException;
import p.lodz.pl.persistence.exceptions.SaveToDatabaseException;

public class SudokuBoardDao implements AutoCloseable, IDao<SudokuBoard> {
  SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
  Transaction transaction;
  String boardTitle;

  public SudokuBoardDao() {
  }

  //  private static class SudokuBoardDaoHolder {
  //    private static final SudokuBoardDao INSTANCE = new SudokuBoardDao();
  //  }
  //
  //  public static SudokuBoardDao getInstance() {
  //    return SudokuBoardDaoHolder.INSTANCE;
  //  }

  public boolean write(SudokuBoard sudokuBoard) throws SaveToDatabaseException {
    boolean successful = true;
    try (Session session = sessionFactory.openSession()) {
      try {
        transaction = session.beginTransaction();
        session.save(sudokuBoard);
        transaction.commit();
      } catch (HibernateException e) {
        transaction.rollback();
        throw new HibernateException(e);
      }
    } catch (HibernateException e) {
      throw new SaveToDatabaseException(e);
    }
    return successful;
  }

  public SudokuBoard read() throws ReadFromDatabaseException {
    SudokuBoard sudokuFields;
    SudokuBoard sudokuBoard = SudokuBoardPrototypeFactory.create();
    try (Session session = sessionFactory.openSession()) {
      try {
        transaction = session.beginTransaction();
        sudokuFields = (SudokuBoard) session
                .createQuery("select s from sudokuboard s where boardTitle = '" + boardTitle + "'")
                .getResultList().get(0);
        sudokuBoard.absorbSudokuBoard(sudokuFields);
        transaction.commit();
      } catch (HibernateException e) {
        transaction.rollback();
        throw new HibernateException(e);
      }
    } catch (HibernateException | IndexOutOfBoundsException e) {
      throw new ReadFromDatabaseException(e);
    }
    return sudokuBoard;
  }

  public void setBoardTitle(String title) {
    boardTitle = title;
  }

  public TransactionStatus getTransactionStatus() {
    return transaction.getStatus();
  }

  @Override
  public void close() {
    sessionFactory.close();
  }
}
