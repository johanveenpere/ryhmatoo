package Service;

import java.time.OffsetDateTime;

public class Kriteerium {
    private final double maxKaal;
    private final double minKaal;
    private final double keskKaal;
    private final OffsetDateTime alguskuupäev;

    private final double mootemaaramatus;
    private final int minValim;

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

    public OffsetDateTime getAlguskuupäev() {
        return alguskuupäev;
    }

    public Kriteerium(double maxKaal, double minKaal, double keskKaal, double mootemaaramatus, int minValim, OffsetDateTime alguskuupäev) {
        this.maxKaal = maxKaal;
        this.minKaal = minKaal;
        this.keskKaal = keskKaal;
        this.mootemaaramatus = mootemaaramatus;
        this.minValim = minValim;
        this.alguskuupäev = alguskuupäev;
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
