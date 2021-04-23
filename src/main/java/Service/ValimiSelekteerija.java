package Service;
import Model.Uuring;
import Repository.UuringRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Service.PuudulikValimException.exceptionTypes.SOBIMATU_KAALUKESKMINE;
import static Service.PuudulikValimException.exceptionTypes.UURINGUTE_MIINIMUM_TÄITMATA;

public class ValimiSelekteerija <T extends Uuring> {
    private Class<T> uuringType;
    private EntityManagerFactory emf;
    private double maxKaal;
    private double minKaal;
    private double keskKaal;
    private double mootemaaramatus;
    private int minValim;
    // indeksid: 1 = keskmine, 2 = hälve
    private double[] bestValimiKandidaat = new double[2];

    public ValimiSelekteerija(EntityManagerFactory emf, Class<T> uuringType, double maxKaal, double minKaal, double keskKaal, int minValim, double mootemaaramatus) {
        this.emf = emf;
        this.uuringType = uuringType;
        this.maxKaal = maxKaal;
        this.minKaal = minKaal;
        this.keskKaal = keskKaal;
        this.minValim = minValim;
        this.mootemaaramatus = mootemaaramatus;
    }
    public List<Uuring> getValim() throws PuudulikValimException {
        EntityManager em = emf.createEntityManager();
        try {
            UuringRepository repo = new UuringRepository(this.emf,em);
            List<Uuring> valim = repo.getValimiKandidaadid(uuringType,minKaal,maxKaal);
            Collections.sort(valim);
            /*
            int index = 1;
            for (Uuring uuring : valim) {
                System.out.println(index + ". " + uuring.toString());
                ++index;
            }
            System.out.println("=".repeat(20));
            */
            if (valim.size() < minValim) {
                PuudulikValimException e = new PuudulikValimException(UURINGUTE_MIINIMUM_TÄITMATA);
                double sum = valim.stream().mapToDouble(Uuring::getKaal).sum();
                e.setUuringuidPuudu(minValim - valim.size());
                e.setHetkeKeskmine((float) (sum/valim.size()));
                throw e;
            }
            Uuring[] leitudValim = findValim(valim.toArray(new Uuring[valim.size()]), new Uuring[0]);
            if (leitudValim == null) {
                PuudulikValimException e = new PuudulikValimException(SOBIMATU_KAALUKESKMINE);
                e.setHetkeKeskmine(this.bestValimiKandidaat[0]);
                throw e;
            }
            else {
                return Arrays.asList(leitudValim);
            }
        }
        finally {
            em.close();
        }
    }
    private Uuring[] findValim(Uuring[] searchPool, Uuring[] resultPool) {
        // Kui otsitav valim ongi miinimumsuurusega
        if (searchPool.length + resultPool.length == minValim) {
            resultPool = Stream.concat(Arrays.stream(searchPool),Arrays.stream(resultPool)).toArray(Uuring[]::new);
        }
        // Kui pole enam millegi seast otsida
        if (searchPool.length == 0) {
            double keskmine = Stream.of(resultPool).mapToDouble(Uuring::getKaal).sum() / resultPool.length;
            double hälve = Math.abs(keskKaal - keskmine);
            boolean piirides = hälve < this.mootemaaramatus;
            if (piirides) {
                return resultPool;
            }
            else {
                if (this.bestValimiKandidaat[1] > hälve | this.bestValimiKandidaat[0] == 0) {
                    this.bestValimiKandidaat[0] = keskmine;
                    this.bestValimiKandidaat[1] = hälve;
                }
                return null;
            }
        }
        Uuring[] skipLast = null;
        if (searchPool.length >= minValim) {
            skipLast = findValim(Arrays.copyOf(searchPool, searchPool.length - 1), Arrays.copyOf(resultPool, resultPool.length));
        }
        Uuring[] extResultPool = new Uuring[resultPool.length + 1];
        System.arraycopy(resultPool, 0, extResultPool, 0, resultPool.length);
        extResultPool[extResultPool.length - 1] = searchPool[searchPool.length - 1];
        Uuring[] inclLast = findValim(Arrays.copyOf(searchPool, searchPool.length - 1), extResultPool);

        if (skipLast == null && inclLast == null) {
            return null;
        }
        else if (skipLast != null && inclLast != null) {
            if (inclLast.length < skipLast.length) {
                return inclLast;
            }
            else {
                return skipLast;
            }
        }
        else if (skipLast == null) {
            return inclLast;
        }
        else {
            return skipLast;
        }
    }
}
