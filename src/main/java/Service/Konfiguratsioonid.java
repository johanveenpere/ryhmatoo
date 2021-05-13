package Service;

import Model.NimmelülidUuring;
import Model.PeaNatiivUuring;
import Model.RindkereUuring;
import Model.Uuring;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Konfiguratsioonid {
    private Map<String, Class<? extends Uuring>> nimedklassidmap;
    private Map<Class<? extends Uuring>, String> klassidnimedmap;
    private Map<Class<? extends Uuring>, Kriteerium> klassidkriteeriumidmap;

    public Konfiguratsioonid(String modaliteet) {
        switch (modaliteet) {
            case "KT" -> {
                nimedklassidmap = new HashMap<>();
                nimedklassidmap.put("Pea KT natiiv", PeaNatiivUuring.class);

                klassidnimedmap = new HashMap<>();
                klassidnimedmap.put(PeaNatiivUuring.class, "Pea KT natiiv");

                klassidkriteeriumidmap = new HashMap<>();
                klassidkriteeriumidmap.put(PeaNatiivUuring.class, new Kriteerium(200, 30, 75, 100, 10, LocalDate.parse("2020-04-01")));
            }
            case "RG" -> {
                nimedklassidmap = new HashMap<>();
                nimedklassidmap.put("Rindkere PA", RindkereUuring.class);
                nimedklassidmap.put("Nimmelülid AP/LAT", NimmelülidUuring.class);

                klassidnimedmap = new HashMap<>();
                klassidnimedmap.put(RindkereUuring.class, "Rindkere PA");
                klassidnimedmap.put(NimmelülidUuring.class, "Nimmelülid AP/LAT");

                klassidkriteeriumidmap = new HashMap<>();
                klassidkriteeriumidmap.put(RindkereUuring.class, new Kriteerium(85, 65, 75, 3, 10, LocalDate.parse("2020-04-01")));
                klassidkriteeriumidmap.put(NimmelülidUuring.class, new Kriteerium(85, 65, 75, 3, 10, LocalDate.parse("2020-04-01")));
            }
        }
    }

    public Konfiguratsioonid() {
        nimedklassidmap = new HashMap<>();
        nimedklassidmap.put("Rindkere PA", RindkereUuring.class);
        nimedklassidmap.put("Nimmelülid AP/LAT", NimmelülidUuring.class);
        nimedklassidmap.put("Pea KT natiiv", PeaNatiivUuring.class);

        klassidnimedmap = new HashMap<>();
        klassidnimedmap.put(RindkereUuring.class, "Rindkere PA");
        klassidnimedmap.put(NimmelülidUuring.class, "Nimmelülid AP/LAT");
        klassidnimedmap.put(PeaNatiivUuring.class, "Pea KT natiiv");

        klassidkriteeriumidmap = new HashMap<>();
        klassidkriteeriumidmap.put(RindkereUuring.class, new Kriteerium(85, 65, 75, 3, 10, LocalDate.parse("2020-04-01")));
        klassidkriteeriumidmap.put(NimmelülidUuring.class, new Kriteerium(85, 65, 75, 3, 10, LocalDate.parse("2020-04-01")));
        klassidkriteeriumidmap.put(PeaNatiivUuring.class, new Kriteerium(200, 30, 75, 100, 10, LocalDate.parse("2020-04-01")));
    }

    public Map<String, Class<? extends Uuring>> getNimedklassidmap() {
        return nimedklassidmap;
    }

    public Map<Class<? extends Uuring>, String> getKlassidnimedmap() {
        return klassidnimedmap;
    }

    public Map<Class<? extends Uuring>, Kriteerium> getKlassidkriteeriumidmap() {
        return klassidkriteeriumidmap;
    }
}
