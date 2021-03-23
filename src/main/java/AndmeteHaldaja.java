import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class AndmeteHaldaja {
    public static void main(String[] args) {
        looAndmebaas();
    }
    public static void looAndmebaas() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/resources/andmebaas";
        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                DatabaseMetaData metadata = connection.getMetaData();
                System.out.println("Andmebaasi nimi on " + metadata.getDriverName());
                System.out.println("Andmebaas edukalt loodud.");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
