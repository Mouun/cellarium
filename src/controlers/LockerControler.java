package controlers;

import classes.Bottle;
import classes.BottleCircle;
import classes.Couleurs;
import classes.Locker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import util.CellariumUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LockerControler implements Initializable {

    @FXML
    private JFXTextField lockerName;
    @FXML
    private Label nbWhite;
    @FXML
    private Label nbRed;
    @FXML
    private Label nbRose;
    @FXML
    private HBox hboxLocker;
    @FXML
    private Locker locker;
    @FXML
    private MainPanelControler mainPanelControler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Permet d'ajouter une bouteille au composant HBox du casier
     * @param bottle La bouteille à ajouter
     */
    public void addBottle(Bottle bottle) {
        hboxLocker.getChildren().add(bottle.getCircle());
    }

    /**
     * Permet d'augmenter les compteurs des vins dans les casiers
     * @param counts Tableau d'entiers contenant les nouvelles valeurs à afficher
     */
    public void updateCount(int[] counts) {
        nbWhite.setText(Integer.toString(counts[Couleurs.Blanc.ordinal()]));
        nbRed.setText(Integer.toString(counts[Couleurs.Rouge.ordinal()]));
        nbRose.setText(Integer.toString(counts[Couleurs.Rose.ordinal()]));
    }

    /**
     *
     * @param listener
     */
    public void addTextListener(ChangeListener<String> listener) {
        lockerName.textProperty().addListener(listener);
    }

    /**
     * Permet d'affecter un nom à un casier
     * @param name Le nom du casier
     */
    public void setName(String name) {
        lockerName.setText(name);
    }

    /**
     * Permet d'assigner un casier à un contrôleur de casier
     * @param l Le casier à assigner
     */
    public void setLocker(Locker l) {
        locker = l;
    }

    /**
     * Permet de renseigner le contrôleur parent du contrôleur actuel
     * @param controler Le contrôleur parent
     */
    public void setMainPanelControler(MainPanelControler controler) {
        mainPanelControler = controler;
    }

    /**
     * Permet de supprimer un casier
     * @param mouseEvent
     */
    public void closeLocker(MouseEvent mouseEvent) {
        if (locker.isEmpty()) {
            try {
                locker.delete();
                mainPanelControler.removeLocker(locker);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            CellariumUtil.displayJFXDialog(mainPanelControler.getStackPane(), "Attention !", "Le casier que vous voulez supprimer contient des bouteilles. Supprimez les bouteilles avant de supprimer le casier", "Okay");
        }
    }

    /**
     * Permet de supprimer une bouteille d'un casier
     * @param b Le bouteille à retirer
     */
    public void removeBottle(Bottle b) {
        Circle circle = null;
        for (Node c : hboxLocker.getChildren()) {
            if (((BottleCircle) c).getBottle() == b) {
                circle = (BottleCircle) c;
                break;
            }
        }
        if (circle != null) hboxLocker.getChildren().remove(circle);
    }
}
