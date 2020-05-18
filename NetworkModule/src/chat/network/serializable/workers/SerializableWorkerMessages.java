package chat.network.serializable.workers;

import chat.network.NetworkConsts;
import chat.network.serializable.classes.Messages;
import chat.network.workerInterfaces.WorkerMessagesInterface;

import java.util.ArrayList;

public class SerializableWorkerMessages implements WorkerMessagesInterface
{
    @Override
    public void createFileForMessages()
    {
        Messages messages = new Messages();
        SerializableTools.writeObject(messages, NetworkConsts.MESSAGES_PATH);
    }

    @Override
    public void addMessage(String msg)
    {
        Messages messages = (Messages)SerializableTools.getObject(NetworkConsts.MESSAGES_PATH);
        messages.addMessage(msg);
        SerializableTools.writeObject(messages, NetworkConsts.MESSAGES_PATH);
    }

    @Override
    public ArrayList<String> getFiveLastMessages()
    {
        Messages messages = (Messages)SerializableTools.getObject(NetworkConsts.MESSAGES_PATH);
        return messages.getFiveLastMessages();
    }
}
