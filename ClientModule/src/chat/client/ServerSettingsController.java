package chat.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ServerSettingsController extends ClientConsts
{
    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Button goToChatButton;

    @FXML
    private Label labelForFails;

    @FXML
    private Button backButton;

    private static User allSettingsUser;

    static User getUser()
    {
        return allSettingsUser;
    }

    @FXML
    void initialize()
    {
        backButton.setOnAction(event -> {
            Main.getNavigation().load(MAIN_WINDOW_PATH);
        });

        goToChatButton.setOnAction(event -> {
            if(!portField.getText().equals(EMPTY_LINE) && !hostField.getText().equals(EMPTY_LINE))
            {
                allSettingsUser = new User(MainController.getUser(), hostField.getText(), Integer.parseInt(portField.getText()));
                System.out.println(allSettingsUser.getUserName() + SPACE + allSettingsUser.getPassword() + SPACE + allSettingsUser.getHost() + SPACE + allSettingsUser.getPort());
                FXMLLoader loader = Main.getNavigation().load(CHAT_WINDOW_PATH);

                ChatController controller =
                        loader.<ChatController>getController();
                controller.initData(allSettingsUser.getUserName());

            }
            else
                labelForFails.setText(HOST_OR_PORT_IS_EMPTY);

        });
    }

}
