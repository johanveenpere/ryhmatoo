package Service;
import Model.Uuring;
import Repository.UuringRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
            UuringRepository repo = new UuringRepository(em);
            Valim sorteerimataValim = new Valim(kriteerium, uuringType, repo.getByKriteerium(uuringType, kriteerium));

            if (!sorteerimataValim.isMiinimumTäidetud()) {
                throw new PuudulikValimException(UURINGUTE_MIINIMUM_TÄITMATA, sorteerimataValim);
            }
            Valim sorteeritudValim;
            // Väikse valimi puhul kasuta kombinatsioone
            if (sorteerimataValim.getSuurus() < 50) {
                sorteeritudValim = findValim(sorteerimataValim.getUuringud().toArray(new Uuring[0]), new Uuring[0]);
            }
            // Suure valimi puhul kasuta optimiseerimist
            else {
                Valim[][] valimTable = new Valim[(int) Math.round(kriteerium.getMaxKaal() - kriteerium.getMinKaal() + 1)][sorteerimataValim.getSuurus()];
                sorteeritudValim = findValim(valimTable, sorteerimataValim.getUuringud(), 1);
            }

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
        if (searchPool.length + resultPool.length <= this.kriteerium.getMinValim()) {
            resultPool = Stream.concat(Arrays.stream(searchPool),Arrays.stream(resultPool)).toArray(Uuring[]::new);
            searchPool = new Uuring[0];
        }
        if (searchPool.length == 0) {
            Valim valim = new Valim(this.kriteerium,this.uuringType,Arrays.asList(resultPool));
            return valim;
        }
        Valim skipLast = null;
        if (searchPool.length + resultPool.length > this.kriteerium.getMinValim()) {
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

    private Valim findValim(Valim[][] valimTable, List<Uuring> uuringud, int setsize) {
        Collections.sort(uuringud);
        if (setsize == 1) {
            for (int i = 0; i < valimTable.length; i++) {
                Uuring parimUuring = uuringud.get(uuringud.size()-1);
                for (int j = 0; j < uuringud.size(); j++) {
                    if (j != uuringud.size()-1) {
                        int kumbParem = Double.compare(Math.abs(uuringud.get(j + 1).getKaal() - (i + kriteerium.getMinKaal())), Math.abs(uuringud.get(j).getKaal() - (i + kriteerium.getMinKaal())));
                        if (kumbParem > 0) {
                            parimUuring = uuringud.get(j);
                            break;
                        }
                    }
                }
                valimTable[i][0] = new Valim(new Kriteerium(kriteerium.getMaxKaal(), kriteerium.getMinKaal(), i + kriteerium.getMinKaal(), kriteerium.getMootemaaramatus(), kriteerium.getMinValim(), kriteerium.getAlguskuupäev()),this.uuringType, Collections.singletonList(parimUuring));
            }
            return findValim(valimTable,uuringud,++setsize);
        }
        else {
            for (int i = 0; i < valimTable.length; i++) {
                Kriteerium iterKriteerium = new Kriteerium(kriteerium.getMaxKaal(), kriteerium.getMinKaal(), Math.round(i + kriteerium.getMinKaal()), kriteerium.getMootemaaramatus(), kriteerium.getMinValim(), kriteerium.getAlguskuupäev());
                double sum = setsize*(kriteerium.getMinKaal() + i);
                Valim bestValim = new Valim(iterKriteerium,this.uuringType,new ArrayList<>());
                for (Uuring uuring : uuringud) {
                    double complementSum = sum - uuring.getKaal();
                    double complementAvg = complementSum / (setsize - 1);

                    int lowerBound = (int) (complementAvg - kriteerium.getMootemaaramatus() - kriteerium.getMinKaal() - 1);
                    int upperBound = (int) (complementAvg + kriteerium.getMootemaaramatus() - kriteerium.getMinKaal() + 1);
                    if (lowerBound < 0) {
                        lowerBound = 0;
                    }
                    if (upperBound > valimTable.length-1) {
                        upperBound = valimTable.length - 1;
                    }

                    for (int j = lowerBound; j < upperBound; j++) {
                        List<Uuring> iterUuringud = new ArrayList<>();
                        iterUuringud.addAll(valimTable[j][setsize - 2].getUuringud());
                        if (iterUuringud.contains(uuring)) {
                            continue;
                        }
                        iterUuringud.add(uuring);
                        Valim iterValim = new Valim(iterKriteerium,this.uuringType, iterUuringud);
                        if (bestValim.getSuurus() == 0 || bestValim.getHälve() > iterValim.getHälve()) {
                            bestValim = iterValim;
                        }
                    }
                }
                if (bestValim.getKriteerium().getKeskKaal() == this.kriteerium.getKeskKaal() && bestValim.isVastabKriteeriumitele()) {
                    return new Valim(this.kriteerium,this.uuringType,bestValim.getUuringud());
                }
                valimTable[i][setsize-1] = bestValim;
            }
        }
        if (setsize < valimTable[0].length) {
            return findValim(valimTable,uuringud,++setsize);
        }
        else {
            Valim bestValim = new Valim(this.kriteerium,this.uuringType,new ArrayList<>());
            for (int i = -1; i < 2; i++) {
                for (Valim valim : valimTable[(int) Math.round(this.kriteerium.getKeskKaal() - this.kriteerium.getMinKaal()) + i]) {
                    Valim iterValim = new Valim(this.kriteerium,this.uuringType,valim.getUuringud());
                    if (iterValim.compareTo(bestValim) >= 0) {
                        bestValim = iterValim;
                    }
                }
            }
            return bestValim;
        }
    }
}
