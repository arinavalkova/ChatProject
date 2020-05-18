package chat.network.XMLWorkers;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import chat.network.workerInterfaces.WorkerOnlineUsersCountInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import static chat.network.NetworkConsts.*;

public class XMLWorkerOnlineUsersCount implements WorkerOnlineUsersCountInterface
{
    @Override
    public void createOnlineUsersFile()
    {
        Document document = null;
        try
        {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element onlineUsers = document.createElement(ONLINE_USERS);
            onlineUsers.setTextContent(ZERO_STRING);
            document.appendChild(onlineUsers);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult( new File(ONLINE_USERS_COUNT_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getCountOfOnlineUsers()
    {
        int answer = 0;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(ONLINE_USERS_COUNT_XML_NAME);
            doc.getDocumentElement().normalize();
            answer = Integer.parseInt(doc.getElementsByTagName(ONLINE_USERS).item(0).getTextContent());

        } catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    public void incCountOfOnlineUsers()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(ONLINE_USERS_COUNT_XML_NAME);
            doc.getDocumentElement().normalize();

            int lastOnlineUsersCount = Integer.parseInt(doc.getElementsByTagName(ONLINE_USERS).item(0).getTextContent()) + 1;
            doc.getElementsByTagName(ONLINE_USERS).item(0).setTextContent(String.valueOf(lastOnlineUsersCount));

            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(ONLINE_USERS_COUNT_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decCountOfOnlineUsers()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(ONLINE_USERS_COUNT_XML_NAME);
            doc.getDocumentElement().normalize();
            int lastOnlineUsersCount = Integer.parseInt(doc.getElementsByTagName(ONLINE_USERS).item(0).getTextContent());
            doc.getElementsByTagName(ONLINE_USERS).item(0).setTextContent(String.valueOf(lastOnlineUsersCount - 1));

            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(ONLINE_USERS_COUNT_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e)
        {
            e.printStackTrace();
        }
    }
}
