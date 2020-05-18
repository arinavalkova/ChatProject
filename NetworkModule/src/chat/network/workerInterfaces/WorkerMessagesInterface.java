package chat.network.workerInterfaces;

import java.util.ArrayList;

public interface WorkerMessagesInterface
{
    void createFileForMessages();
    void addMessage(String msg);
    ArrayList<String> getFiveLastMessages();
}