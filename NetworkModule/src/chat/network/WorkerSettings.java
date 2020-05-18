package chat.network;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class WorkerSettings extends NetworkConsts
{
    private static final Map<String, String> workerMessages = Map.of(
            SER, SERIALIZABLE_WORKER_MESSAGES,
            XML, XML_WORKER_MESSAGES
    );

    private static final Map<String, String> workerOnlineUsersCount = Map.of(
            SER, SERIALIZABLE_WORKER_ONLINE_USERS_COUNT,
            XML, XML_WORKER_ONLINE_USERS_COUNT
    );

    private static final Map<String, String> workerUsers = Map.of(
            SER, SERIALIZABLE_WORKER_USERS,
            XML, XML_WORKER_USERS
    );

    private static String getMsgSer()
    {
        String msgSer = null;
        Properties prop = new Properties();
        try
        {
            FileInputStream fileInputStream = new FileInputStream(SERVER_CONFIG_PATH);
            prop.load(fileInputStream);
            msgSer = prop.getProperty(MSG_SERIL);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return msgSer;
    }

    public static String getMessagesNameOfClass()
    {

        return workerMessages.get(getMsgSer());
    }

    public static String getOnlineUsersCountNmaeOfClass()
    {
        return workerOnlineUsersCount.get(getMsgSer());
    }

    public static String getUsersNameOfClass()
    {
        return workerUsers.get(getMsgSer());
    }
}
