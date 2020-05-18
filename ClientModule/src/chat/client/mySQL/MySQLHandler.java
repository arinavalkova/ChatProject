package chat.client.mySQL;

import chat.client.ClientConsts;
import chat.client.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class MySQLHandler extends MySQLConfigs
{
    private static Connection mySQLConnection;

    private static Connection getMySQLConnection()
    {
        String connectionString = CONNECTION_URL_FIRST_PART + bdName + CONNECTION_URL_SECOND_PART;

        try
        {
            Class.forName(DRIVER).getDeclaredConstructor().newInstance();
            mySQLConnection = DriverManager.getConnection(connectionString, bdUser, bdPass);
        } catch (SQLException | ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return mySQLConnection;
    }

    public static void signUpUsers(User user)
    {
        String insert = INSERT_FIRST_PART + ClientConsts.USERS_TABLE + OPENNNING_BRACKET + ClientConsts.USERS_USERNAME + COMMA + ClientConsts.USERS_PASSWORD + CLOSING_BRACKET + INSERT_LAST_PART;

        try
        {
            PreparedStatement preparedStatement = getMySQLConnection().prepareStatement(insert);

            preparedStatement.setString(FIRST_PARAMETER, user.getUserName());
            preparedStatement.setString(SECOND_PARAMETER, user.getPassword());

            preparedStatement.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ResultSet getUser(User user)
    {
        ResultSet resultSet = null;

        String select = SELECT_FROM + ClientConsts.USERS_TABLE + WHERE + ClientConsts.USERS_USERNAME + QUESTION_AND + ClientConsts.USERS_PASSWORD + QUESTION;

        try
        {
            PreparedStatement preparedStatement = getMySQLConnection().prepareStatement(select);
            preparedStatement.setString(FIRST_PARAMETER, user.getUserName());
            preparedStatement.setString(SECOND_PARAMETER, user.getPassword());

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static boolean checkLoginBusy(User user)
    {
        ResultSet resultSet = null;

        String select = SELECT_FROM + ClientConsts.USERS_TABLE + WHERE + ClientConsts.USERS_USERNAME + QUESTION;

        try
        {
            PreparedStatement preparedStatement = getMySQLConnection().prepareStatement(select);
            preparedStatement.setString(FIRST_PARAMETER, user.getUserName());

            resultSet = preparedStatement.executeQuery();

            resultSet.last();
            return resultSet.getRow() != 0;

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}
