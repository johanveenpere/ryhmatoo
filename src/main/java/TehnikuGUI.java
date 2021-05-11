import Model.NimmelülidUuring;
import Model.PeaNatiivUuring;
import Model.RindkereUuring;
import Model.Uuring;
import Repository.UuringRepository;
import Service.Kriteerium;
import Service.Valim;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TehnikuGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage peaLava) throws Exception, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Map<String,Class<? extends Uuring>> uuringutüübid = new HashMap<>();
        uuringutüübid.put("Rindkere PA",RindkereUuring.class);
        uuringutüübid.put("Nimmelülid AP/LAT",NimmelülidUuring.class);
        uuringutüübid.put("Pea KT natiiv",PeaNatiivUuring.class);

        Süsteemiliides sl = new Süsteemiliides("default");
        Uuring nimme = new NimmelülidUuring("HTYK12212", 80.0);
        Uuring rinna = new RindkereUuring("HTYK143412", 80.0);
        Uuring pea = new PeaNatiivUuring("HTYK143412", 80.0);
        sl.UusUuring("HTYK12212", 80.0,NimmelülidUuring.class);
        sl.UusUuring("HTYK143412", 80.0,RindkereUuring.class);
        sl.UusUuring("HTYK143416", 80.0,PeaNatiivUuring.class);

        VBox juur = new VBox();
        juur.setPadding(new Insets(5, 5, 5, 5));

        /**
         * Esimene paan Seaded nupuga
         */
        BorderPane esimene = new BorderPane();
        esimene.setPadding(new Insets(10, 0, 0, 0));

        Button seaded = new Button("Seaded");
        esimene.setLeft(seaded);

        juur.getChildren().add(esimene);

        /**
         * Teine paan seadme nimetusega
         */
        BorderPane teine = new BorderPane();
        teine.setPadding(new Insets(10, 0, 0, 0));
        String seadmenimetus = "tukforce1";

        Label seadmenimetussilt = new Label("Seadme nimetus: " + seadmenimetus);
        teine.setLeft(seadmenimetussilt);

        juur.getChildren().add(teine);

        /**
         * Kolmas paan sisestuse väljadega
         */
        BorderPane kolmas = new BorderPane();
        kolmas.setPadding(new Insets(10, 0, 0, 0));

        GridPane sisestus = new GridPane();
        sisestus.setHgap(5);

        Label uuringunimetus = new Label("Uuringu nimetus");
        Label accnr = new Label("Accession number");
        Label kaal = new Label("Kaal");
        sisestus.addRow(0, uuringunimetus, accnr, kaal);

        ObservableList<String> options = FXCollections.observableArrayList(uuringutüübid.keySet());
        ComboBox<String> uuringunimetused = new ComboBox<>(options);
        uuringunimetused.setPrefWidth(230);

        TextField accnrsisestus = new TextField("HTYK");
        accnrsisestus.setPrefWidth(230);

        TextField kaalsisestus = new TextField("0.0");
        kaalsisestus.setPrefWidth(100);

        Button lisa = new Button("Lisa");
        lisa.setPrefWidth(70);

        sisestus.addRow(1, uuringunimetused, accnrsisestus, kaalsisestus, lisa);

        kolmas.setTop(sisestus);

        juur.getChildren().add(kolmas);

        /**
         * Neljas paan sisestatud uuringute tabeliga
         */
        BorderPane neljas = new BorderPane();
        neljas.setPadding(new Insets(20, 0, 0, 0));

        BorderPane päis1 = new BorderPane();
        päis1.setPadding(new Insets(0, 0, 5, 0));

        Label viimatisisestatud = new Label("Viimati sisestatud uuringud");
        Button kustuta = new Button("Kustuta");

        päis1.setLeft(viimatisisestatud);
        päis1.setRight(kustuta);

        neljas.setTop(päis1);

        ScrollPane rullitav1 = new ScrollPane();
        rullitav1.setPrefHeight(100);
        rullitav1.setFitToWidth(true);

        TableView sisestatuduuringud = new TableView();
        sisestatuduuringud.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Uuring, String> veerg1 = new TableColumn<>("Uuringu nimetus");
        veerg1.setCellValueFactory(new PropertyValueFactory<>("uuringunimetus"));

        TableColumn<Uuring, String> veerg2 = new TableColumn<>("Accession number");
        veerg2.setCellValueFactory(new PropertyValueFactory<>("viit"));

        TableColumn<Uuring, String> veerg3 = new TableColumn<>("Kaal");
        veerg3.setCellValueFactory(new PropertyValueFactory<>("kaal"));

        sisestatuduuringud.getColumns().addAll(veerg1, veerg2, veerg3);

        sisestatuduuringud.getItems().add(nimme);
        sisestatuduuringud.getItems().add(rinna);
        sisestatuduuringud.getItems().add(pea);

        rullitav1.setContent(sisestatuduuringud);
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

        TableView valimitestaatustabel = new TableView();
        valimitestaatustabel.setPrefHeight(100);
        valimitestaatustabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Valim, String> veerg4 = new TableColumn<>("Uuringu nimetus");
        veerg4.setCellValueFactory(new PropertyValueFactory<>("uuringunimetus"));

        TableColumn<Valim, Integer> veerg5 = new TableColumn<>("Valimis");
        veerg5.setCellValueFactory(new PropertyValueFactory<>("suurus"));

        TableColumn<Valim, Integer> veerg6 = new TableColumn<>("Ootel");
        veerg6.setCellValueFactory(new PropertyValueFactory<>("ootel"));

        TableColumn<Valim, Double> veerg7 = new TableColumn<>("Kesk. kaal");
        veerg7.setCellValueFactory(new PropertyValueFactory<>("keskKaal"));

        TableColumn<Valim, String> veerg8 = new TableColumn<>("Olek");
        veerg8.setCellValueFactory(new PropertyValueFactory<>("staatus"));

        valimitestaatustabel.getColumns().addAll(veerg4, veerg5, veerg6,veerg7,veerg8);

        valimitestaatustabel.getItems().add(new Valim(sl.getDb().getAllUuringud(NimmelülidUuring.class)));
        valimitestaatustabel.getItems().add(new Valim(sl.getDb().getAllUuringud(RindkereUuring.class)));
        valimitestaatustabel.getItems().add(new Valim(sl.getDb().getAllUuringud(PeaNatiivUuring.class)));

        viies.setBottom(valimitestaatustabel);

        juur.getChildren().add(viies);

        /**
         * Nupulevajutuse käsitleja
         */
        lisa.setOnMouseClicked(me -> {
            String valik = uuringunimetused.getSelectionModel().selectedItemProperty().getValue();
            String accnrväljund = accnrsisestus.getText();
            double kaalväljund = Double.parseDouble(kaalsisestus.getText());
            Class<? extends Uuring> klass = uuringutüübid.get(valik);
            Class[] cArg = new Class[2];
            cArg[0] = String.class;
            cArg[1] = double.class;
            Object[] iArg = new Object[2];
            iArg[0] = accnrväljund;
            iArg[1] = kaalväljund;
            Constructor constructor = null;
            try {
                constructor = klass.getDeclaredConstructor(cArg);
                constructor.newInstance(iArg);
                System.out.println("Tegi ära");
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); //tuleb teha midagi
            }
        });


//        /**
//         * Teine paan seadme nimetusega
//         */
//        BorderPane keskmine = new BorderPane();
//        keskmine.setPadding(new Insets(10, 0, 0, 0));
//
//        Label eur = new Label("EUR");
//        eur.setFont(new Font(15));
//        eur.setPadding(new Insets(5, 0, 0, 5));
//        keskmine.setLeft(eur);
//
//        HBox hBox = new HBox();
//        hBox.setPrefWidth(140);
//        ObservableList<String> options = FXCollections.observableArrayList(kursid.keySet());
//        ComboBox<String> valuutad = new ComboBox<>(options);
//        valuutad.getSelectionModel().selectFirst();
//        hBox.getChildren().add(valuutad);
//        keskmine.setRight(hBox);
//
//        juur.getChildren().add(keskmine);
//
//        /**
//         * Alumine paan kahe tekstiväljaga
//         */
//        BorderPane alumine = new BorderPane();
//        alumine.setPadding(new Insets(5, 2, 5, 2));
//
//        TextField eurod = new TextField("0");
//        eurod.setPrefWidth(140);
//        alumine.setLeft(eurod);
//
//        TextField valuuta = new TextField("0");
//        valuuta.setPrefWidth(140);
//        valuuta.setEditable(false);
//        alumine.setRight(valuuta);
//
//        juur.getChildren().add(alumine);

        /**
         * Stseen ja show
         */
        Scene stseen1 = new Scene(juur, 640, 480, Color.AZURE);
        peaLava.setScene(stseen1);
        peaLava.setTitle("Dooside kogumise rakendus");
        peaLava.setResizable(false);
        peaLava.show();
    }
}
