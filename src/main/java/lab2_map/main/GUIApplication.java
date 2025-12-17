package lab2_map.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab2_map.controller.DSNController;
import lab2_map.service.SocialNetworkService;

public class GUIApplication extends Application {

    public static SocialNetworkService globalService;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/GUI/Duck_socialnetwork_GUI.fxml"));

        Parent root = loader.load();

        DSNController controller = loader.getController();
        controller.setService(globalService);

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Duck GUI");
        stage.show();
    }
}
