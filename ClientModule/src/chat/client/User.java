package chat.client;

public class User
{
    private String userName;
    private String password;
    private String host;
    private int port;

    User(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    User(User user, String host, int port)
    {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.host = host;
        this.port = port;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }


    String getHost()
    {
        return host;
    }

    int getPort()
    {
        return port;
    }
}
