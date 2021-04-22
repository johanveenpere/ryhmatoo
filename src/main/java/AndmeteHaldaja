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
    private float keskKaal;
    private float mootemaaramatus;
    private int minValim;
    private boolean kriteeriumidSeatud;

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
    public float getKeskKaal() {
        return keskKaal;
    }
    public void setKeskKaal(float keskKaal) {
        this.keskKaal= keskKaal;
    }
    public float getMootemaaramatus() {
        return mootemaaramatus;
    }
    public void setMootemaaramatus(float mootemaaramatus) {
        this.mootemaaramatus = mootemaaramatus;
    }
    public int getMinValim() {
        return minValim;
    }
    public void setMinValim(int minValim) {
        this.minValim = minValim;
    }
    public boolean isKriteeriumidSeatud() {
        return kriteeriumidSeatud;
    }

    /**
     * Meetodi eesmärk on luua ühendus lokaalse sqlite andmebaasiga
     * @return meetod tagastab Connection klassi isendi.
     */
    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/main/resources/andmebaas.db";
        Connection connection = null;
        connection = DriverManager.getConnection(url);
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
        }
    }

    /**
     * Meetod seab kriteeriumid millele valim peab vastama;
     * @param minValim int väärtus mis määrab ära miinimum uuringute arvu mida tagastada võib
     * @param keskKaal float väärtus mis määrab ära mis peab olema valimi keskmine kaal
     * @param mootemaaramatus float väärtus mis määrab valimi keskmise kaalu hindamisel arvesse võetavat mõõtemääramatust
     * @param alampiir float väärtus mis määrab ära valimis aksepteeritavate uuringute miinimum kaalu
     * @param ulempiir float väärtus mis määrab ära valimis aksepteeritavate uuringute maksimum kaalu
     */
    public void setKriteeriumid(int minValim, float keskKaal, float mootemaaramatus, float alampiir, float ulempiir) {
        this.keskKaal = keskKaal;
        this.mootemaaramatus = mootemaaramatus;
        this.minValim = minValim;
        this.alampiir = alampiir;
        this.ulempiir = ulempiir;
        this.kriteeriumidSeatud = true;
    }

    /**
     * Meetod salvestab andmebaasi uuringuid.
     * @param uuring Uuring klassi objekt
     */
    public void salvestaUuring(Uuring uuring) throws SQLException {
        String sqlCode = "INSERT INTO uuring VALUES(?,?,?,?,?,?, CURRENT_TIMESTAMP)";

        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setString(1, uuring.getViit());
            sqlStatement.setFloat(2, uuring.getKaal());
            sqlStatement.setString(3, uuring.getSugu());
            sqlStatement.setInt(4, uuring.getVanus());
<<<<<<< HEAD
            sqlStatement.setFloat(5, uuring.getDoosiandmed());
            sqlStatement.setString(6, uuring.getSeade_id());
=======
            //sqlStatement.setFloat(5, uuring.getDoosiandmed());
            //sqlStatement.setFloat(6, uuring.getIdSeade());

            LocalDate date = LocalDate.now();
            System.out.println("Kas sobib järgmine kuupäev: " + date + " (y/n) - ");
            try (Scanner myScanner = new Scanner(System.in)) {
                if (myScanner.next().equals("y")) {
                    sqlStatement.setString(7, date.toString());
                }
            }

>>>>>>> master
            sqlStatement.executeUpdate();
        }
    }

    /**
     * Meetod queryib andbebaasist kõik andmed ja väljastab Listi mis koosneb Uuring klassi objektidest
     * iga objekt on konkreetne uuring andmebaasis.
     * @return List Uuring objektidest kus iga objekt on kindel uuring
     */
    public List<Uuring> loeUuringud() throws SQLException {
        List<Uuring> koikUuringud = new ArrayList<>();
        try (Connection connection = this.connect()) {
            String sqlCode = "SELECT pildiviit,kaal,sugu,vanus,doosiandmed from uuring;";
            Statement sqlStatement = connection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(sqlCode);

            while (resultSet.next()) {
                Uuring andmeObjekt = new Uuring(resultSet.getString("pildiviit"), resultSet.getFloat("kaal"));
                andmeObjekt.setSugu(resultSet.getString("sugu"));
                //andmeObjekt.setDoosiandmed(resultSet.getFloat("doosiandmed"));
                andmeObjekt.setVanus(resultSet.getInt("vanus"));
                andmeObjekt.setKande_kuupaev("kande_kuupaev");
                andmeObjekt.setSeade_id("id_seade");
                koikUuringud.add(andmeObjekt);
            }
        }
        return koikUuringud;
    }

    /**
     * Meetod mis queryib andmebaasist kõik uuringud mis vastavad kriteeriumile ning väljastab kriteeriumitele
     * vastava valimi. Juhul kui valimit kokku ei tule väljastatakse erind
     *
     * Valimid järjestatakse keskmisest erineva hälve järgi kasvavas järjekorras ning seejärel itereeritakse
     * kuni valimi kriteeriumid on täidetud. Võib eksisteerida kombinatsioone kus valim tuleks kokku, kuid
     * algoritm seda ei leia.
     *
     * @return väljastab Listi kõigist uuringu pildiviitadest mis sobivad, et valim kokku tuleks
     * @throws PuudulikValimException väljastatakse kui valimit ei tule kokku sest uuringud ei vasta kriteeriumitele
     * @throws SQLException väljastatakse kui tekib probleem andmebaasiga suhtluses
     */
    private List<String> getValimPildiviidad() throws PuudulikValimException, SQLException {
        // Kontrollin kas kasutaja on valimi määramiseks vajalikud kriteeriumid seadnud
        if (!this.kriteeriumidSeatud) {
            throw new PuudulikValimException(PuudulikValimException.exceptionTypes.VALIMI_KRITEERIUMID_SEADMATA);
        }

        // Valim mis koosneb nende uuringute pildiviidast, kaalust mis vastasid min/max kaalukriteeriumitele
        ArrayList<Object[]> inValim = new ArrayList<>();
        // Valim mis koosneb nende uuringute pildiviitadest mis kokku vastavad kesk kaalu kriteeriumile
        ArrayList<String> outValim = new ArrayList<>();
        // Counter mis loeb kui suur on sorteerimata valim
        int unsortedValimSuurus = 0;

        // Meetod queryb andmebaasist need uuringud mis vastavad kaalukriteeriumitele
        String sqlCode = "SELECT pildiviit,kaal, ABS(? - kaal) AS spread from uuring WHERE kaal < ? AND kaal > ? ORDER BY spread;";
        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setFloat(1,this.keskKaal);
            sqlStatement.setFloat(2,this.ulempiir);
            sqlStatement.setFloat(3,this.alampiir);
            ResultSet resultSet = sqlStatement.executeQuery();

            while (resultSet.next()) {
                String pildiviit = resultSet.getString("pildiviit");
                float kaal = resultSet.getFloat("kaal");
                inValim.add(new Object[]{pildiviit,kaal});
                unsortedValimSuurus++;
            }

            // Kontrollitakse kas min valimi suurus tuli kokku
            if (unsortedValimSuurus < minValim) {
                PuudulikValimException e = new PuudulikValimException(PuudulikValimException.exceptionTypes.UURINGUTE_MIINIMUM_TÄITMATA);
                e.setUuringuidPuudu(minValim-unsortedValimSuurus);
                throw e;
            }

            float kaalKokku = 0;
            int sobivaidUuringuid = 0;
            // Kui valimi min suurus on täidetud hakkan salvestama infot väikseima hälvega valimi kohta
            float parimKeskmineKaal = 0;
            float parimHalve = this.ulempiir;

            // Lisan ükshaaval uuringuid väljastatavasse Listi ning kontrollin kas kriteeriumid on täidetud
            for (Object[] pildiviitKaal : inValim) {
                outValim.add((String) pildiviitKaal[0]);
                kaalKokku += (float) pildiviitKaal[1];
                sobivaidUuringuid++;
                float hetkeHalve = Math.abs(this.keskKaal- kaalKokku / sobivaidUuringuid);
                // Kontrollin kas valimi miinimum on täitunud
                if (sobivaidUuringuid >= this.minValim) {
                    if (hetkeHalve < parimHalve) {
                        parimHalve = hetkeHalve;
                        parimKeskmineKaal = kaalKokku / sobivaidUuringuid;
                    }
                    if (hetkeHalve < mootemaaramatus) {
                        return outValim;
                    }
                }
            }
            // Kui valimit ei väljastata siis viskan exceptioni ja panen kaasa parima keskmise kaalu
            // mis valimi sorteerimise ajal saavutati.
            PuudulikValimException e = new PuudulikValimException(PuudulikValimException.exceptionTypes.SOBIMATU_KAALUKESKMINE);
            e.setHetkeKeskmine(parimKeskmineKaal);
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
        // Teisendan pildiviidad Listist soneks mis on eraldatud komadega ja iga some ümber on jutumärgid
        String pildiviidadSonena = pildiviidad.stream()
                .map(s -> String.format("\"%s\"", s))
                .collect(Collectors.joining(","));
        String sqlQuery = "SELECT * FROM uuring WHERE pildiviit IN " + "(" + pildiviidadSonena + ")" ;
        try (
                Connection connection = this.connect();
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                Uuring andmeObjekt = new Uuring(resultSet.getString("pildiviit"), resultSet.getFloat("kaal"));
                andmeObjekt.setSugu(resultSet.getString("sugu"));
                //andmeObjekt.setDoosiandmed(resultSet.getFloat("doosiandmed"));
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

    /**
     * Meetodi eesmärk on uuendada andmebaasis olevaid uuringuid
     * @param uuring Uuring klassi objekt mille pildiviit vastab sellele pildiviidile mis juba andmebaasides on
     *               ning objekti muutujad on nende väärtustega mida soovitakse muuta.
     * @throws SQLException Meetodi jooksmise jooksul on võimalik ainult SQLExceptionite tekkimine
     */
    public void uuendaUuring(Uuring uuring) throws SQLException{
        String sqlCode = "UPDATE uuring SET kaal = ?, sugu = ?, vanus = ?, doosiandmed = ?, id_seade = ?, WHERE pildiviit = ?;";
        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setFloat(1, uuring.getKaal());
            sqlStatement.setString(2, uuring.getSugu());
            sqlStatement.setInt(3, uuring.getVanus());
            sqlStatement.setFloat(4, uuring.getDoosiandmed());
            sqlStatement.setString(5, uuring.getSeade_id());
            sqlStatement.setString(6, uuring.getViit());
            sqlStatement.executeUpdate();
        }
    }
    /**
     * Meetodi eesmärk on andmebaasist uuringuid eemaldada
     * @param pildiviit String pildiviit mida soovitakse eemaldada
     * @throws SQLException
     */
    public void eemaldaUuring(String pildiviit) throws SQLException {
        String sqlCode = "DELETE FROM uuring WHERE pildiviit = ?";
        try (Connection connection = this.connect()) {
            PreparedStatement sqlStatement = connection.prepareStatement(sqlCode);
            sqlStatement.setString(1, pildiviit);
            sqlStatement.executeUpdate();
        }
    }
}