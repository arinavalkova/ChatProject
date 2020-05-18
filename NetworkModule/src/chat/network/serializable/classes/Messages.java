package chat.network.serializable.classes;

import chat.network.NetworkConsts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;

public class Messages implements Serializable
{
    private int messagesCount;
    private ArrayList<String> massage;

    public Messages()
    {
        messagesCount = 0;
        massage = new ArrayList<>();
    }

    public void addMessage(String msg)
    {
        massage.add(msg);
        messagesCount++;
    }

    public ArrayList<String> getFiveLastMessages()
    {
        String msgCount = null;
        Properties prop = new Properties();
        try
        {
            FileInputStream fileInputStream = new FileInputStream(NetworkConsts.SERVER_CONFIG_PATH);
            prop.load(fileInputStream);
            msgCount = prop.getProperty(NetworkConsts.MSG_COUNT);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        ArrayList<String> fiveMessages = new ArrayList<>();
        int allowedCoutOfMessages = 0;

        if(messagesCount >= Integer.parseInt(msgCount))
            allowedCoutOfMessages = Integer.parseInt(msgCount);
        else
            allowedCoutOfMessages = messagesCount;

        for(int i = messagesCount - allowedCoutOfMessages; i < messagesCount; i++)
            fiveMessages.add(massage.get(i));

        return fiveMessages;
    }
}
