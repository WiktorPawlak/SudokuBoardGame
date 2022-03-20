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
import p.lodz.pl.model.exceptions.SerializationException;

public class ByteDao<T extends Serializable> implements IDao<T> {

  private byte[] byteArray;

  @Override
  public T read() throws DeserializationException {
    T object;

    try (ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
      object = (T) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException f) {
      throw new DeserializationException();
    }

    return object;
  }

  @Override
  public boolean write(T obj) throws SerializationException {
    try (ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream)) {
      objectOutputStream.writeObject(obj);
      byteArray = byteOutputStream.toByteArray();
    } catch (IOException e) {
      throw new SerializationException();
    }
    return true;
  }

  public byte[] getByteArray() {
    byte[] newByteArray = new byte[byteArray.length];
    System.arraycopy(byteArray, 0, newByteArray, 0, byteArray.length);
    return newByteArray;
  }
}
