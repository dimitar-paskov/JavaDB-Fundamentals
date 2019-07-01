import java.sql.*;
import java.util.Properties;

public class Application {

    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "dimitar");
        properties.setProperty("password", "123456");

        final String connectionURL = "jdbc:mysql://localhost:3306/minions_db?serverTimezone=UTC";

        Connection connection = DriverManager.getConnection(connectionURL, properties);

        Engine engine = new Engine(connection);
        engine.run();



    }
}
