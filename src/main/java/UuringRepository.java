import Model.Uuring;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UuringRepository {
    private EntityManagerFactory emf;
    private EntityManager em;

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
}
