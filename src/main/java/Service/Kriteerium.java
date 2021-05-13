package Service;

import Model.Uuring;

import java.time.LocalDate;

public class Kriteerium {
    private double maxKaal = 0;
    private double minKaal = 0;
    private LocalDate alguskuupäev = null;

    private final double keskKaal;
    private final double mootemaaramatus;
    private final int minValim;

    private boolean täitmataUuringud = true;
    private boolean täidetudUuringud = true;

    public boolean isTäitmataUuringud() {
        return täitmataUuringud;
    }

    public boolean isTäidetudUuringud() {
        return täidetudUuringud;
    }

    public double getMaxKaal() {
        return maxKaal;
    }

    public double getMinKaal() {
        return minKaal;
    }

    public double getKeskKaal() {
        return keskKaal;
    }

    public double getMootemaaramatus() {
        return mootemaaramatus;
    }

    public int getMinValim() {
        return minValim;
    }

    public LocalDate getAlguskuupäev() {
        return alguskuupäev;
    }

    public void setMaxKaal(double maxKaal) {
        this.maxKaal = maxKaal;
    }

    public void setMinKaal(double minKaal) {
        this.minKaal = minKaal;
    }

    public void setAlguskuupäev(LocalDate alguskuupäev) {
        this.alguskuupäev = alguskuupäev;
    }

    public void setTäitmataUuringud(boolean täitmataUuringud) {
        this.täitmataUuringud = täitmataUuringud;
    }

    public void setTäidetudUuringud(boolean täidetudUuringud) {
        this.täidetudUuringud = täidetudUuringud;
    }

    public Kriteerium(double keskKaal, double mootemaaramatus, int minValim) {
        this.keskKaal = keskKaal;
        this.mootemaaramatus = mootemaaramatus;
        this.minValim = minValim;
    }

    public Kriteerium(double maxKaal, double minKaal, double keskKaal, double mootemaaramatus, int minValim) {
        this(keskKaal,mootemaaramatus,minValim);
        this.maxKaal = maxKaal;
        this.minKaal = minKaal;
    }

    public Kriteerium(double maxKaal, double minKaal, double keskKaal, double mootemaaramatus, int minValim, LocalDate alguskuupäev) {
        this(maxKaal,minKaal,keskKaal,mootemaaramatus,minValim);
        this.alguskuupäev = alguskuupäev;
    }

    public boolean uuringVastabKriteeriumile(Uuring uuring) {
        boolean vastabMinKaaluKriteeriumile = uuring.getKaal() >= this.minKaal;
        boolean vastabMaxKaaluKriteeriumile = uuring.getKaal() <= this.maxKaal || this.maxKaal == 0;
        boolean vastabTäidetudKriteeriumile = (this.täidetudUuringud && this.täitmataUuringud) || (this.täidetudUuringud && uuring.isTäidetud()) || (this.täitmataUuringud && !uuring.isTäidetud());
        boolean vastabAlgusKuupäevaKriteeriumile = this.alguskuupäev == null || this.alguskuupäev.isBefore(uuring.getLoomisaeg());
        return vastabMinKaaluKriteeriumile && vastabMaxKaaluKriteeriumile && vastabTäidetudKriteeriumile && vastabAlgusKuupäevaKriteeriumile;
    }

    @Override
    public String toString() {
        return "Kriteerium{" +
                "maxKaal=" + maxKaal +
                ", minKaal=" + minKaal +
                ", keskKaal=" + keskKaal +
                ", mootemaaramatus=" + mootemaaramatus +
                ", minValim=" + minValim +
                '}';
    }
}
