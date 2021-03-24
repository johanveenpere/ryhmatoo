import java.util.Arrays;
import java.util.List;

public class TestAndmebaas {
    public static void main(String[] args) {
        AndmeteHaldaja haldaja = new AndmeteHaldaja(10, 80, 5, 60, 85);
        try {
            System.out.println(haldaja.getValimPildiviidad());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /*
        haldaja.salvestaAndmed("HTYKRG4210311xxx",Float.valueOf("23"), "M", 18, Float.valueOf("13"), "AF123");
        List<Andmed> koikPatisendid = haldaja.getKoikPatsiendid();
        for (Andmed patsient : koikPatisendid) {
            System.out.println(patsient.getViit());
            System.out.println(patsient.getKaal());
         */
    }
}
