//    sudoku thing; projekt na zajęcia z PROKOM 2021-2022
//    Copyright (C) 2021  Antoni Jończyk, Wiktor Pawlak
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package p.lodz.pl.model;

import java.io.*;
import p.lodz.pl.model.exceptions.DeserializationException;
import p.lodz.pl.model.exceptions.RequiredFileMissingException;
import p.lodz.pl.model.exceptions.SerializationException;

public class FileDao<T extends Serializable> implements IDao<T> {

  String fileName;

  public FileDao(final String fileName) {
    this.fileName = fileName;
  }

  @Override
  public T read() throws RequiredFileMissingException, DeserializationException {
    T object;

    try (FileInputStream fileInputStream = new FileInputStream(fileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
      object = (T) objectInputStream.readObject();
    } catch (FileNotFoundException e) {
      throw new RequiredFileMissingException();
    } catch (IOException | ClassNotFoundException f) {
      throw new DeserializationException();
    }

    return object;
  }

  @Override
  public boolean write(T obj) throws SerializationException {

    try {
      SudokuBoard sudokuBoard = (SudokuBoard) obj;
      SudokuBoard newSudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
      newSudokuBoard.setBoard(sudokuBoard.getBoard());
      newSudokuBoard.copyEditability(sudokuBoard);
      obj = (T) newSudokuBoard;
    } catch (Exception e) {
      throw new SerializationException();
    }

    try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
      objectOutputStream.writeObject(obj);
    } catch (IOException e) {
      throw new SerializationException();
    }
    return true;
  }
}
