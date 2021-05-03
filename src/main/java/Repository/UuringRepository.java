package Repository;

import Model.Uuring;
import Service.Kriteerium;

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
        this.em.getTransaction().begin();
        this.em.persist(uuring);
        this.em.getTransaction().commit();
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

    /**
     * Tagasta andmebaasist kõik uuringud mis on teatud Uuringu tüüpi.
     * @param uuringType Uuring klass mis uuringuid soovitakse väljastada
     * @param <T> Uuring tüüpi klass
     * @return List soovitud Uuringu objektidest
     */
    public <T extends Uuring> List<Uuring> getAllUuringud(Class<T> uuringType) {
        return this.em.createQuery("SELECT c FROM " + uuringType.getName() + " AS c", Uuring.class).getResultList();
    }

    /**
     * Tagasta andmebaasist kõik uuringud mis on teatud Uuringu tüüpi ning vastavad kaalukriteeriumitele
     * @param kriteerium Kriteeriumi objekt mille järgi valimit otsitakse
     * @param uuringType Uuring klass mis uuringuid soovitakse väljastada
     * @param <T> Uuring tüüpi klass
     * @return List soovitud Uuring objektidest mis vastavad min/max kaalu kriteeriumitele
     */
    public <T extends Uuring> List<Uuring> getValimiKandidaadid(Class<T> uuringType, Kriteerium kriteerium) {
        return this.em.createQuery("SELECT c FROM " + uuringType.getName() + " AS c WHERE c.kaal < " + kriteerium.getMaxKaal() + " AND c.kaal > " + kriteerium.getMinKaal(), Uuring.class).getResultList();
    }
}
