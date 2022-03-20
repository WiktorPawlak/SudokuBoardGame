package p.lodz.pl.view.exceptions;

public class CharacterTypeException extends LocalizedException {
  private final String character;

  public CharacterTypeException(String character) {
    super(character);
    this.character = character;
  }

  @Override
  public String getLocalizedMessage() {
    return quote(character) + ' ' + attemptToTranslate("notACharacter");
  }
}
