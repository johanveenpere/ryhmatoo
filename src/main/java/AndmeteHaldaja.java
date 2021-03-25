import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Klassi eesmärk on suhelda andmebaasiga. Sisend ja väljund päringud toimuvad kõik Uuring klassi isendite kaudu.
 * @author Tomi Theodor Kuusik
 */
public class AndmeteHaldaja {
    private float alampiir;
    private float ulempiir;
    private float keskKaalKriiteerium;
    private float mootemaaramatus;
    private int minValimSuurus;

    public float getAlampiir() {
        return alampiir;
    }

    public void setAlampiir(float alampiir) {
        this.alampiir = alampiir;
    }

    public float getUlempiir() {
        return ulempiir;
    }

    public void setUlempiir(float ulempiir) {
        this.ulempiir = ulempiir;
    }

    public float getKeskKaalKriiteerium() {
        return keskKaalKriiteerium;
    }

    public void setKeskKaalKriiteerium(float keskKaalKriiteerium) {
        this.keskKaalKriiteerium = keskKaalKriiteerium;
    }

    public float getMootemaaramatus() {
        return mootemaaramatus;
    }

    public void setMootemaaramatus(float mootemaaramatus) {
        this.mootemaaramatus = mootemaaramatus;
    }

    public int getMinValimSuurus() {
        return minValimSuurus;
    }

    public void setMinValimSuurus(int minValimSuurus) {
        this.minValimSuurus = minValimSuurus;
    }

    public boolean isKriteeriumidSeatud() {
        return kriteeriumidSeatud;
    }

    private boolean kriteeriumidSeatud;

    /**
     * Meetodi eesmärk on luua ühendus lokaalse sqlite andmebaasiga
     * @return meetod tagastab Connection klassi isendi.
     */
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

    /**
     * Meetod kontrollib andmebaasi olemasolu ning juhul kui andmebaasi ei eksisteeri loob selle
     * Andmebaasis on üks tabel 'uuring' mis sisaldab 7 attribuuti:
     * 'pildiviit' (PK), 'kaal', 'sugu', vanus', 'doosiandmed', 'id_seade', 'kande_kuupaev'
     */
    public AndmeteHaldaja() throws SQLException {
        try (Connection connection = this.connect()) {
            if (connection != null) {
                System.out.println("ÜHENDATUD ANDMEBAASI");
                DatabaseMetaData metadata = connection.getMetaData();

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
            throw e;
        }
    }

    /**
     * Meetod seab kriteeriumid millele valim peab vastama;
     * @param minValimSuurus int väärtus mis määrab ära miinimum uuringute arvu mida tagastada võib
     * @param keskKaalKriiteerium float väärtus mis määrab ära mis peab olema valimi keskmine kaal
     * @param mootemaaramatus float väärtus mis määrab valimi keskmise kaalu hindamisel arvesse võetavat mõõtemääramatust
     * @param alampiir float väärtus mis määrab ära valimis aksepteeritavate uuringute miinimum kaalu
     * @param ulempiir float väärtus mis määrab ära valimis aksepteeritavate uuringute maksimum kaalu
     */
    public void setParameetrid(int minValimSuurus, float keskKaalKriiteerium, float mootemaaramatus, float alampiir, float ulempiir) {
        this.keskKaalKriiteerium = keskKaalKriiteerium;
        this.mootemaaramatus = mootemaaramatus;
        this.minValimSuurus = minValimSuurus;
        this.alampiir = alampiir;
        this.ulempiir = ulempiir;
        this.kriteeriumidSeatud = true;
    }

    /**
     * Meetod salvestab andmebaasi uuringuid.
     * @param uuring Uuring klassi objekt
     */
    public void salvestaUuring(Uuring uuring) throws SQLException {
        String sqlCode = "INSERT INTO uuring VALUES(?,?,?,?,?,?,?)";

        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setString(1, uuring.getViit());
            sqlStatement.setFloat(2, uuring.getKaal());
            sqlStatement.setString(3, uuring.getSugu());
            sqlStatement.setInt(4, uuring.getVanus());
            sqlStatement.setFloat(5, uuring.getDoosiandmed());
            //sqlStatement.setFloat(6, uuring.getIdSeade());

            LocalDate date = LocalDate.now();
            System.out.println("Kas sobib järgmine kuupäev: " + date + " (y/n) - ");
            try (Scanner myScanner = new Scanner(System.in)) {
                if (myScanner.next().equals("y")) {
                    sqlStatement.setString(7, date.toString());
                }
            }

            sqlStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Meetod queryib andbebaasist kõik andmed ja väljastab Listi mis koosneb Uuring klassi objektidest
     * iga objekt on konkreetne uuring andmebaasis.
     * @return List Uuring objektidest kus iga objekt on kindel uuring
     */
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

    /**
     * Meetod mis queryib andmebaasist kõik uuringud mis vastavad kriteeriumile ning väljastab kriteeriumitele
     * vastava valimi. Juhul kui valimit kokku ei tule väljastatakse erind
     * @return väljastab Listi kõigist uuringu pildiviitadest mis sobivad, et valim kokku tuleks
     * @throws PuudulikValimException väljastatakse kui valimit ei tule kokku sest uuringud ei vasta kriteeriumitele
     * @throws SQLException väljastatakse kui tekib probleem andmebaasiga suhtluses
     */
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
                throw new PuudulikValimException("UURINGUTE_MIINIMUM_TÄITMATA_EXCEPTION");
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
            if (kriteeriumidSeatud) {
                throw new PuudulikValimException("SOBIMATU_KAALUKESKMINE_EXCEPTION");
            }
            else {
                throw new PuudulikValimException("VALIMI_KRITEERIUMID_SEADMATA_EXCEPTION");
            }
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Meetod väljastab listi Uuring klassi objektidest ning võtab sisendiks Listi mis koosneb pildiviitadest.
     * Meetodit kasutatakse, et valim seostada uuringutega.
     * @param pildiviidad List pildiviitadest
     * @return List mis koosneb Uuring objektidest
     * @throws SQLException
     */
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

    /**
     * Peameetod valimi saamiseks.
     * @return Väljastab Listi mis koosneb uuring objektidest
     * @throws PuudulikValimException
     * @throws SQLException
     */
    public List<Uuring> getValim () throws PuudulikValimException, SQLException {
        return getUuringudByPildiviit(getValimPildiviidad());
    }
}