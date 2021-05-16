package Repository;

import Model.Uuring;
import Service.Kriteerium;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;

public class UuringRepository {
    protected EntityManagerFactory emf;
    protected EntityManager em;

    public UuringRepository(EntityManagerFactory emf) {
        this.emf = emf;
        em = emf.createEntityManager();
    }

    public UuringRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * Tagasta Uuring tüüpi objekt pildiviida kaudu
     * @param viit uuringu pildiviit
     * @return Uuring tüüpi objekt
     */
    public Uuring getUuring(String viit) {
        return this.em.find(Uuring.class, viit);
    }

    /**
     * Lisa Uuring objekt andmebaasi
     * @param uuring Uuring objekt
     */
    public void addUuring(Uuring uuring) {
        if (this.em.find(Uuring.class,uuring.getViit()) == null) {
            this.em.getTransaction().begin();
            this.em.persist(uuring);
            this.em.getTransaction().commit();
        }
        else {
            throw new EntityExistsException();
        }
    }

    /**
     * Muuda andmebaasis uuringu väärtust
     * @param uuring Uuring tüüpi objekt
     */
    public void alterUuring(Uuring uuring) {
        this.em.getTransaction().begin();
        this.em.merge(uuring);
        this.em.getTransaction().commit();
    }

    public void removeUuring(Uuring uuring) {
        this.em.getTransaction().begin();
        this.em.remove(uuring);
        this.em.getTransaction().commit();
    }

    public void removeUuring(String viit) {
        this.em.getTransaction().begin();
        this.em.remove(getUuring(viit));
        this.em.getTransaction().commit();
    }

    public void deleteAll() {
        this.em.getTransaction().begin();
        this.em.createQuery("DELETE from Uuring").executeUpdate();
        this.em.getTransaction().commit();
    }

    /**
     * Tagasta andmebaasist kõik uuringud mis on teatud Uuringu tüüpi.
     * @param uuringType Uuring klass mis uuringuid soovitakse väljastada
     * @param <T> Uuring tüüpi klass
     * @return List soovitud Uuringu objektidest
     */
    public <T extends Uuring> List<Uuring> getAllUuringud(Class<T> uuringType) {
        List<Uuring> uuringud = this.em.createQuery("SELECT c FROM " + uuringType.getName() + " AS c", Uuring.class).getResultList();
        if (uuringud.size() == 0)
            throw new TühiUuringulistException();
        return uuringud;
    }

    public <T extends Uuring> List<Uuring> getViimasedUuringud(int uuringutearv) {
        List<Uuring> uuringud = this.em.createQuery("SELECT u FROM Uuring u ORDER BY u.loomiseaeg DESC", Uuring.class).setMaxResults(uuringutearv).getResultList();
        if (uuringud.size() == 0)
            throw new TühiUuringulistException();
        return uuringud;
    }

    public List<Uuring> getAllTäitmataUuringud() {
        return this.em.createQuery("SELECT c FROM Uuring AS c WHERE c.täidetud = false", Uuring.class).getResultList();
    }

    /**
     * Tagasta andmebaasist kõik uuringud mis on teatud Uuringu tüüpi ning vastavad kaalukriteeriumitele
     * @param kriteerium Kriteeriumi objekt mille järgi valimit otsitakse
     * @param uuringType Uuring klass mis uuringuid soovitakse väljastada
     * @param <T> Uuring tüüpi klass
     * @return List soovitud Uuring objektidest mis vastavad min/max kaalu kriteeriumitele
     */
    public <T extends Uuring> List<Uuring> getByKriteerium(Class<T> uuringType, Kriteerium kriteerium) {
            List<Uuring> uuringud = em.createQuery("SELECT c FROM " + uuringType.getName() + " AS c WHERE  c.kaal >= :minkaal", Uuring.class).setParameter("minkaal",kriteerium.getMinKaal()).getResultList();
            uuringud.removeIf(uuring -> !kriteerium.uuringVastabKriteeriumile(uuring));
        if (uuringud.size() == 0)
            throw new TühiUuringulistException();
        return uuringud;
    }
}
