package p.lodz.pl.model;

import org.junit.jupiter.api.Test;
import p.lodz.pl.model.exceptions.UninitializedSudokuFieldException;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {
  @Test
  public void test_setter_positive() {
    SudokuField field = new SudokuField();
    field.setFieldValue(3);
    assertEquals(3, field.getFieldValue());
  }

  @Test
  public void test_setter_negative() {
    SudokuField field = new SudokuField();
    field.setFieldValue(3);
    field.setFieldValue(-3); // it should probably throw something, but wrong index, under/over flow don't
    // fit and i can't be bothered to write my own
    field.setFieldValue(131);
    assertEquals(3, field.getFieldValue());
  }

  @Test
  public void test_equals_positive() {
    SudokuField field = new SudokuField();
    field.setFieldValue(6);
    SudokuField field2 = new SudokuField();
    field2.setFieldValue(6);
    assertEquals(field, field2);
    assertEquals(field, field);
  }

  @Test
  public void test_equals_negative() {
    SudokuField field = new SudokuField();
    field.setFieldValue(5);
    SudokuField field2 = new SudokuField();
    field2.setFieldValue(6);
    assertNotEquals(field, field2);
  }

  @Test
  public void test_sudoku_field_not_being_a_cat() {
    Object test = new SudokuField();
    assertNotEquals(test, "kitty");
  }

  @Test
  public void test_hashCode_positive() {
    SudokuField field = new SudokuField();
    field.setFieldValue(6);
    SudokuField field2 = new SudokuField();
    field2.setFieldValue(6);
    assertEquals(field.hashCode(), field2.hashCode());
  }

  @Test
  public void test_hashCode_negative() {
    SudokuField field = new SudokuField();
    field.setFieldValue(5);
    SudokuField field2 = new SudokuField();
    field2.setFieldValue(6);
    assertNotEquals(field.hashCode(), field2.hashCode());
  }

  @Test
  public void test_comparable_positive() {
    SudokuField greaterField = new SudokuField();
    greaterField.setFieldValue(6);
    SudokuField lesserField = new SudokuField();
    lesserField.setFieldValue(2);
    SudokuField lesserField2 = new SudokuField();
    lesserField2.setFieldValue(2);

    assertEquals(greaterField.compareTo(lesserField), 1);
    assertEquals(lesserField.compareTo(greaterField), -1);
    assertEquals(lesserField.compareTo(lesserField2), 0);
  }

  @Test
  public void test_comparable_negative() {
    SudokuField field = new SudokuField();
    field.setFieldValue(6);

    assertThrows(UninitializedSudokuFieldException.class, () -> field.compareTo(null));
  }
}
