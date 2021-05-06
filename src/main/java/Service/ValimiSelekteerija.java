package Service;
import Model.Uuring;
import Repository.UuringRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.stream.Stream;

import static Service.PuudulikValimException.exceptionTypes.SOBIMATU_KAALUKESKMINE;
import static Service.PuudulikValimException.exceptionTypes.UURINGUTE_MIINIMUM_TÄITMATA;

/**
 * Klass suudab tagastada valimi mis vastab kriteeriumitele
 * @param <T> uuringu klassi tüüp millest soovitakse valik teha
 */
public class ValimiSelekteerija <T extends Uuring> {
    private final Class<T> uuringType;
    private final EntityManagerFactory emf;
    private final Kriteerium kriteerium;

    /**
     * Konstruktoris on vaja määrata järgmised parameetrid:
     * @param uuringType Uuring klass mille kohta soovitakse valim sorteerida
     * @param emf EMF
     * @param kriteerium kriteeriumi objekt mille järgi valimit sorteeritakse
     */
    public ValimiSelekteerija(Class<T> uuringType, EntityManagerFactory emf, Kriteerium kriteerium) {
        this.uuringType = uuringType;
        this.emf = emf;
        this.kriteerium = kriteerium;
    }

    public Valim getValim() throws PuudulikValimException {
        EntityManager em = emf.createEntityManager();
        try {
            UuringRepository repo = new UuringRepository(this.emf);
            Valim sorteerimataValim = new Valim(kriteerium, uuringType, repo.getValimiKandidaadid(uuringType, kriteerium));
            if (!sorteerimataValim.isMiinimumTäidetud()) {
                throw new PuudulikValimException(UURINGUTE_MIINIMUM_TÄITMATA, sorteerimataValim);
            }
            Valim sorteeritudValim = findValim(sorteerimataValim.getUuringud().toArray(new Uuring[sorteerimataValim.getSuurus()]), new Uuring[0]);
            if (!sorteeritudValim.isVastabKriteeriumitele()) {
                throw new PuudulikValimException(SOBIMATU_KAALUKESKMINE, sorteeritudValim);
            }
            else {
                return sorteeritudValim;
            }
        }
        finally {
            em.close();
        }
    }
    private Valim findValim(Uuring[] searchPool, Uuring[] resultPool) {
        if (searchPool.length + resultPool.length == this.kriteerium.getMinValim()) {
            resultPool = Stream.concat(Arrays.stream(searchPool),Arrays.stream(resultPool)).toArray(Uuring[]::new);
            searchPool = new Uuring[0];
        }
        if (searchPool.length == 0) {
            return new Valim(this.kriteerium,this.uuringType,Arrays.asList(resultPool));
        }
        Valim skipLast = null;
        if (searchPool.length >= this.kriteerium.getMinValim()) {
            skipLast = findValim(Arrays.copyOf(searchPool, searchPool.length - 1), Arrays.copyOf(resultPool, resultPool.length));
        }
        Uuring[] extResultPool = new Uuring[resultPool.length + 1];
        System.arraycopy(resultPool, 0, extResultPool, 0, resultPool.length);
        extResultPool[extResultPool.length - 1] = searchPool[searchPool.length - 1];
        Valim inclLast = findValim(Arrays.copyOf(searchPool, searchPool.length - 1), extResultPool);

        if (skipLast == null) {
            return inclLast;
        }
        else if (skipLast.compareTo(inclLast) < 0) {
            return inclLast;
        }
        else {
            return skipLast;
        }
    }
}
