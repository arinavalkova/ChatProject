package chat.network.serializable.workers;

import java.io.*;

class SerializableTools
{
    static void writeObject(Object object, String path)
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(object);

            objectOutputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static Object getObject(String path)
    {
        Object object = null;
        try
        {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            object = objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return object;
    }
}
