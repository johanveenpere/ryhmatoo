public class PuudulikValimException extends Exception{
    private int uuringuidPuudu;
    private float hetkeKeskmine;
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
    public float getHetkeKeskmine() {
        return hetkeKeskmine;
    }
    public void setHetkeKeskmine(float hetkeKeskmine) {
        this.hetkeKeskmine = hetkeKeskmine;
    }
}
