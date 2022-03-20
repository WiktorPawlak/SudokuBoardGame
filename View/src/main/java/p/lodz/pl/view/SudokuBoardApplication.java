package p.lodz.pl.view;

import javafx.application.Application;
import javafx.stage.Stage;
import p.lodz.pl.view.exceptions.InitializeException;

public class SudokuBoardApplication extends Application {
    @Override
    public void start(final Stage stage) {
        LanguageBundle.getInstance().initialize();
        try {
            StageController.getInstance().initialize(stage).switchToMenu();
        } catch (InitializeException e) {
            e.viewLog(e.getLocalizedMessage());
        }
    }

    public static void main(final String[] args) {
        System.getProperties().put("log4j2.configurationFile", "log4j.xml");
        //PropertyConfigurator.configure("log4j.xml");
        //DOMConfigurator.configure("log4j.xml");
        launch();
    }
}
