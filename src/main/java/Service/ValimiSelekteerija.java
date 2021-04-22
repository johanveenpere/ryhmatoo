package Service;
import Model.Uuring;
import Repository.UuringRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.List;

public class ValimiSelekteerija <T extends Uuring> {
    private Class<T> uuringType;
    private EntityManagerFactory emf;
    private double maxKaal;
    private double minKaal;
    private double keskKaal;
    private int minValim;

    public ValimiSelekteerija(EntityManagerFactory emf, Class<T> uuringType, double maxKaal, double minKaal, double keskKaal, int minValim) {
        this.emf = emf;
        this.uuringType = uuringType;
        this.maxKaal = maxKaal;
        this.minKaal = minKaal;
        this.keskKaal = keskKaal;
        this.minValim = minValim;
    }
    public void getValim() {
        EntityManager em = emf.createEntityManager();
        try {
            UuringRepository repo = new UuringRepository(this.emf,em);
            List<Uuring> valim = repo.getAllUuringud(uuringType);
            Collections.sort(valim);
            for (Uuring uuring : valim) {
                System.out.println(uuring.toString());
            }
        }
        finally {
            em.close();
        }
    }
}
