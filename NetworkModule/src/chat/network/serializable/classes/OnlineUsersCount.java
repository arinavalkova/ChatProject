package chat.network.serializable.classes;

import java.io.Serializable;

public class OnlineUsersCount implements Serializable
{
    private int onlineUsersCount;

    public OnlineUsersCount()
    {
        this.onlineUsersCount = 0;
    }

    public int getOnlineUsersCount()
    {
        return onlineUsersCount;
    }

    public void incOnlineUsersCount()
    {
        onlineUsersCount++;
    }

    public void decOnlineUsersCount()
    {
        onlineUsersCount--;
    }
}
