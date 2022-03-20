package p.lodz.pl.model.exceptions;

import p.lodz.pl.model.ModelLanguageBundle;

public class UninitializedSudokuFieldException extends RuntimeException {
  @Override
  public String getLocalizedMessage() {
    return ModelLanguageBundle.getInstance().get("uninitializedSudokuField");
  }
}
