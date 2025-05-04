import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/gcs";
    private static final String USER = "leona";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco", e);
        }
    }

}
