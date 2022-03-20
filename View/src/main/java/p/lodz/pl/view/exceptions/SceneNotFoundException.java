package p.lodz.pl.view.exceptions;

public class SceneNotFoundException extends InitializeException {
    final String name;

    public SceneNotFoundException(String s) {
        super(s);
        name = s;
    }

    @Override
    public String getLocalizedMessage() {
        return attemptToTranslate("sceneNotFound") + " " + quote(name);
    }
}
