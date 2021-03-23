import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class AndmeteHaldaja {

    private Connection connect() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/resources/andmebaas.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public AndmeteHaldaja () {
        try (Connection connection = this.connect()) {
            if (connection != null) {
                DatabaseMetaData metadata = connection.getMetaData();
                System.out.println("Andmebaasi driver on " + metadata.getDriverName());
                System.out.println("Andmebaas edukalt loodud.");

                String sqlCode = "CREATE TABLE IF NOT EXISTS patsient(" +
                        "pildiviit varchar(16) NOT NULL PRIMARY KEY," +
                        "kaal FLOAT(24)," +
                        "sugu VARCHAR(1)," +
                        "vanus int(3)," +
                        "doosiandmed FLOAT(24)," +
                        "id_seade VARCHAR(16)," +
                        "kande_kuupaev DATE" +
                        ");";
                try (Statement statement = connection.createStatement()) {
                    statement.execute(sqlCode);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void salvestaAndmed(String pildiviit, Float kaal, String sugu, int vanus, Float doosiandmed, String idSeade) {
        String sqlCode = "INSERT INTO patsient VALUES(?,?,?,?,?,?,?)";

        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setString(1, pildiviit);
            sqlStatement.setFloat(2, kaal);
            sqlStatement.setString(3, sugu);
            sqlStatement.setInt(4, vanus);
            sqlStatement.setFloat(5, doosiandmed);
            sqlStatement.setString(6, idSeade);

            System.out.print("Kas sobib järgmine kuupäev: ");
            LocalDate date = LocalDate.now();
            System.out.print(date + " (y/n) - ");
            try (Scanner myScanner = new Scanner(System.in)) {
                if (myScanner.next().equals("y")) {
                    sqlStatement.setString(7, date.toString());
                }
            }

            sqlStatement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
