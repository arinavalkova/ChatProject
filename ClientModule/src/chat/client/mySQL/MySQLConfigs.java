package chat.client.mySQL;

public class MySQLConfigs
{
    protected static String bdHost = "localhost";
    protected static String bdPort = "3306";
    protected static String bdUser = "root";
    protected static String bdPass = "12345";
    protected static String bdName = "chatbd";

    static final String CONNECTION_URL_FIRST_PART = "jdbc:mysql://127.0.0.1:3306/";
    static final String CONNECTION_URL_SECOND_PART = "?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String INSERT_FIRST_PART = "INSERT INTO ";
    static final String INSERT_LAST_PART = "VALUES(?,?)";
    static final String OPENNNING_BRACKET = "(";
    static final String COMMA = ",";
    static final String CLOSING_BRACKET = ")";
    static final String SELECT_FROM = "SELECT * FROM ";
    static final String WHERE = " WHERE ";
    static final String QUESTION = "=?";
    static final String QUESTION_AND = "=? AND ";

    static final int FIRST_PARAMETER = 1;
    static final int SECOND_PARAMETER = 2;
}
