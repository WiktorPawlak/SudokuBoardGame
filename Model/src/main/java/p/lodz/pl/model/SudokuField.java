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
import java.util.Objects;
import javax.persistence.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;
import p.lodz.pl.model.exceptions.UninitializedSudokuFieldException;

@Entity(name = "sudokufield")
@Table(name = "SUDOKUFIELD")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SudokuField implements IObservable, Serializable, Cloneable, Comparable<SudokuField> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "fieldId")
  private Integer id;
  private int value;
  private boolean editable = false; // just a marker for user

  @Transient
  @HashCodeExclude
  @ToStringExclude
  private final ArrayList<IObserver> observers;

  protected SudokuField() {
    observers = new ArrayList<>();
  }

  public int getFieldValue() {
    return value;
  }

  public void setFieldValue(int value) {
    if (value >= 0 && value < 10) {
      this.value = value;
    }
    notifyObservers();
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
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    SudokuField other = (SudokuField) obj;
    return getFieldValue() == other.getFieldValue();
  }

  @Override
  public SudokuField clone() {
    try {
      return (SudokuField) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public int compareTo(SudokuField field) {
    if (Objects.nonNull(field)) {
      return Integer.compare(this.getFieldValue(), field.getFieldValue());
    } else {
      throw new UninitializedSudokuFieldException();
    }
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public Integer getId() {
    return id;
  }
}
