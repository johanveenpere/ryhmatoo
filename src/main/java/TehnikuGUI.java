import Model.Uuring;
import Repository.TühiUuringulistException;
import Service.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.persistence.EntityExistsException;
import java.util.*;
import java.util.List;
import java.util.prefs.*;

public class TehnikuGUI extends Application {
    private Preferences prefs;
    private final String modaliteetpref = "modaliteedivalik";
    private final String seadmenimipref = "seadmenimi";

    private Süsteemiliides süsteemiliides;
    private Konfiguratsioonid konfiguratsioonid;
    private Label seadmenimetussilt;
    private ComboBox<String> uuringunimetusedvalik;
    private ObservableList<Valim> valimidstaatuslist;
    private ObservableList<Uuring> viimaseduuringudlist;
    private ObservableList<String> uuringuvaliklist;
    private Label teade;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage peaLava) {
        seadmenimetussilt = new Label();
        uuringunimetusedvalik = new ComboBox<>();
        valimidstaatuslist = FXCollections.observableArrayList();
        viimaseduuringudlist = FXCollections.observableArrayList();

        prefs = Preferences.userNodeForPackage(SeadedGUI.class);
        süsteemiliides = new Süsteemiliides("default");
        uuendaOlek();

        /**
         * Juur
         */
        VBox juur = new VBox();
        juur.setPadding(new Insets(5, 5, 5, 5));

        /**
         * Esimene paan Seaded nupu ja olekukuvaga
         */
        BorderPane esimene = new BorderPane();
        esimene.setPadding(new Insets(10, 0, 0, 0));

        Button seaded = new Button("Seaded");
        esimene.setLeft(seaded);

        teade = new Label();
        teade.setPadding(new Insets(0, 20, 0, 0));
        esimene.setRight(teade);

        juur.getChildren().add(esimene);

        /**
         * Teine paan seadme nimetusega
         */
        BorderPane teine = new BorderPane();
        teine.setPadding(new Insets(10, 0, 0, 0));

        teine.setLeft(seadmenimetussilt);

        juur.getChildren().add(teine);

        /**
         * Kolmas paan sisestuse väljadega
         */
        BorderPane kolmas = new BorderPane();
        kolmas.setPadding(new Insets(10, 0, 0, 0));

        GridPane sisestus = new GridPane();
        sisestus.setHgap(5);

        Label uuringunimetussilt = new Label("Uuringu nimetus");
        Label viitsilt = new Label("Accession number");
        Label kaalsilt = new Label("Kaal");
        sisestus.addRow(0, uuringunimetussilt, viitsilt, kaalsilt);

        uuringunimetusedvalik.setPrefWidth(230);

        TextField viitsisestus = new TextField("HTYK");
        viitsisestus.setPrefWidth(230);

        TextField kaalsisestus = new TextField("0.0");
        kaalsisestus.setPrefWidth(100);

        Button lisanupp = new Button("Lisa");
        lisanupp.setPrefWidth(70);

        sisestus.addRow(1, uuringunimetusedvalik, viitsisestus, kaalsisestus, lisanupp);

        kolmas.setTop(sisestus);

        juur.getChildren().add(kolmas);

        /**
         * Neljas paan sisestatud uuringute tabeliga
         */
        BorderPane neljas = new BorderPane();
        neljas.setPadding(new Insets(20, 0, 0, 0));

        BorderPane päis1 = new BorderPane();
        päis1.setPadding(new Insets(0, 0, 5, 0));

        Label viimatisisestatudsilt = new Label("Viimati sisestatud uuringud");
        Button kustutanupp = new Button("Kustuta");

        päis1.setLeft(viimatisisestatudsilt);
        päis1.setRight(kustutanupp);

        neljas.setTop(päis1);

        ScrollPane rullitav1 = new ScrollPane();
        rullitav1.setPrefHeight(100);
        rullitav1.setFitToWidth(true);

        TableView<Uuring> sisestatuduuringudtabel = new TableView<>();
        sisestatuduuringudtabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Uuring, String> veerg1 = new TableColumn<>("Uuringu nimetus");
        veerg1.setCellValueFactory(new PropertyValueFactory<>("uuringunimetus"));
        sisestatuduuringudtabel.getColumns().add(veerg1);

        TableColumn<Uuring, String> veerg2 = new TableColumn<>("Accession number");
        veerg2.setCellValueFactory(new PropertyValueFactory<>("viit"));
        sisestatuduuringudtabel.getColumns().add(veerg2);

        TableColumn<Uuring, String> veerg3 = new TableColumn<>("Kaal");
        veerg3.setCellValueFactory(new PropertyValueFactory<>("kaal"));
        sisestatuduuringudtabel.getColumns().add(veerg3);

        sisestatuduuringudtabel.setItems(viimaseduuringudlist);

        rullitav1.setContent(sisestatuduuringudtabel);
        neljas.setBottom(rullitav1);

        juur.getChildren().add(neljas);

        /**
         * Viies paan valimite staatusega
         */
        BorderPane viies = new BorderPane();
        viies.setPadding(new Insets(20, 0, 0, 0));

        BorderPane päis2 = new BorderPane();
        päis2.setPadding(new Insets(0, 0, 5, 0));

        Label valimitestaatus = new Label("Valimite staatus");
        Button värskenda = new Button("Värskenda");

        päis2.setLeft(valimitestaatus);
        päis2.setRight(värskenda);

        viies.setTop(päis2);

        TableView<Valim> valimitestaatustabel = new TableView<Valim>();
        valimitestaatustabel.setPrefHeight(100);
        valimitestaatustabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Valim, String> veerg4 = new TableColumn<>("Uuringu nimetus");
        veerg4.setCellValueFactory(new PropertyValueFactory<>("uuringunimetus"));
        valimitestaatustabel.getColumns().add(veerg4);

        TableColumn<Valim, Integer> veerg5 = new TableColumn<>("Valimis");
        veerg5.setCellValueFactory(new PropertyValueFactory<>("suurus"));
        valimitestaatustabel.getColumns().add(veerg5);

        TableColumn<Valim, Integer> veerg6 = new TableColumn<>("Ootel");
        veerg6.setCellValueFactory(new PropertyValueFactory<>("ootel"));
        valimitestaatustabel.getColumns().add(veerg6);

        TableColumn<Valim, Double> veerg7 = new TableColumn<>("Kesk. kaal");
        veerg7.setCellValueFactory(new PropertyValueFactory<>("keskKaal"));
        valimitestaatustabel.getColumns().add(veerg7);

        TableColumn<Valim, String> veerg8 = new TableColumn<>("Olek");
        veerg8.setCellValueFactory(new PropertyValueFactory<>("staatus"));
        valimitestaatustabel.getColumns().add(veerg8);

        valimitestaatustabel.setItems(valimidstaatuslist);

        viies.setBottom(valimitestaatustabel);

        juur.getChildren().add(viies);

        /**
         * "Lisa" nupule vajutuse käsitleja
         */
        lisanupp.setOnMouseClicked(me -> {
            String uuringuvalik = uuringunimetusedvalik.getSelectionModel().selectedItemProperty().getValue();
            String viitväljund = viitsisestus.getText();
            prefs.put("viimaneacc", viitväljund);
            double kaalväljund;
            if (uuringuvalik != null) {
                Class<? extends Uuring> klass = konfiguratsioonid.getNimedklassidmap().get(uuringuvalik);
                try {
                    kaalväljund = Double.parseDouble(kaalsisestus.getText().replace(",","."));
                    süsteemiliides.UusUuring(viitväljund, kaalväljund, klass);
                    kontrolliSisestus(süsteemiliides.getDb().getUuring(viitväljund));
                    uuendaTabelid();
                    viitsisestus.setText("HTYK");
                    kaalsisestus.setText("0.0");
                    uuringunimetusedvalik.setValue("");
                    teade.setTextFill(Color.GREEN);
                    teade.setText("Uuring \"" + viitväljund + "\" lisatud!");
                } catch (NumberFormatException e) {
                    teade.setTextFill(Color.RED);
                    teade.setText("Kaal \"" + kaalsisestus.getText() + "\" on vale vormistusega!");
                } catch (EntityExistsException e) {
                    teade.setTextFill(Color.RED);
                    teade.setText("Uuring \"" + viitväljund + "\" on juba andmebaasis!");
                } catch (SisestusVormiException e) {
                    teade.setTextFill(Color.RED);
                    teade.setText("Uuringut ei õnnestunud lisada: " + e.getMessage());
                    süsteemiliides.getDb().removeUuring(süsteemiliides.getDb().getUuring(viitväljund));
                } catch (Exception e) {
                    teade.setTextFill(Color.RED);
                    teade.setText("Uuringut ei õnnestunud lisada!");
                }
            } else {
                teade.setTextFill(Color.RED);
                teade.setText("Uuringu nimetus peab olema valitud!");
            }
        });

        /**
         * "Värskenda" nupule vajutuse käsitleja
         */
        värskenda.setOnMouseClicked(me -> {
            valimiteUuendus(valimidstaatuslist, süsteemiliides);
        });

        /**
         * "Kustuta" nupule vajutuse käsitleja
         */
        kustutanupp.setOnMouseClicked(me -> {
            Uuring uuring = sisestatuduuringudtabel.getSelectionModel().getSelectedItem();
            if (uuring != null) {
                süsteemiliides.getDb().removeUuring(uuring);
                uuringuteUuendus(viimaseduuringudlist, süsteemiliides);
                valimiteUuendus(valimidstaatuslist, süsteemiliides);
                teade.setTextFill(Color.GREEN);
                teade.setText("Uuring " + uuring.getViit() + " kustutatud!");
            }
        });

        /**
         * "Seaded" nupule vajutuse käsitleja
         */
        seaded.setOnMouseClicked(me -> {
            peaLava.hide();
            Stage seadedaken = new Stage();
            seadedaken.setTitle("Seaded");
            SeadedGUI seadedGUI = new SeadedGUI();
            seadedGUI.run();
            Scene stseen2 = new Scene(seadedGUI, 320, 150, Color.AZURE);
            seadedaken.setScene(stseen2);
            seadedaken.setResizable(false);
            seadedaken.showAndWait();
            prefs = Preferences.userNodeForPackage(SeadedGUI.class);
            uuendaOlek();
            peaLava.show();
        });

        /**
         * Stseen ja show
         */
        Scene stseen1 = new Scene(juur, 640, 480, Color.AZURE);
        peaLava.setScene(stseen1);
        peaLava.setTitle("Dooside kogumise rakendus");
        peaLava.setResizable(false);
        peaLava.show();
    }

    private ObservableList<Valim> valimiteUuendus(ObservableList<Valim> valimid, Süsteemiliides sl) {
        if (valimid.size() != 0)
            valimid.removeAll(valimid);
        for (Map.Entry<String, Class<? extends Uuring>> klass : konfiguratsioonid.getNimedklassidmap().entrySet()) {
            Class klassivalik = klass.getValue();
            Kriteerium kriteerium = konfiguratsioonid.getKlassidkriteeriumidmap().get(klassivalik);
            try {
                Valim valim = new ValimiSelekteerija(klassivalik, sl.getEmf(), kriteerium).getValim();
                List<Uuring> uuringud = valim.getUuringud();
                valim.setUuringunimetus(konfiguratsioonid.getKlassidnimedmap().get(klassivalik));
                valim.setOotel(uuringud.size() - (int) uuringud.stream().filter(Uuring::isTäidetud).count());
                valim.setStaatus("Valim on koos");
                valimid.add(valim);
            } catch (TühiUuringulistException e) {
                // ei tee midagi
            } catch (PuudulikValimException e) {
                Valim valim = e.getValim();
                List<Uuring> uuringud = valim.getUuringud();
                valim.setUuringunimetus(konfiguratsioonid.getKlassidnimedmap().get(klassivalik));
                valim.setOotel(uuringud.size() - (int) uuringud.stream().filter(Uuring::isTäidetud).count());
                if (e.getMessage().equals("SOBIMATU_KAALUKESKMINE")) {
                    if (valim.getKeskKaal() < kriteerium.getKeskKaal())
                        valim.setStaatus("Keskmine kaal on madal");
                    else
                        valim.setStaatus("Keskmine kaal on kõrge");
                }
                if (e.getMessage().equals("UURINGUTE_MIINIMUM_TÄITMATA"))
                    valim.setStaatus("Miinimum täitmata");
                valimid.add(valim);
            }
        }
        return valimid;
    }

    private ObservableList<Uuring> uuringuteUuendus(ObservableList<Uuring> viimatisisestatud, Süsteemiliides sl) {
        if (viimatisisestatud.size() != 0)
            viimatisisestatud.removeAll(viimatisisestatud);
        try {
            viimatisisestatud.addAll(sl.getDb().getViimasedUuringud(3));
        } catch (TühiUuringulistException e) {
            // ei tee midagi
        }
        return viimatisisestatud;
    }

    private void kontrolliSisestus(Uuring uuring) throws SisestusVormiException {
        String viit = uuring.getViit();
        Class klass = uuring.getClass();
        Kriteerium kriteerium = konfiguratsioonid.getKlassidkriteeriumidmap().get(klass);
        int pikkusekriteerium = 16;
        double minkaal = kriteerium.getMinKaal();
        double maxkaal = kriteerium.getMaxKaal();
        double kaal = uuring.getKaal();

        if (viit.length() >= 16)
            throw new SisestusVormiException("Accession nr " + viit + " ei ole õige pikkusega (" + pikkusekriteerium+ " tähemärki)");
        if (kaal < minkaal || kaal > maxkaal)
            throw new SisestusVormiException("Sisestatud kaal " + kaal + " ei ole lubatud vahemikus (" + minkaal + " kuni " + maxkaal + " kg)");
    }

    private void uuendaOlek() {
        konfiguratsioonid = new Konfiguratsioonid(prefs.get(modaliteetpref,"RG"));
        uuringuvaliklist = FXCollections.observableArrayList(konfiguratsioonid.getNimedklassidmap().keySet());
        uuringunimetusedvalik.setItems(uuringuvaliklist);
        seadmenimetussilt.setText("Seadme nimetus: " + prefs.get(seadmenimipref,"default"));
        uuendaTabelid();
    }

    private void uuendaTabelid(){
        valimiteUuendus(valimidstaatuslist, süsteemiliides);
        uuringuteUuendus(viimaseduuringudlist, süsteemiliides);
    }
}
