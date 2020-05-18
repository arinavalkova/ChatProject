package chat.server;

import chat.network.NetworkConsts;
import chat.network.TCPConnection;
import chat.network.TCPListener;
import chat.network.WorkerSettings;
import chat.network.workerInterfaces.WorkerMessagesInterface;
import chat.network.workerInterfaces.WorkerOnlineUsersCountInterface;
import chat.network.workerInterfaces.WorkerUsersInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;

public class ChatServer extends ServerConsts implements TCPListener
{
    private final Object readyConnectMutex = new Object();
    private final Object stringRecieveMutex = new Object();
    private final Object onDisconnectMutex = new Object();
    private final Object settingsRecieveMutex = new Object();

    public static void main(String[] args)
    {
        System.out.println(SERVER_IS_STARTING);
        new ChatServer();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer()
    {
        WorkerMessagesInterface workerMessagesInterface = null;
        WorkerOnlineUsersCountInterface workerOnlineUsersCountInterface = null;
        WorkerUsersInterface workerUsersInterface = null;

        try
        {
            workerMessagesInterface = (WorkerMessagesInterface)Class.forName(WorkerSettings.getMessagesNameOfClass()).newInstance();
            workerOnlineUsersCountInterface = (WorkerOnlineUsersCountInterface)Class.forName(WorkerSettings.getOnlineUsersCountNmaeOfClass()).newInstance();
            workerUsersInterface =(WorkerUsersInterface)Class.forName(WorkerSettings.getUsersNameOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        workerMessagesInterface.createFileForMessages();
        workerOnlineUsersCountInterface.createOnlineUsersFile();
        workerUsersInterface.createFileForUsers();

        String port = null;
        Properties prop = new Properties();
        try
        {
            FileInputStream fileInputStream = new FileInputStream(NetworkConsts.SERVER_CONFIG_PATH);
            prop.load(fileInputStream);
            port = prop.getProperty(NetworkConsts.PORT);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try(ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port)))
        {
            while(true)
            {
                new TCPConnection(this, serverSocket.accept());
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readyConnection(TCPConnection tcpConnection)
    {
        connections.add(tcpConnection);

        WorkerOnlineUsersCountInterface workerOnlineUsersCountInterface = null;
        try
        {
            workerOnlineUsersCountInterface = (WorkerOnlineUsersCountInterface)Class.forName(WorkerSettings.getOnlineUsersCountNmaeOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        WorkerUsersInterface workerUsersInterface = null;
        try
        {
            workerUsersInterface =(WorkerUsersInterface)Class.forName(WorkerSettings.getUsersNameOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        synchronized (readyConnectMutex)
        {
            workerOnlineUsersCountInterface.incCountOfOnlineUsers();

            String user = workerUsersInterface.getUserName(String.valueOf(tcpConnection.getIp()), tcpConnection.getPort());

            sendMsgToAll(CLIENT_CONNECTED + user);
        }
    }

    @Override
    public void stringReceieve(TCPConnection tcpConnection, String s)
    {
        synchronized (stringRecieveMutex)
        {
            sendMsgToAll(s);
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection)
    {
        connections.remove(tcpConnection);

        WorkerOnlineUsersCountInterface workerOnlineUsersCountInterface = null;
        try
        {
            workerOnlineUsersCountInterface = (WorkerOnlineUsersCountInterface)Class.forName(WorkerSettings.getOnlineUsersCountNmaeOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        WorkerUsersInterface workerUsersInterface = null;
        try
        {
            workerUsersInterface =(WorkerUsersInterface)Class.forName(WorkerSettings.getUsersNameOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        synchronized (onDisconnectMutex)
        {
            workerOnlineUsersCountInterface.decCountOfOnlineUsers();

            String user = workerUsersInterface.getUserName(String.valueOf(tcpConnection.getIp()), tcpConnection.getPort());
            workerUsersInterface.changeStatus(String.valueOf(tcpConnection.getIp()), tcpConnection.getPort(), user);

            sendMsgToAll(CLIENT_DISCONNECTED + user);
        }
    }

    @Override
    public void exception(TCPConnection tcpConnection, Exception e)
    {
        System.out.println(CONNECTION_EXCEPTION + e);
    }

    @Override
    public void settingsRecieve(TCPConnection tcpConnection, String msg)
    {
        String[] arrOfStr = msg.split(SPACE);

        WorkerUsersInterface workerUsersInterface = null;
        try
        {
            workerUsersInterface =(WorkerUsersInterface)Class.forName(WorkerSettings.getUsersNameOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        synchronized (settingsRecieveMutex)
        {
            workerUsersInterface.addNewUser(String.valueOf(tcpConnection.getIp()),tcpConnection.getPort(),arrOfStr[3]);
        }
    }

    private void sendMsgToAll(String msg)
    {
        if(msg != null)
        {
            System.out.println(msg);

            WorkerMessagesInterface workerMessagesInterface = null;
            try
            {
                workerMessagesInterface = (WorkerMessagesInterface)Class.forName(WorkerSettings.getMessagesNameOfClass()).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            workerMessagesInterface.addMessage(msg);
        }
        int connectionSize = connections.size();

        for (int i = 0; i < connectionSize; i++)
            connections.get(i).sendMessage(msg);
    }

}
