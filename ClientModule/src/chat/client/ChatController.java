package chat.client;

import chat.network.TCPConnection;
import chat.network.TCPListener;
import chat.network.WorkerSettings;
import chat.network.workerInterfaces.WorkerMessagesInterface;
import chat.network.workerInterfaces.WorkerOnlineUsersCountInterface;
import chat.network.workerInterfaces.WorkerUsersInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class ChatController extends ClientConsts implements TCPListener
{
    private final Object mutex = new Object();

    private static Thread onlineUsersThread;
    private static TCPConnection tcpConnection;

    static TCPConnection getTcpConnection()
    {
        return tcpConnection;
    }

    static Thread getOnlineUsersThread()
    {
        return onlineUsersThread;
    }

    private ObservableList<String> onlineUsers = FXCollections.observableArrayList();

    @FXML
    private Label nameLabel;

    @FXML
    private Button backToMenuButton;

    @FXML
    private Label countOfOnlineUsers;

    @FXML
    private ListView onlineUsersListView;

    @FXML
    private TextArea fieldForChat;

    @FXML
    private TextField areaForTypingMessages;

    @FXML
    private Button sendMsgButton;

    private void addFiveLastMessagesFromServerChat()
    {
        WorkerMessagesInterface workerMessagesInterface = null;
        try
        {
            workerMessagesInterface = (WorkerMessagesInterface) Class.forName(WorkerSettings.getMessagesNameOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        ArrayList<String> fiveMessages = workerMessagesInterface.getFiveLastMessages();

        for (int i = 0; i < fiveMessages.size(); i++)
            fieldForChat.appendText(fiveMessages.get(i) + NEW_LINE);
    }

    void initData(String userName)
    {
        onlineUsersListView.setStyle(STYLE_FOR_LIST);
        addFiveLastMessagesFromServerChat();
        nameLabel.setText(userName);
        onlineUsersListView.setItems(onlineUsers);
        try
        {
            onlineUsersThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (!onlineUsersThread.isInterrupted())
                    {
                        try
                        {
                            WorkerOnlineUsersCountInterface workerOnlineUsersCountInterface = null;
                            try
                            {
                                workerOnlineUsersCountInterface = (WorkerOnlineUsersCountInterface) Class.forName(WorkerSettings.getOnlineUsersCountNmaeOfClass()).newInstance();
                            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
                            {
                                e.printStackTrace();
                            }

                            String countOfOnline = String.valueOf(workerOnlineUsersCountInterface.getCountOfOnlineUsers());

                            Platform.runLater((() -> countOfOnlineUsers.setText(countOfOnline)));

                            checkOnlineUsers();
                            System.out.println(countOfOnline);

                            Thread.sleep(ONE_SEC);
                        } catch (InterruptedException ex)
                        {
                            break;
                        }
                    }
                }
            });
            onlineUsersThread.start();

            tcpConnection = new TCPConnection(this, ServerSettingsController.getUser().getHost(), ServerSettingsController.getUser().getPort());

            tcpConnection.sendSettings(SET + ServerSettingsController.getUser().getHost() + SPACE + ServerSettingsController.getUser().getPort() + SPACE + ServerSettingsController.getUser().getUserName() + NEW_LINE_WITH_R);

        } catch (IOException e)
        {
            printMsg(CONNECTION_EXCEPTION + e);
        }
    }

    private void checkOnlineUsers()
    {
        WorkerUsersInterface workerUsersInterface = null;
        try
        {
            workerUsersInterface = (WorkerUsersInterface) Class.forName(WorkerSettings.getUsersNameOfClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        ArrayList<String> onlineUsersList = workerUsersInterface.getOnlineUsers();

        for (int i = 0; i < onlineUsers.size(); i++)
        {
            String currentUser = onlineUsers.get(i);

            if (!onlineUsersList.contains(currentUser))
                Platform.runLater((() -> onlineUsers.remove(currentUser)));
        }

        for (int i = 0; i < onlineUsersList.size(); i++)
        {
            String currentUser = onlineUsersList.get(i);

            if (!onlineUsers.contains(currentUser))
                Platform.runLater((() -> onlineUsers.add(currentUser)));
        }
    }

    @FXML
    void initialize()
    {
        sendMsgButton.setOnAction(event ->
        {
            String msg = areaForTypingMessages.getText();
            if (msg.equals(EMPTY_LINE))
                return;
            areaForTypingMessages.setText(null);
            tcpConnection.sendMessage(ServerSettingsController.getUser().getUserName() + COLON + msg);
        });

        backToMenuButton.setOnAction(event ->
        {
            getTcpConnection().disconnect();
            getOnlineUsersThread().interrupt();
            Main.getNavigation().load(SERVER_SETTINGS_WINDOW_PATH);
        });
    }

    @Override
    public void readyConnection(TCPConnection tcpConnection)
    {
        printMsg(ServerSettingsController.getUser().getUserName() + CONNECTION_IS_READY);
    }

    @Override
    public void stringReceieve(TCPConnection tcpConnection, String s)
    {
        if (s == null)
            return;
        if (!s.equals(NULL))
            printMsg(s);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection)
    {
        System.out.println(ServerSettingsController.getUser().getUserName() + CONNECTION_IS_CLOSE);
    }

    @Override
    public void exception(TCPConnection tcpConnection, Exception e)
    {
        printMsg(CONNECTION_EXCEPTION + e);
    }

    @Override
    public void settingsRecieve(TCPConnection tcpConnection, String msg)
    {
        String[] arrOfStr = msg.split(SPACE);
        System.out.println(arrOfStr[0]);
        System.out.println(arrOfStr[1]);
    }


    private void printMsg(String msg)
    {
        synchronized (mutex)
        {
            fieldForChat.appendText(msg + NEW_LINE);
        }
    }
}
