import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class AndmeteHaldaja {
    private float alampiir;
    private float ülempiir;
    private float keskKaalKriiteerium = 75;
    private float mootemääramatus = 2;
    private int minValimSuurus = 0;

    // Ühenda andmebaasiga
    private Connection connect() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/resources/andmebaas.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    // Initialiseeri andmebaas ja default parameetrid
    public AndmeteHaldaja(int minValimSuurus, float keskKaalKriiteerium, float mootemääramatus, float alampiir, float ülempiir) {
        this.keskKaalKriiteerium = keskKaalKriiteerium;
        this.mootemääramatus = mootemääramatus;
        this.minValimSuurus = minValimSuurus;
        this.alampiir = alampiir;
        this.ülempiir = ülempiir;

        try (Connection connection = this.connect()) {
            if (connection != null) {
                DatabaseMetaData metadata = connection.getMetaData();
                System.out.println("Andmebaasi driver on " + metadata.getDriverName());
                System.out.println("Andmebaas edukalt loodud.");

                String sqlCode = "CREATE TABLE IF NOT EXISTS uuring(" +
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Salvest andmebaasi andmeid
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Andmed> getUuringud() {
        List<Andmed> koikUuringud = new ArrayList<>();
        try (Connection connection = this.connect()) {
            String sqlCode = "SELECT pildiviit,kaal,sugu,vanus,doosiandmed from uuring;";
            Statement sqlStatement = connection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(sqlCode);

            while (resultSet.next()) {
                Andmed andmeObjekt = new Andmed();
                andmeObjekt.setViit(resultSet.getString("pildiviit"));
                andmeObjekt.setKaal(resultSet.getFloat("kaal"));
                andmeObjekt.setSugu(resultSet.getString("sugu"));
                andmeObjekt.setDoosiandmed(resultSet.getFloat("doosiandmed"));
                koikUuringud.add(andmeObjekt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return koikUuringud;
    }
    public ArrayList<String> getValimPildiviidad() throws PuudulikValimException, SQLException {
        ArrayList<Object[]> sorteeritudDataset = new ArrayList<>();
        ArrayList<String> väljastatavValim = new ArrayList<>();
        int valimSuurus = 0;

        String sqlCode = "SELECT pildiviit,kaal, ABS(? - kaal) AS spread from uuring WHERE kaal < ? AND kaal > ? ORDER BY spread;";
        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);

            sqlStatement.setFloat(1,this.keskKaalKriiteerium);
            sqlStatement.setFloat(2,this.ülempiir);
            sqlStatement.setFloat(3,this.alampiir);
            ResultSet resultSet = sqlStatement.executeQuery();

            while (resultSet.next()) {
                valimSuurus++;
                String pildiviit = resultSet.getString("pildiviit");
                float kaal = resultSet.getFloat("kaal");
                sorteeritudDataset.add(new Object[]{pildiviit,kaal});
            }

            if (valimSuurus < minValimSuurus) {
                throw new PuudulikValimException("Valim ei tule hindamisele. Kaalupiiridesse jäävaid patsiente on vähem kui valimi miinimum");
            }

            float kaalKokku = 0;
            int uuringuidVäljastatamiseks = 0;

            for (Object[] pildiviitKaal : sorteeritudDataset) {
                väljastatavValim.add((String) pildiviitKaal[0]);
                kaalKokku += (float) pildiviitKaal[1];
                uuringuidVäljastatamiseks++;
                float hetkeErinevusKeskmisest = Math.abs(this.keskKaalKriiteerium - kaalKokku / uuringuidVäljastatamiseks);
                System.out.println(hetkeErinevusKeskmisest);
                if (uuringuidVäljastatamiseks >= this.minValimSuurus &&  hetkeErinevusKeskmisest < mootemääramatus) {
                    return väljastatavValim;
                }
            }
            throw new PuudulikValimException("Valim ei tule kokku. Kaalupiiridesse jäävate patsientide kaalukeskmine on mõõteääramatusest väljas");
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}