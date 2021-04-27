import Model.PeaNatiivUuring;
import Service.Kriteerium;
import Service.Valim;
import Service.ValimiSelekteerija;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Süsteemiliides {
   public void SisestaUuring(String pildiviit, double kaal){
        //salvestada andmebaasi pildiviit, kaal
   }

   public void TeeKokkuvõte(String failitee){
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("andmed");
      OffsetDateTime test = OffsetDateTime.of(2020,1,1,1,1,1,1, ZoneOffset.UTC);
      Kriteerium kriteerium = new Kriteerium(80,60,65,1,5, test);
      ValimiSelekteerija selekteerija = new ValimiSelekteerija(PeaNatiivUuring.class, emf, kriteerium);
      Valim valim = selekteerija.getValim();
      try {
         KokkuvõtteKoostaja.teeCSV(valim.getUuringud(), failitee);
      }
      catch(IOException e){
         e.printStackTrace();
      }
   }

   //private void
}
