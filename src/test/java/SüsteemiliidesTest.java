import Model.NimmelülidUuring;
import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;


class SüsteemiliidesTest {

    static EntityManagerFactory emf;
    static Süsteemiliides liides;
    static UuringRepository db;

    @BeforeAll
    static void setUp(){
        var em = Persistence.createEntityManagerFactory("default").createEntityManager();
        db = new UuringRepository(em);
        liides = new Süsteemiliides(db);
    }

    @AfterAll
    static void tearDown(){
    }

    @Test
    void uusUuring() {
        String viit = "PiLdIvIiT";
        try {
            liides.UusUuring(viit, 100.0, NimmelülidUuring.class);
            assertNotNull(db.getUuring(viit));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    final String võrdlusFail = "NimmelülidUuring\n" +
            "Kuupäev, Seade, Viit, Kaal, Vanus, Sugu, DAP_AP, DSP_AP, DAP_LL, DSP_LL\n" +
            "2021-05-16T21:05:54, null, PiLdIvIiT, 100.0, 0, null, 0.0, 0.0, 0.0, 0.0\n";

    @Test
    void teeKokkuvõte() throws IOException {
        var test = LocalDateTime.of(2020,1,1,0,0);
        Kriteerium kriteerium = new Kriteerium(80.0,60.0,65.0,1.0,5, test);
        liides.teeKokkuvõte(new File("testKokkuvõtted"), NimmelülidUuring.class);
    }
}