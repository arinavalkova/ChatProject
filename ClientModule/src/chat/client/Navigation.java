package chat.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

class Navigation extends ClientConsts
{
    private static Scene scene;
    private static Stage stage;

    static Stage getStage()
    {
        return stage;
    }

    static void setScene(Scene newScene)
    {
        scene = newScene;
    }

    Navigation(Stage primaryStage, Parent root)
    {
        stage = primaryStage;
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    FXMLLoader load(String nameOfClass)
    {
        FXMLLoader loader = null;
        try
        {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(nameOfClass));

            loader.load();

            Parent root = loader.getRoot();

            try
            {
                scene.setRoot(root);

            } catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return loader;
    }
}
