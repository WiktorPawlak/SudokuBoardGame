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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

//@Entity(name = "sudokusection")
//@Table(name = "SUDOKUSECTION")
public class SudokuSection implements IObserver, IObservable, Serializable, Cloneable {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @Column(name = "sectionId")
  //  private Integer id;
  private boolean valid = false;

  //  @Transient
  //  @HashCodeExclude
  private ArrayList<IObserver> observers;
  //
  //  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  //  @JoinColumn(name = "sectionId")
  protected List<SudokuField> fields = Arrays.asList(new SudokuField[9]);

  protected SudokuSection() {
  }

  SudokuSection(SudokuBoard board, //
                ToIntFunction<Integer> getx, //
                ToIntFunction<Integer> gety) {
    observers = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      board.giveSudokuField(this, i, getx.applyAsInt(i), gety.applyAsInt(i));
    }
    registerObserver(board);
    update();
  }

  public void receiveSudokuField(int i, SudokuField field) {
    fields.set(i, field);
    field.registerObserver(this);
  }

  public boolean verify() {
    return valid;
  }

  public boolean contains(int val) {
    return fields.stream().anyMatch(e -> e.getFieldValue() == val);
  }

  @Override
  public void update() {
    boolean old = valid;
    valid = checkValidity();
    if (old != valid) {
      notifyObservers();
    }
  }

  private boolean checkValidity() {
    for (int i = 1; i <= 9; i++) {
      if (!contains(i)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    StringBuilder ans = new StringBuilder();
    if (valid) {
      ans.append("valid");
    } else {
      ans.append("invalid");
    }
    ans.append(", contains [");
    for (int i = 0; i < 8; i++) {
      ans.append(fields.get(i)).append(", ");
    }
    ans.append(fields.get(8));
    ans.append("]");
    return ans.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (getClass() != obj.getClass()) {
      return false;
    }
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public void registerObserver(IObserver observer) {
    observers.add(observer);
  }

  @Override
  public void notifyObservers() {
    for (IObserver o : observers) {
      o.update();
    }
  }

  @Override
  public SudokuSection clone() throws CloneNotSupportedException {
    ByteDao<SudokuSection> byteDao = new ByteDao<>();
    SudokuSection clone = (SudokuSection) super.clone();
    try {
      byteDao.write(this);
      clone = byteDao.read();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return clone;
  }

  //  public Integer getId() {
  //    return id;
  //  }
}
