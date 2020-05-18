package chat.network;

public interface TCPListener
{
    void readyConnection(TCPConnection tcpConnection);
    void stringReceieve(TCPConnection tcpConnection, String s);
    void onDisconnect(TCPConnection tcpConnection);
    void exception(TCPConnection tcpConnection, Exception e);
    void settingsRecieve(TCPConnection tcpConnection, String msg);
}
