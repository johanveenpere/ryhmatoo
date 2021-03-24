import javax.net.ssl.HttpsURLConnection;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//katsetamiseks powershell kood:
//try{invoke-webrequest -method get -uri "http://dicomserver.co.uk:81/qido/studies/?00100010=CT" -headers @{ accept = "application/json"} } catch{ $PSItem.exception.response}

public class HttpsServeriühendus implements Serveriühendus{

    URL serveriAadress;

    public HttpsServeriühendus(URL serveriAadress) {
        this.serveriAadress = serveriAadress;
    }

    public HttpsServeriühendus(String serveriAadress) throws MalformedURLException { //viskab erindi, sest ei oska ise seda parandada
        this.serveriAadress = new URL(serveriAadress);
    }

    @Override
    public Andmed SaaMetaandmed(String pildiviit) {
        try{
            HttpURLConnection serveriühendus = (HttpURLConnection) serveriAadress.openConnection();
            serveriühendus.setRequestMethod("GET");
            serveriühendus.setRequestProperty("Accept", "application/dicom");
            InputStream in = serveriühendus.getInputStream();
            System.out.println("printing");
            OutputStream out = new FileOutputStream("test.txt");
            out.write(in.readAllBytes());
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
