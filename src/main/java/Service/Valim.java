package Service;

import Model.Uuring;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Valim implements Comparable<Valim>{
    private final Class<?> uuringType;
    private final Kriteerium kriteerium;
    private final List<Uuring> uuringud;

    private double keskKaal = 0;
    private double hälve = 0;
    private int suurus = 0;

    private boolean vastabKriteeriumitele = false;
    private boolean inMõõtemääramatus = false;
    private boolean miinimumTäidetud = false;

    public Valim(Kriteerium kriteerium, Class<?> uuringType, List<Uuring> uuringud) {
        this.kriteerium = kriteerium;
        this.uuringType = uuringType;
        this.uuringud = uuringud;
        if (uuringud != null && uuringud.size() != 0) {
            Collections.sort(this.uuringud);

            this.keskKaal = Math.round(uuringud.stream().mapToDouble(Uuring::getKaal).sum() / uuringud.size()*100) / (double) 100;
            this.hälve = Math.round(Math.abs(kriteerium.getKeskKaal() - this.keskKaal)*100) / (double) 100;
            this.suurus = uuringud.size();

            this.inMõõtemääramatus = this.hälve < this.kriteerium.getMootemaaramatus();
            this.miinimumTäidetud = this.suurus >= this.kriteerium.getMinValim();
            this.vastabKriteeriumitele = this.inMõõtemääramatus && this.miinimumTäidetud;
        }
    }

    public Class<?> getUuringType() {
        return uuringType;
    }

    public Kriteerium getKriteerium() {
        return kriteerium;
    }

    public List<Uuring> getUuringud() {
        return uuringud;
    }

    public double getKeskKaal() {
        return keskKaal;
    }

    public double getHälve() {
        return hälve;
    }

    public int getSuurus() {
        return suurus;
    }

    public boolean isVastabKriteeriumitele() {
        return vastabKriteeriumitele;
    }

    public boolean isInMõõtemääramatus() {
        return inMõõtemääramatus;
    }

    public boolean isMiinimumTäidetud() {
        return miinimumTäidetud;
    }

    @Override
    public String toString() {
        String header = "Uuring=" + this.uuringType.getSimpleName() + ", " + kriteerium.toString() + ", uuringuid=" + this.suurus + ", keskmine=" + this.keskKaal + ", hälve=" + this.hälve + ", vastab kriteeriumitele=" + this.vastabKriteeriumitele + "\n";
        String uuringud;
        if (this.uuringud != null) {
            uuringud = this.uuringud.stream().map(Uuring::simpleString).collect(Collectors.joining(", \n"));
        }
        else {
            uuringud = "";
        }
        return header + uuringud;
    }

    @Override
    public int compareTo(Valim o) {
        int miinimumTäidetud = Boolean.compare(this.miinimumTäidetud,o.miinimumTäidetud);
        if (miinimumTäidetud == 0) {
            if (!this.miinimumTäidetud) {
                int suurus = Integer.compare(this.suurus,o.suurus);
                if (suurus != 0) {
                    return suurus;
                }
            }
            return Double.compare(o.hälve, this.hälve);
        }
        else {
            return miinimumTäidetud;
        }
    }
}