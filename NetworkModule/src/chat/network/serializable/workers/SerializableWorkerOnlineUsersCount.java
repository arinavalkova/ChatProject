package chat.network.serializable.workers;

import chat.network.NetworkConsts;
import chat.network.serializable.classes.OnlineUsersCount;
import chat.network.workerInterfaces.WorkerOnlineUsersCountInterface;

public class SerializableWorkerOnlineUsersCount implements WorkerOnlineUsersCountInterface
{
    @Override
    public void createOnlineUsersFile()
    {
        OnlineUsersCount onlineUsersCount = new OnlineUsersCount();
        SerializableTools.writeObject(onlineUsersCount, NetworkConsts.ONLINE_USERS_COUNT_PATH);
    }

    @Override
    public int getCountOfOnlineUsers()
    {
        OnlineUsersCount onlineUsersCount = (OnlineUsersCount) SerializableTools.getObject(NetworkConsts.ONLINE_USERS_COUNT_PATH);

        return onlineUsersCount.getOnlineUsersCount();
    }

    @Override
    public void incCountOfOnlineUsers()
    {
        OnlineUsersCount onlineUsersCount = (OnlineUsersCount) SerializableTools.getObject(NetworkConsts.ONLINE_USERS_COUNT_PATH);
        onlineUsersCount.incOnlineUsersCount();

        SerializableTools.writeObject(onlineUsersCount, NetworkConsts.ONLINE_USERS_COUNT_PATH);
    }

    @Override
    public void decCountOfOnlineUsers()
    {
        OnlineUsersCount onlineUsersCount = (OnlineUsersCount) SerializableTools.getObject(NetworkConsts.ONLINE_USERS_COUNT_PATH);
        onlineUsersCount.decOnlineUsersCount();

        SerializableTools.writeObject(onlineUsersCount, NetworkConsts.ONLINE_USERS_COUNT_PATH);
    }
}
