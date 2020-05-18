package chat.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection extends NetworkConsts
{
    private final Object sendMutex = new Object();
    private final Object discMutex = new Object();

    private  Socket socket;
    private final Thread thread;
    private final TCPListener tcpListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public InetAddress getIp()
    {
        return socket.getInetAddress();
    }

    public int getPort()
    {
        return socket.getPort();
    }

    public TCPConnection(TCPListener tcpListener, String ip, int port) throws IOException
    {
        this(tcpListener, new Socket(ip, port));
    }

    public TCPConnection(TCPListener tcpListener, Socket socket) throws IOException
    {
        this.tcpListener = tcpListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName(UTF_8)));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName(UTF_8)));

        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    while(!thread.isInterrupted())
                    {
                        String msg = in.readLine();
                        if(isSettings(msg))
                        {
                            tcpListener.settingsRecieve(TCPConnection.this, msg);
                            tcpListener.readyConnection(TCPConnection.this);
                        }
                        else
                            tcpListener.stringReceieve(TCPConnection.this, msg);
                    }
                }
                catch (IOException e)
                {
                    tcpListener.exception(TCPConnection.this, e);
                }
                finally
                {
                    tcpListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        thread.start();
    }

    private boolean isSettings(String msg)
    {
        if(msg == null)
            return false;
        String[] arrOfStr = msg.split(SPACE);
        System.out.println(arrOfStr[0]);
        return arrOfStr[0].equals(SET);
    }

    public void sendMessage(String msg)
    {
        synchronized (sendMutex)
        {
            try
            {
                out.write(msg + NEW_LINE_WITH_R);
                out.flush();
            } catch (IOException e)
            {
                tcpListener.exception(TCPConnection.this, e);
                disconnect();
            }
        }
    }

    public void disconnect()
    {
        synchronized (discMutex)
        {
            thread.interrupt();
            try
            {
                socket.close();
            } catch (IOException e)
            {
                tcpListener.exception(TCPConnection.this, e);
            }
        }
    }

    @Override
    public String toString()
    {
        return CONNECTION + socket.getInetAddress() + SPACE + socket.getPort();
    }

    public void sendSettings(String msg)
    {
        try
        {
            out.write(msg);
            out.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
