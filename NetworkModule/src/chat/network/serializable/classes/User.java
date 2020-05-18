package chat.network.serializable.classes;

import chat.network.NetworkConsts;

import java.io.Serializable;

public class User extends NetworkConsts implements Serializable
{
    private String ip;
    private int port;
    private String userName;
    private String status;

    User(String ip, int port, String userName)
    {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.status = ONLINE;
    }


    String getIp()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }

    public String getUserName()
    {
        return userName;
    }

    String getStatus()
    {
        return status;
    }

    void setStatus(String status)
    {
        this.status = status;
    }
}
