import Model.Uuring;
import Repository.TühiUuringulistException;
import Service.Kriteerium;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class SeadedGUI extends VBox implements Runnable {
    private final Süsteemiliides süsteemiliides;
    private Preferences prefs;
    private ComboBox<String> modaliteetvalik;
    private TextField seadmenimisisestus;
    private Label teade1;
    private Label teade2;
    private String valitudpath;
    private String failinimi;
    private final String modaliteetpref = "modaliteedivalik";
    private final String seadmenimipref = "seadmenimi";

    public SeadedGUI(Süsteemiliides süsteemiliides) {
        this.süsteemiliides = süsteemiliides;
    }

    @Override
    public void run() {
        prefs = Preferences.userNodeForPackage(this.getClass());

        TabPane tabPane = new TabPane();

        /**
         * Sisestuse tab
         */
        Tab sisestustab = new Tab();
        sisestustab.setText("Sisestus");
        sisestustab.setClosable(false);

        List<String> modaliteedid = new ArrayList<>();
        modaliteedid.add("RG");
        modaliteedid.add("KT");
        ObservableList<String> options = FXCollections.observableArrayList(modaliteedid);

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5, 20, 5, 20));
        gridpane.setHgap(20);

        Label modaliteetvaliksilt = new Label("Modaliteet:");
        modaliteetvaliksilt.setPadding(new Insets(5, 0, 5, 0));
        gridpane.add(modaliteetvaliksilt, 0, 0);

        modaliteetvalik = new ComboBox<>(options);
        modaliteetvalik.setPrefWidth(200);
        modaliteetvalik.setValue(prefs.get(modaliteetpref, modaliteedid.get(0)));
        gridpane.add(modaliteetvalik, 0, 1);

        Label seadmenimisilt = new Label("Seadme nimetus:");
        seadmenimisilt.setPadding(new Insets(10, 0, 5, 0));
        gridpane.add(seadmenimisilt, 0, 2);

        seadmenimisisestus = new TextField();
        seadmenimisisestus.setPrefWidth(200);
        seadmenimisisestus.setText(prefs.get(seadmenimipref, ""));
        gridpane.add(seadmenimisisestus, 0, 3);

        Button salvestanupp = new Button("Salvesta");
        BorderPane bp = new BorderPane();
        bp.setPrefWidth(150);
        bp.setRight(salvestanupp);
        gridpane.add(bp, 1, 3);

        teade1 = new Label("");
        teade1.setPadding(new Insets(10, 0, 0, 0));
        gridpane.add(teade1, 0, 4);

        sisestustab.setContent(gridpane);
        tabPane.getTabs().add(sisestustab);

        /**
         * Kokkuvõtte tab
         */

        DirectoryChooser kaustavalija = new DirectoryChooser();
        kaustavalija.setInitialDirectory(new File("src"));

        Tab kokkuvõtetab = new Tab();
        kokkuvõtetab.setText("Kokkuvõte");
        kokkuvõtetab.setClosable(false);

        VBox suurkast = new VBox();
        suurkast.setPadding(new Insets(10, 20, 5, 20));

        BorderPane esimenepaan = new BorderPane();
        esimenepaan.setPadding(new Insets(0, 0, 5, 0));

        Label kaustasilt = new Label("Vali kaust:");
        esimenepaan.setLeft(kaustasilt);

        suurkast.getChildren().add(esimenepaan);

        BorderPane teinepaan = new BorderPane();
        teinepaan.setPadding(new Insets(0, 0, 5, 0));

        TextField kaustasisestus = new TextField();
        kaustasisestus.setPrefWidth(280);
        teinepaan.setLeft(kaustasisestus);

        suurkast.getChildren().add(teinepaan);

        BorderPane kolmaspaan = new BorderPane();
        kolmaspaan.setPadding(new Insets(0, 0, 5, 0));

        Button valikaustnupp = new Button("Vali kaust");
        kolmaspaan.setRight(valikaustnupp);

        Label failinimisilt = new Label("Sisesta faili nimi:");
        kolmaspaan.setLeft(failinimisilt);

        suurkast.getChildren().add(kolmaspaan);

        BorderPane neljaspaan = new BorderPane();
        neljaspaan.setPadding(new Insets(0, 0, 5, 0));

        TextField failinimisisestus = new TextField();
        failinimisisestus.setPrefWidth(280);
        neljaspaan.setLeft(failinimisisestus);

        suurkast.getChildren().add(neljaspaan);

        BorderPane viiespaan = new BorderPane();
        viiespaan.setPadding(new Insets(0, 0, 5, 0));

        Button salvestakokkuvõtenupp = new Button("Salvesta");
        viiespaan.setRight(salvestakokkuvõtenupp);

        teade2 = new Label();
        viiespaan.setLeft(teade2);

        suurkast.getChildren().add(viiespaan);

        kokkuvõtetab.setContent(suurkast);

        tabPane.getTabs().add(kokkuvõtetab);

        getChildren().add(tabPane);

        /**
         * Handlerid
         */
        salvestanupp.setOnMouseClicked(me -> {
            prefs.put(modaliteetpref, modaliteetvalik.getValue());
            prefs.put(seadmenimipref, seadmenimisisestus.getText());
            teade1.setTextFill(Color.GREEN);
            teade1.setText("Salvestatud!");
        });

        valikaustnupp.setOnMouseClicked(me -> {
            try {
                File selectedDirectory = kaustavalija.showDialog(new Stage());
                valitudpath = selectedDirectory.getAbsolutePath();
                kaustasisestus.setText(valitudpath);
            } catch (NullPointerException e){
                //kui kausta ei valita
            }
        });

        salvestakokkuvõtenupp.setOnMouseClicked(me -> {
            if (isVäljadKorras())
                try {
                    File csvOutputFile = new File(valitudpath + "\\" + failinimi);
                    KokkuvõtteKoostaja.teeCSV(süsteemiliides.getDb().getAllUuringud(Uuring.class),csvOutputFile);
                    teade2.setTextFill(Color.GREEN);
                    teade2.setText("Salvestatud!");
                } catch (TühiUuringulistException e) {
                    teade2.setTextFill(Color.RED);
                    teade2.setText("Andmebaas on tühi!");
                } catch (IOException e) {
                    teade2.setTextFill(Color.RED);
                    teade2.setText("Ei õnnestunud salvestada!");
                }
        });

        kaustasisestus.textProperty().addListener((observable, oldValue, newValue) -> {
            teade2.setText("");
            valitudpath = newValue;
        });

        failinimisisestus.textProperty().addListener((observable, oldValue, newValue) -> {
            teade2.setText("");
            failinimi = newValue;
            if (failinimi.length() >= 5 && !failinimi.substring(failinimi.length() - 4).equalsIgnoreCase(".csv"))
                failinimi = failinimi + ".csv";
        });


    }

    private boolean isVäljadKorras() {
        if (valitudpath == null || valitudpath.isEmpty()) {
            teade2.setText("Sisesta kausta asukoht!");
            return false;
        }
        try {
            Paths.get(valitudpath);
        } catch (InvalidPathException | NullPointerException e) {
            teade2.setText("Täpsusta kausta asukoht!");
            return false;
        }
        if (failinimi == null || failinimi.isEmpty()) {
            teade2.setText("Täpsusta failinimi!");
            return false;
        }
        try {
            Paths.get(valitudpath + "\\" + failinimi);
        } catch (InvalidPathException | NullPointerException e) {
            teade2.setText("Failinimi ei sobi!");
            return false;
        }
        return true;
    }
}
