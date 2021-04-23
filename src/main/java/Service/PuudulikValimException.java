package Service;

public class PuudulikValimException extends RuntimeException{
    private int uuringuidPuudu;
    private double hetkeKeskmine;
    public enum exceptionTypes {
        VALIMI_KRITEERIUMID_SEADMATA, SOBIMATU_KAALUKESKMINE, UURINGUTE_MIINIMUM_TÄITMATA
    }
    public PuudulikValimException(exceptionTypes exceptionType) {
        super(String.valueOf(exceptionType));
    }
    public void setUuringuidPuudu(int uuringuidPuudu) {
        this.uuringuidPuudu = uuringuidPuudu;
    }
    public int getUuringuidPuudu() {
        return uuringuidPuudu;
    }
    public double getHetkeKeskmine() {
        return hetkeKeskmine;
    }
    public void setHetkeKeskmine(double hetkeKeskmine) {
        this.hetkeKeskmine = hetkeKeskmine;
    }
}
