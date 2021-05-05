import Model.PeaNatiivUuring;
import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import Service.Valim;
import Service.ValimiSelekteerija;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Süsteemiliides {

   private EntityManagerFactory emf;
   private EntityManager em;
   private UuringRepository db;
   private Kriteerium kriteerium;

   public Süsteemiliides(String andmebaasiNimi){
      EntityManagerFactory emf = Persistence.createEntityManagerFactory(andmebaasiNimi);
      em = emf.createEntityManager();
      db = new UuringRepository(emf, em);
   }

   public Süsteemiliides(EntityManagerFactory emf, EntityManager em){
      db = new UuringRepository(emf, em);
   }

   public Süsteemiliides(UuringRepository db){
      this.db = db;
   }

   public <T extends Uuring> void UusUuring(String pildiviit, double kaal, Class<T> tüüp) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
      //salvestada andmebaasi pildiviit, kaal
      Constructor<T> con = tüüp.getConstructor(String.class, double.class);
      T uuring = con.newInstance(pildiviit, kaal);
      db.addUuring(uuring);
   }

   public void TäidaPuuduvadUuringud(String tempPildikaust){
      List<Uuring> uuringud = new ArrayList<>();//db.getPoolikudUuringud();
      List<String> tõmmatudFailideTeed = new ArrayList<>();
      CGetUuringud serveriühendus = new CGetUuringud();
      for (Uuring uuring : uuringud) {
         tõmmatudFailideTeed.add(serveriühendus.TõmbaUuringud(uuring.getViit(), tempPildikaust));
      }
   }

   public void setKriteerium(Kriteerium kriteerium) {
      this.kriteerium = kriteerium;
   }

   public void TeeKokkuvõte(String failitee, Class<? extends Uuring> tüüp){
      List<Uuring> uuringud = db.getValimiKandidaadid(tüüp, kriteerium);
      try {
         KokkuvõtteKoostaja.teeCSV(uuringud, failitee);
      }
      catch(IOException e){
         e.printStackTrace();
      }
   }

   //private void
}
