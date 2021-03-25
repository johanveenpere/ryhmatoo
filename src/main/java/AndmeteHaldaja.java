import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AndmeteHaldaja {
    private final float alampiir;
    private final float ulempiir;
    private final float keskKaalKriiteerium;
    private final float mootemaaramatus;
    private final int minValimSuurus;

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
    public AndmeteHaldaja(int minValimSuurus, float keskKaalKriiteerium, float mootemaaramatus, float alampiir, float ulempiir) {
        this.keskKaalKriiteerium = keskKaalKriiteerium;
        this.mootemaaramatus = mootemaaramatus;
        this.minValimSuurus = minValimSuurus;
        this.alampiir = alampiir;
        this.ulempiir = ulempiir;

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
    public void salvestaUuring(Uuring uuring) {
        String sqlCode = "INSERT INTO patsient VALUES(?,?,?,?,?,?,?)";

        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setString(1, uuring.getViit());
            sqlStatement.setFloat(2, uuring.getKaal());
            sqlStatement.setString(3, uuring.getSugu());
            sqlStatement.setInt(4, uuring.getVanus());
            sqlStatement.setFloat(5, uuring.getDoosiandmed());

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
    public List<Uuring> loeUuringud() {
        List<Uuring> koikUuringud = new ArrayList<>();
        try (Connection connection = this.connect()) {
            String sqlCode = "SELECT pildiviit,kaal,sugu,vanus,doosiandmed from uuring;";
            Statement sqlStatement = connection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(sqlCode);

            while (resultSet.next()) {
                Uuring andmeObjekt = new Uuring(resultSet.getString("pildiviit"), resultSet.getFloat("kaal"));
                andmeObjekt.setSugu(resultSet.getString("sugu"));
                andmeObjekt.setDoosiandmed(resultSet.getFloat("doosiandmed"));
                andmeObjekt.setVanus(resultSet.getInt("vanus"));
                koikUuringud.add(andmeObjekt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return koikUuringud;
    }
    private List<String> getValimPildiviidad() throws PuudulikValimException, SQLException {
        ArrayList<Object[]> sorteeritudDataset = new ArrayList<>();
        ArrayList<String> väljastatavValim = new ArrayList<>();
        int valimSuurus = 0;

        String sqlCode = "SELECT pildiviit,kaal, ABS(? - kaal) AS spread from uuring WHERE kaal < ? AND kaal > ? ORDER BY spread;";
        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);

            sqlStatement.setFloat(1,this.keskKaalKriiteerium);
            sqlStatement.setFloat(2,this.ulempiir);
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
                //System.out.println(hetkeErinevusKeskmisest);
                if (uuringuidVäljastatamiseks >= this.minValimSuurus &&  hetkeErinevusKeskmisest < mootemaaramatus) {
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
    private List<Uuring> getUuringudByPildiviit (List<String> pildiviidad) throws SQLException {
        ArrayList<Uuring> uuringObjektid = new ArrayList<>();
        String pildiviidaList = pildiviidad.stream()
                .map(s -> String.format("\"%s\"", s))
                .collect(Collectors.joining(","));
        String sqlQuery = "SELECT * FROM uuring WHERE pildiviit IN " + "(" + pildiviidaList + ")" ;
        try (
                Connection connection = this.connect();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                Uuring andmeObjekt = new Uuring(resultSet.getString("pildiviit"), resultSet.getFloat("kaal"));
                andmeObjekt.setSugu(resultSet.getString("sugu"));
                andmeObjekt.setDoosiandmed(resultSet.getFloat("doosiandmed"));
                uuringObjektid.add(andmeObjekt);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return uuringObjektid;
    }
    public List<Uuring> getValim () throws PuudulikValimException, SQLException {
        return getUuringudByPildiviit(getValimPildiviidad());
    }
}