package chat.network.serializable.classes;

import chat.network.NetworkConsts;

import java.io.Serializable;
import java.util.ArrayList;

public class Users extends NetworkConsts implements Serializable
{
    private int userCount;
    private ArrayList<User> users;

    public Users()
    {
        this.userCount = 0;
        users = new ArrayList<>();
    }

    public void addNewUser(String ip, int port, String userName)
    {
        users.add(new User(ip, port, userName));
        userCount++;
    }

    public String getUserName(String ip, int port)
    {
        String answer = null;
        for (User user : users)
        {
            if (user.getIp().equals(ip) && user.getPort() == port)
            {
                answer = user.getUserName();
                break;
            }
        }
        return answer;
    }

    public void changeStatus(String ip, int port, String userName)
    {
        for (User user : users)
        {
            if (user.getIp().equals(ip) && user.getPort() == port && user.getUserName().equals(userName))
            {
                user.setStatus(user.getStatus().equals(ONLINE) ? OFFLINE : ONLINE);
                break;
            }
        }
    }

    public String getStatus(String ip, int port, String userName)
    {
        String answer = null;
        for (User user : users)
        {
            if (user.getIp().equals(ip) && user.getPort() == port && user.getUserName().equals(userName))
            {
                answer = user.getStatus();
                break;
            }
        }
        return answer;
    }

    public ArrayList<String> getOnlineUsers()
    {
        ArrayList<String> arrayList = new ArrayList<>();
        for (User user : users)
            if (user.getStatus().equals(ONLINE))
                arrayList.add(user.getUserName());

        return arrayList;
    }
}
