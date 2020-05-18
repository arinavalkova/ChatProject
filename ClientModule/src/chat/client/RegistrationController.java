package chat.client;

import chat.client.mySQL.MySQLHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController extends ClientConsts
{
    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registrateButton;

    @FXML
    private Label labelForFails;

    @FXML
    private Button backButton;

    @FXML
    void initialize()
    {
        backButton.setOnAction(event -> {
            Main.getNavigation().load(MAIN_WINDOW_PATH);
        });

        registrateButton.setOnAction(event -> {
            User user = new User(loginField.getText().trim(), passwordField.getText().trim());

            if(!user.getUserName().equals(SPACE) && !user.getPassword().equals(SPACE))
            {
                if(!MySQLHandler.checkLoginBusy(user))
                {
                    MySQLHandler.signUpUsers(user);
                    labelForFails.setText(SUCCESSFULLY_SIGN_UP);
                    Main.getNavigation().load(MAIN_WINDOW_PATH);
                }
                else
                    labelForFails.setText(LOGIN_EXISTS);
            }
            else labelForFails.setText(LOGIN_OR_PASSWORD_ARE_EMPTY);
        });
    }
}
