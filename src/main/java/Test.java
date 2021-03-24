import com.pixelmed.validate.DicomSRValidator;

import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws Exception {
        C_OP_Serveriühendus ühendus = new C_OP_Serveriühendus();
        ühendus.SaaMetaandmed("viit");
    }
}
