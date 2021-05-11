import Model.NimmelülidUuring;
import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SüsteemiliidesTest {

    static EntityManagerFactory emf;
    static Süsteemiliides liides;
    static UuringRepository db;

    @BeforeAll
    static void setUp(){
        emf = Persistence.createEntityManagerFactory("default");
        db = new UuringRepository(emf);
        liides = new Süsteemiliides(db);
    }

    @AfterAll
    static void tearDown(){
        emf.close();
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

    @Test
    void teeKokkuvõte() {
        OffsetDateTime test = OffsetDateTime.of(2020,1,1,1,1,1,1, ZoneOffset.UTC);
        Kriteerium kriteerium = new Kriteerium(80,60,65,1,5, test);
        liides.teeKokkuvõte("testKokkuvõtted", NimmelülidUuring.class, kriteerium);
    }
}