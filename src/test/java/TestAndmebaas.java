import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAndmebaas {
    public static void main(String[] args) {
        /*
        Uuring uuring1 = new Uuring("HTYKRG4210311xxx", 23);
        uuring1.setVanus(18);
        uuring1.setSugu("M");
        uuring1.setDoosiandmed(12345);
         */
        try {
            AndmeteHaldaja haldaja = new AndmeteHaldaja();
            haldaja.setKriteeriumid(10, 75, 3, 60, 85);
            List<Uuring> uuringud = haldaja.getValim();
            //List<Uuring> uuringud = haldaja.loeUuringud();
            int counter = 1;
            for (Uuring uuring : uuringud) {
                System.out.println(counter + ". " +uuring.toString());
                counter++;
            }
        }
        catch (PuudulikValimException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getHetkeKeskmine());
            System.out.println(e.getUuringuidPuudu());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /*
        List<Andmed> koikPatisendid = haldaja.getKoikPatsiendid();
        for (Andmed patsient : koikPatisendid) {
            System.out.println(patsient.getViit());
            System.out.println(patsient.getKaal());
         */
    }
}
