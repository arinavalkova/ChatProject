package chat.network.serializable.workers;

import chat.network.NetworkConsts;
import chat.network.serializable.classes.Users;
import chat.network.workerInterfaces.WorkerUsersInterface;

import java.util.ArrayList;

public class SerializableWorkerUsers implements WorkerUsersInterface
{
    @Override
    public void createFileForUsers()
    {
        Users users = new Users();
        SerializableTools.writeObject(users, NetworkConsts.USERS_PATH);
    }

    @Override
    public String getUserName(String ip, int port)
    {
        Users users = (Users) SerializableTools.getObject(NetworkConsts.USERS_PATH);
        return users.getUserName(ip, port);
    }

    @Override
    public void addNewUser(String ip, int port, String userName)
    {
        Users users = (Users) SerializableTools.getObject(NetworkConsts.USERS_PATH);
        users.addNewUser(ip, port, userName);
        SerializableTools.writeObject(users, NetworkConsts.USERS_PATH);
    }

    @Override
    public void changeStatus(String ip, int port, String userName)
    {
        Users users = (Users) SerializableTools.getObject(NetworkConsts.USERS_PATH);
        users.changeStatus(ip, port, userName);
        SerializableTools.writeObject(users, NetworkConsts.USERS_PATH);
    }

    @Override
    public ArrayList<String> getOnlineUsers()
    {
        Users users = (Users) SerializableTools.getObject(NetworkConsts.USERS_PATH);
        return users.getOnlineUsers();
    }

    @Override
    public String getStatus(String ip, int port, String userName)
    {
        Users users = (Users) SerializableTools.getObject(NetworkConsts.USERS_PATH);
        return users.getStatus(ip, port, userName);
    }
}
