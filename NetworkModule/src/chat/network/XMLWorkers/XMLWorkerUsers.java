package chat.network.XMLWorkers;

import chat.network.workerInterfaces.WorkerUsersInterface;
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
import java.io.IOException;
import java.util.ArrayList;

import static chat.network.NetworkConsts.*;

public class XMLWorkerUsers implements WorkerUsersInterface
{
    @Override
    public void createFileForUsers()
    {
        Document document = null;
        try
        {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element messagesContenet = document.createElement(USERS_CONTENT);
            document.appendChild(messagesContenet);

            Element messageCsount = document.createElement(USERS_COUNT);
            messageCsount.setTextContent(ZERO);

            messagesContenet.appendChild(messageCsount);

            Element messages = document.createElement(USERS);
            messagesContenet.appendChild(messages);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult( new File(USERS_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getUserName(String ip, int port)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(USERS_XML_NAME);

            Node usersContent = document.getDocumentElement();
            NodeList usersSet = usersContent.getChildNodes();

            Node usersCount = usersSet.item(0);
            int usersCountInt = Integer.parseInt(usersCount.getTextContent());

            Node users = usersSet.item(1);
            NodeList usersList = users.getChildNodes();

            for(int i = 0; i < usersCountInt; i++)
            {
                Node currentUser = usersList.item(i);
                NodeList currentUserList = currentUser.getChildNodes();

                if(currentUserList.item(0).getTextContent().equals(ip.toString()) && currentUserList.item(1).getTextContent().equals(String.valueOf(port)))
                    return currentUserList.item(2).getTextContent();

            }


        } catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addNewUser(String ip, int port, String userName)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(USERS_XML_NAME);

            Node usersContent = document.getDocumentElement();
            NodeList usersSet = usersContent.getChildNodes();

            Node usersCount = usersSet.item(0);
            int newUsersCount = Integer.parseInt(usersCount.getTextContent()) + 1;
            usersCount.setTextContent(String.valueOf(newUsersCount));

            Node users = usersSet.item(1);

            Element newUser = document.createElement(USER);
            Attr id = document.createAttribute(NUMBER);
            id.setTextContent(String.valueOf(newUsersCount));
            newUser.setAttributeNode(id);

            Element ipElement = document.createElement(IP);
            ipElement.setTextContent(ip);

            Element portElement = document.createElement(PORT);
            portElement.setTextContent(String.valueOf(port));

            Element userNameElement = document.createElement(USER_NAME);
            userNameElement.setTextContent(userName);

            Element statusElement = document.createElement(STATUS);
            statusElement.setTextContent("online");

            newUser.appendChild(ipElement);
            newUser.appendChild(portElement);
            newUser.appendChild(userNameElement);
            newUser.appendChild(statusElement);

            users.appendChild(newUser);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult( new File(USERS_XML_NAME));
            transformer.transform(source, result);


        } catch (ParserConfigurationException | TransformerException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void changeStatus(String ip, int port, String userName)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(USERS_XML_NAME);

            Node usersContent = document.getDocumentElement();
            NodeList usersSet = usersContent.getChildNodes();

            Node usersCount = usersSet.item(0);
            int usersCountInt = Integer.parseInt(usersCount.getTextContent());

            Node users = usersSet.item(1);
            NodeList usersList = users.getChildNodes();

            for(int i = 0; i < usersCountInt; i++)
            {
                Node currentUser = usersList.item(i);
                NodeList currentUserList = currentUser.getChildNodes();
                if(currentUserList.item(0).getTextContent().equals(ip) && currentUserList.item(1).getTextContent().equals(String.valueOf(port)) && currentUserList.item(2).getTextContent().equals(userName))
                {
                    if(currentUserList.item(3).getTextContent().equals(ONLINE))
                        currentUserList.item(3).setTextContent(OFFLINE);
                    else
                        currentUserList.item(3).setTextContent(ONLINE);
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult( new File(USERS_XML_NAME));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getStatus(String ip, int port, String userName)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(USERS_XML_NAME);

            Node usersContent = document.getDocumentElement();
            NodeList usersSet = usersContent.getChildNodes();

            Node usersCount = usersSet.item(0);
            int usersCountInt = Integer.parseInt(usersCount.getTextContent());

            Node users = usersSet.item(1);
            NodeList usersList = users.getChildNodes();

            for(int i = 0; i < usersCountInt; i++)
            {
                Node currentUser = usersList.item(i);
                NodeList currentUserList = currentUser.getChildNodes();
                if(currentUserList.item(0).getTextContent().equals(ip) && currentUserList.item(1).getTextContent().equals(String.valueOf(port)) && currentUserList.item(2).getTextContent().equals(userName))
                {
                    return currentUserList.item(3).getTextContent();
                }
            }

        } catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getOnlineUsers()
    {
        ArrayList<String> onlineUsers = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(USERS_XML_NAME);

            Node usersContent = document.getDocumentElement();
            NodeList usersSet = usersContent.getChildNodes();

            Node usersCount = usersSet.item(0);
            int usersCountInt = Integer.parseInt(usersCount.getTextContent());

            Node users = usersSet.item(1);
            NodeList usersList = users.getChildNodes();

            for(int i = 0; i < usersCountInt; i++)
            {
                Node currentUser = usersList.item(i);
                NodeList currentUserList = currentUser.getChildNodes();

                if(currentUserList.item(3).getTextContent().equals(ONLINE))
                    onlineUsers.add(currentUserList.item(2).getTextContent());
            }

        } catch (ParserConfigurationException | IOException | SAXException e)
        {
            e.printStackTrace();
        }
        return onlineUsers;
    }
}
