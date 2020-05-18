package chat.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application
{
    private static Navigation navigation;

    static Navigation getNavigation()
    {
        return navigation;
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource(ClientConsts.MAIN_WINDOW_PATH));
        navigation = new Navigation(stage, root);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(ChatController.getTcpConnection() != null)
                {
                    ChatController.getTcpConnection().disconnect();
                    ChatController.getOnlineUsersThread().interrupt();
                }
                System.exit(0);
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
