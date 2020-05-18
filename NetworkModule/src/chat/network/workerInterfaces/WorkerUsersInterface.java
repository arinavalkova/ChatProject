package chat.network.workerInterfaces;

import java.util.ArrayList;

public interface WorkerUsersInterface
{
    void createFileForUsers();
    String getUserName(String ip, int port);
    void addNewUser(String ip, int port, String userName);
    void changeStatus(String ip, int port, String userName);
    String getStatus(String ip, int port, String userName);
    ArrayList<String> getOnlineUsers();
}
