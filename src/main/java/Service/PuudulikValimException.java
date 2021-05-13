package Service;

public class PuudulikValimException extends RuntimeException{
    private final Valim valim;
    public enum exceptionTypes {
        SOBIMATU_KAALUKESKMINE, UURINGUTE_MIINIMUM_TÄITMATA
    }
    public PuudulikValimException(exceptionTypes exceptionType, Valim valim) {
        super(String.valueOf(exceptionType));
        this.valim = valim;
    }
    public Valim getValim() {
        return valim;
    }
}
