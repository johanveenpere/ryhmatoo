import java.util.List;

public class TestAndmebaas {
    public static void main(String[] args) {
        try {
            AndmeteHaldaja haldaja = new AndmeteHaldaja();
            haldaja.setKriteeriumid(14,75,2,65,90);
            //List<Uuring> uuringud = haldaja.loeUuringud();
            List<Uuring> valim = haldaja.getValim();
            printUuringud(valim);
        }
        catch (PuudulikValimException e) {
            switch (e.getMessage()) {
                case "SOBIMATU_KAALUKESKMINE" -> System.out.println(e.getMessage() + " - parim keskmine: " + e.getHetkeKeskmine());
                case "UURINGUTE_MIINIMUM_TÄITMATA" -> System.out.println(e.getMessage() +  " - puudu uuringuid: " + e.getUuringuidPuudu());
                default -> System.out.println(e.getMessage());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void printUuringud(List<Uuring> uuringud) {
        int counter = 1;
        for (Uuring uuring : uuringud) {
            System.out.println(counter + ". " +uuring.toString());
            counter++;
        }
    }
}
