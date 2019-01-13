package controlers;

import bdd.Attribut;
import classes.Bottle;
import classes.Locker;
import classes.Wine;
import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import util.CellariumUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AddBottleController implements Initializable, IBarcodeReceiver {

    @FXML
    public JFXTextField fieldBarcode;

    @FXML
    public StackPane stackPaneAddBottle;

    @FXML
    public JFXTextField fieldWine;

    @FXML
    public JFXTextField fieldVolume;

    @FXML
    public JFXButton buttonLink;

    @FXML
    public Label lblStatus;

    @FXML
    public JFXButton buttonValidate;

    @FXML
    public JFXTextField fieldMillesime;

    @FXML
    public JFXComboBox comboBoxLockers;

    @FXML
    private MainPanelControler parent;

    @FXML
    private HashMap<String, Wine> wines;

    /**
     * Méthode qui se lance à chaque début d'initialisation de la classe
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wines = new HashMap<>();
        try {
            String[] possibleWines = new String[0];
            Set<Wine> wineSet = Attribut.getVinSet();
            List<String> list = new ArrayList<>();

            wineSet.forEach((Wine w)->{
                wines.put(w.toString(), w);
                list.add(w.toString());
            });
            possibleWines = list.toArray(possibleWines);
            TextFields.bindAutoCompletion(fieldWine, possibleWines);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param con
     */
    public void setParentController(MainPanelControler con)
    {
        parent = con;

        comboBoxLockers.getItems().clear();
        parent.getLockers().forEach((Locker l)->comboBoxLockers.getItems().add(l.getNom()));
    }

    public void addBottle(ActionEvent actionEvent) {
        Wine vin = wines.get(fieldWine.getText());
        if (vin == null)
        {
            CellariumUtil.displayJFXDialog(stackPaneAddBottle, "Attention!", "Le nom de vin n'est pas connu. Ajoutez-le si il n'existe pas puis rééssayez.", "Okay");
            return;
        }

        try
        {
            Bottle bottle = new Bottle(Integer.parseInt(fieldVolume.getText()), Integer.parseInt(fieldMillesime.getText()), fieldBarcode.getText(), vin);
            parent.addBottle(bottle, parent.getLockerFromName(comboBoxLockers.getSelectionModel().getSelectedItem().toString()));
            Stage stage = (Stage) buttonValidate.getScene().getWindow();
            stage.close();
        }
        catch (NumberFormatException e)
        {
            CellariumUtil.displayJFXDialog(stackPaneAddBottle, "Attention!", "Vous avez entré un nombre invalide.", "Okay");
        }
    }

    public void linkBottle(ActionEvent actionEvent) {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            CellariumUtil.displayJFXDialog(stackPaneAddBottle, "Attention !", "Veuillez brancher une webcam.", "Okay");
        } else {
            CellariumUtil.createWebcamCaptureStage(this);
        }
    }

    public void resetFields(ActionEvent actionEvent) {

    }

    @Override
    public void setBarcode(String barcode) {
        fieldBarcode.setText(barcode);
    }
}
