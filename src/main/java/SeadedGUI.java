import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class SeadedGUI extends VBox implements Runnable {
    private Preferences prefs;
    private GridPane gridpane;
    private Label modaliteetvaliksilt;
    private ComboBox<String> modaliteetvalik;
    private List<String> modaliteedid;
    private Label seadmenimisilt;
    private TextField seadmenimisisestus;
    private Button salvestanupp;
    private Label teade;
    private final String modaliteetpref = "modaliteedivalik";
    private final String seadmenimipref = "seadmenimi";

    @Override
    public void run() {
        prefs = Preferences.userNodeForPackage(this.getClass());

        setPadding(new Insets(5, 20, 5, 20));

        modaliteedid = new ArrayList<>();
        modaliteedid.add("RG");
        modaliteedid.add("KT");
        ObservableList<String> options = FXCollections.observableArrayList(modaliteedid);

        gridpane = new GridPane();
        gridpane.setHgap(20);

        modaliteetvaliksilt = new Label("Modaliteet:");
        modaliteetvaliksilt.setPadding(new Insets(5, 0, 5, 0));
        gridpane.add(modaliteetvaliksilt,0,0);

        modaliteetvalik = new ComboBox<>(options);
        modaliteetvalik.setPrefWidth(200);
        modaliteetvalik.setValue(prefs.get(modaliteetpref,modaliteedid.get(0)));
        gridpane.add(modaliteetvalik,0,1);

        seadmenimisilt = new Label("Seadme nimetus:");
        seadmenimisilt.setPadding(new Insets(10, 0, 5, 0));
        gridpane.add(seadmenimisilt,0,2);

        seadmenimisisestus = new TextField();
        seadmenimisisestus.setPrefWidth(200);
        seadmenimisisestus.setText(prefs.get(seadmenimipref,""));
        gridpane.add(seadmenimisisestus,0,3);

        salvestanupp = new Button("Salvesta");
        BorderPane bp = new BorderPane();
        bp.setPrefWidth(150);
        bp.setRight(salvestanupp);
        gridpane.add(bp,1,3);

        teade = new Label("");
        teade.setPadding(new Insets(10, 0, 0, 0));
        gridpane.add(teade,0,4);

        getChildren().add(gridpane);

        salvestanupp.setOnMouseClicked(me -> {
            prefs.put(modaliteetpref,modaliteetvalik.getValue());
            prefs.put(seadmenimipref,seadmenimisisestus.getText());
            teade.setTextFill(Color.GREEN);
            teade.setText("Salvestatud!");
        });
    }
}
