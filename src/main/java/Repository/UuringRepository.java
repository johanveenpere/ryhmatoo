package Repository;

import Model.Uuring;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class UuringRepository {
    protected EntityManagerFactory emf;
    protected EntityManager em;

    public UuringRepository(EntityManagerFactory emf, EntityManager em) {
        this.emf = emf;
        this.em = em;
    }

    public Uuring getUuring(String viit) {
        return this.em.find(Uuring.class, viit);
    }

    public void addUuring(Uuring uuring) {
        this.em.getTransaction().begin();
        this.em.persist(uuring);
        this.em.getTransaction().commit();
    }

    public void alterUuring(Uuring uuring) {
        this.em.getTransaction().begin();
        this.em.merge(uuring);
        this.em.getTransaction().commit();
    }

    public <T extends Uuring> List<Uuring> getAllUuringud(Class<T> uuringType) {
        return this.em.createQuery("SELECT c FROM " + uuringType.getName() + " AS c", Uuring.class).getResultList();
    }

    public <T extends Uuring> List<Uuring> getValimiKandidaadid(Class<T> uuringType, double minKaal, double maxKaal, double minSuurus) {
        return this.em.createQuery("SELECT c FROM " + uuringType.getName() + " AS c WHERE c.kaal < " + maxKaal + " AND c.kaal > " + minKaal, Uuring.class).getResultList();
    }
}
