package chat.client;

import chat.client.mySQL.MySQLHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainController extends ClientConsts
{
    @FXML
    private Button registrationButton;

    @FXML
    private TextField loginField;

    @FXML
    private Button signInButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label labelForFails;

    private static User user;

    static User getUser()
    {
        return user;
    }

    @FXML
    void initialize()
    {
        signInButton.setOnAction(event -> {
            String loginLine = loginField.getText().trim();
            String passwordLine = passwordField.getText().trim();

            if(!loginLine.equals(EMPTY_LINE) && !passwordLine.equals(EMPTY_LINE))
                loginingUser(loginLine, passwordLine);
            else labelForFails.setText(LOGIN_OR_PASSWORD_ARE_EMPTY);
        });

        registrationButton.setOnAction(event -> {
            Main.getNavigation().load(REGISTRATION_WINDOW_PATH);
        });

    }

    private void loginingUser(String loginLine, String passwordLine)
    {
        MySQLHandler mySQLHandler = new MySQLHandler();
        user = new User(loginLine, passwordLine);

        ResultSet resultSet = MySQLHandler.getUser(user);

        try
        {
            resultSet.last();
            if(resultSet.getRow() != 0)
                Main.getNavigation().load(SERVER_SETTINGS_WINDOW_PATH);
            else
                labelForFails.setText(BAD_PASSWORD_OR_LOGIN);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}

