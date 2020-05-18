package chat.network.workerInterfaces;

public interface WorkerOnlineUsersCountInterface
{
    void createOnlineUsersFile();
    int getCountOfOnlineUsers();
    void incCountOfOnlineUsers();
    void decCountOfOnlineUsers();
}
