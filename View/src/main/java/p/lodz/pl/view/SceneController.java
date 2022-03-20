package p.lodz.pl.view;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import p.lodz.pl.view.exceptions.NoAvailableTranslationException;
import p.lodz.pl.view.exceptions.NotaLabelException;
import p.lodz.pl.view.exceptions.SceneNotFoundException;

class SceneController<T extends ILanguageDependent> implements ILanguageDependent {

    Scene scene;
    FXMLLoader loader;
    T controller;
    String name;

    SceneController<T> initialize(String fxml, String name) throws SceneNotFoundException {
        this.name = name;
        loader = new FXMLLoader(SudokuBoardApplication.class.getResource(fxml));
        try {
            scene = new Scene(loader.load());
            controller = loader.getController();
            LanguageBundle.getInstance().addObserver(controller);
            LanguageBundle.getInstance().addObserver(this);
        } catch (IOException e) {
            throw new SceneNotFoundException(name);
        }
        return this;
    }

    SceneController<T> addStyle(String file) {
        String style = Objects.requireNonNull(getClass().getResource(file)).toExternalForm();
        scene.getStylesheets().add(style);
        return this;
    }

    @Override
    public void switchLanguage() {
        applyToEveryNode(scene.getRoot(), //
                node -> (node instanceof Labeled), //
                node -> {
                    try {
                        LanguageBundle.changeElementLanguage(name, node);
                    } catch (NotaLabelException | NoAvailableTranslationException e) {
                        e.viewLog(e.getLocalizedMessage());
                    }
                });
    }

    private void applyToEveryNode(Parent current, Predicate<Node> pred, Consumer<Node> function) {
        if (pred.test(current)) {
            function.accept(current);
        }
        for (Node child : current.getChildrenUnmodifiable()) {
            if (child instanceof Parent) {
                applyToEveryNode((Parent) child, pred, function);
            }
        }
    }
}
