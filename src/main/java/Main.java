import Model.*;
import Service.ValimiSelekteerija;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        try {
            ValimiSelekteerija<PeaNatiivUuring> selekteerija = new ValimiSelekteerija<PeaNatiivUuring>(emf, PeaNatiivUuring.class, 100, 10, 60, 2);
            selekteerija.getValim();
        }
        finally {
            emf.close();
        }
    }
}
