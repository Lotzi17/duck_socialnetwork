package lab2_map.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import lab2_map.controller.DSNController;
import lab2_map.service.ServiceFactory;
import lab2_map.service.SocialNetworkService;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/GUI/Duck_socialnetwork_GUI.fxml")
        );

        Scene scene = new Scene(loader.load());

        // Build service from factory
        SocialNetworkService service = ServiceFactory.createSocialNetworkService();

        // Inject controller
        DSNController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("Duck Social Network");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
