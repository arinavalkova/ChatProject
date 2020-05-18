package chat.network.XMLWorkers;

import chat.network.NetworkConsts;
import chat.network.workerInterfaces.WorkerMessagesInterface;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import static chat.network.NetworkConsts.*;

public class XMLWorkerMessages implements WorkerMessagesInterface
{
    @Override
    public void createFileForMessages()
    {
        Document document = null;
        try
        {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element messagesContenet = document.createElement(MESSAGE_CONTENT);
            document.appendChild(messagesContenet);

            Element messageCsount = document.createElement(MESSAGES_COUNT);
            messageCsount.setTextContent(ZERO_STRING);
            messagesContenet.appendChild(messageCsount);

            Element messages = document.createElement(MESSAGES);
            messagesContenet.appendChild(messages);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult( new File(MESSAGES_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addMessage(String msg)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(MESSAGES_XML_NAME);

            Node messagesContent = document.getDocumentElement();
            NodeList messageSet = messagesContent.getChildNodes();

            Node messageCount = messageSet.item(0);
            int newMessageCount = Integer.parseInt(messageCount.getTextContent()) + 1;
            messageCount.setTextContent(String.valueOf(newMessageCount));

            Node messages = messageSet.item(1);

            Element newMessage = document.createElement(MESSAGE);
            newMessage.setTextContent(msg);
            Attr id = document.createAttribute(NUMBER);
            id.setTextContent(String.valueOf(newMessageCount));
            newMessage.setAttributeNode(id);

            messages.appendChild(newMessage);


            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult( new File(MESSAGES_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getFiveLastMessages()
    {
        String msgCount = null;
        Properties prop = new Properties();
        try
        {
            FileInputStream fileInputStream = new FileInputStream(NetworkConsts.SERVER_CONFIG_PATH);
            prop.load(fileInputStream);
            msgCount = prop.getProperty(NetworkConsts.MSG_COUNT);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        ArrayList<String> fiveMessages = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(MESSAGES_XML_NAME);

            Node messagesContent = document.getDocumentElement();
            NodeList messageSet = messagesContent.getChildNodes();

            Node messageCount = messageSet.item(0);
            int messageCountInt = Integer.parseInt(messageCount.getTextContent());

            Node messages = messageSet.item(1);

            NodeList message = messages.getChildNodes();

            int allowedCoutOfMessages = 0;

            if(messageCountInt >= Integer.parseInt(msgCount))
                allowedCoutOfMessages = Integer.parseInt(msgCount);
            else
                allowedCoutOfMessages = messageCountInt;

            for(int i = allowedCoutOfMessages; i > 0; i--)
                fiveMessages.add(message.item(messageCountInt - i).getTextContent());

        } catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
        return fiveMessages;
    }
}
