package controlers;

import bdd.Connexion;
import classes.Bottle;
import classes.Couleurs;
import classes.Locker;
import classes.User;
import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXTreeView;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.CellariumUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPanelControler implements Initializable, IBarcodeReceiver {

    @FXML
    public Label ficheBouteilles;
    public Label region;
    public Label appellationtype;
    public Label appellation;
    public Label couleur;
    public Label cepages;
    public Label garde;
    public JFXMasonryPane mansoryPane;
    public Label caveName;
    public Label nbRose;
    public Label nbRouge;
    public Label nbBlanc;
    public Label lblRegion;
    public Label lblDegres;
    public Label lblVolume;
    public Label lblMillesime;
    public JFXTreeView<String> treeView;
    public ScrollPane scrollPane;
    public Label nbBottleGlobal;

    @FXML
    private JFXListView<Bottle> listView;

    @FXML
    private FontAwesomeIconView hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private ImageView imgVin;

    @FXML
    private Label lblDomaine;

    @FXML
    private Label lblAppellationType;

    @FXML
    private Label lblAppellation;

    @FXML
    private Label lblCouleur;

    @FXML
    private Label lblCepages;

    @FXML
    private StackPane stackPane;

    private List<Locker> lockers = new ArrayList<>();

    public Locker getTargetLocker() {
        return targetLocker;
    }

    public void setTargetLocker(Locker targetLocker) {
        this.targetLocker = targetLocker;
    }

    private Locker targetLocker;

    private int statusView;

    public MainPanelControler() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        statusView = 1;
        treeView.toBack();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/drawer.fxml"));
            AnchorPane boxMaCave = loader.load();
            drawer.setSidePane(boxMaCave);
            ((DrawerControler)loader.getController()).setParentController(this);

        } catch (IOException ex) {
            Logger.getLogger(MainPanelControler.class.getName()).log(Level.SEVERE, null, ex);
        }

        caveName.setText("Cave de " + User.active.getNom());

        drawer.toBack();

        try {
            User.active.loadLockers().forEach((Locker l)-> {
                try {
                    addCasier(l);
                    l.loadBottles();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

//        final TreeItem<String> fruitItem = new TreeItem<>("Fruits");
//        fruitItem.getChildren().setAll(
//                new TreeItem("Fraise"),
//                new TreeItem("Pomme"),
//                new TreeItem("Poire")
//        );

        generateListView();
    }

    private void generateListView() {
        ArrayList<String> lockersNames = new ArrayList<>();
        for (Locker l : lockers) {
            lockersNames.add(l.getNom());
        }

        ArrayList<TreeItem<String>> nodesFirstLevel = new ArrayList<>();
        for (String lockersName : lockersNames) {
            TreeItem<String> treeItem = new TreeItem(lockersName);
            treeItem.setExpanded(true);
            nodesFirstLevel.add(treeItem);
        }

        for (TreeItem<String> aNodesFirstLevel : nodesFirstLevel) {
            setRootChildren(aNodesFirstLevel);
        }

        TreeItem<String> treeRoot = new TreeItem<>(caveName.getText());
        treeRoot.setExpanded(true);
        treeRoot.getChildren().addAll(nodesFirstLevel);
        treeView.setRoot(treeRoot);
    }

    public Locker getLockerFromName(String lockerName) {
        for (Locker l : lockers) {
            if (l.getNom().equals(lockerName)) return l;
        }
        return null;
    }

    private void setRootChildren(TreeItem<String> treeItem) {
        Locker l = getLockerFromName(treeItem.getValue());
        ArrayList<String> temp = getLockerChildren(l);
        for (String s : temp) {
            treeItem.getChildren().add(new TreeItem<>(s));
        }
    }

    private ArrayList<String> getLockerChildren(Locker l) {
        ArrayList<String> lockerChildren = new ArrayList<>();
        for (Bottle b : l.getBottles()) {
            lockerChildren.add(b.toString());
        }

        return lockerChildren;
    }

    private Bottle highlighted = null;

    @FXML
    void afficherFiche(MouseEvent event) throws SQLException {
        refreshFiche();
    }

    private void refreshFiche() throws SQLException
    {
        if (highlighted != null) highlighted.dehighlightCircle();

        Bottle selectedItem = listView.getSelectionModel().getSelectedItem();

        selectedItem.highlightCircle();
        changerInfos(selectedItem);
        highlighted = selectedItem;
    }

    @FXML
    void openConnexion(MouseEvent event) throws IOException {
        CellariumUtil.createNewStage("/fxml/login.fxml", "Connexion", false);
    }

    private void changerInfos(Bottle b) throws SQLException {
        lblDomaine.setText(b.getVin().getDomaine());
        lblRegion.setText("region");
        lblAppellationType.setText("AOP");
        lblAppellation.setText(b.getVin().getAppellation());
        lblCouleur.setText(b.getVin().getCouleur().toString());
        lblCepages.setText(b.getVin().getCepage());
        lblDegres.setText(b.getVin().getDegres() + "°");
        lblVolume.setText(b.getVolume() + "cl");
        lblMillesime.setText(String.valueOf(b.getMillesime()));
    }

    public void openDrawer(MouseEvent event) {
        /*HamburgerBasicCloseTransition transition = new HamburgerBasicCloseTransition(hamburger);
        transition.setRate(-1);*/


        if (drawer.isShown()) {
            drawer.close();
            drawer.toBack();

            /*hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                transition.setRate(transition.getRate() * -1);
                transition.play();
            });*/

        } else {
            drawer.toFront();
            drawer.open();
        }
    }

    @FXML
    private void settings(MouseEvent e) throws IOException {

        CellariumUtil.createNewStage("/fxml/settings.fxml", "Parametres", false);
    }

    public void addCasier(Locker l) throws IOException {
        l.setMainPanelControler(this);
        mansoryPane.getChildren().add(l);
        lockers.add(l);
        generateListView();
    }

    public void removeLocker(Locker l)
    {
        if(l.getBottles().size() > 0){
            CellariumUtil.displayJFXDialog(stackPane,"Attention","Supprimez d'abord les bouteilles du casier !","okay");
        }
        else{
            mansoryPane.getChildren().remove(l);
            lockers.remove(l);
            generateListView();
        }

    }

    public List<Locker> getLockers()
    {
        return lockers;
    }

    public void testCamera(ActionEvent actionEvent) throws IOException {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            CellariumUtil.displayJFXDialog(stackPane, "Attention !", "Veuillez brancher une webcam.", "Okay");
        } else {
            CellariumUtil.createNewStage("/fxml/cameraPreview.fxml", "Previsualisation camera", false);

            /*webcam.open();
            BarcodeReaderWebcam reader = new BarcodeReaderWebcam(webcam);

            System.out.println(reader.readBarcodeSafe(true));
            webcam.close();*/
        }
    }

    public void testGeolocalisation(ActionEvent actionEvent) throws IOException, GeoIp2Exception {
        CellariumUtil.createNewStage("/fxml/map.fxml", "Cave aux alentours", false);
    }

    public void testAddBottleLocker(ActionEvent actionEvent) throws IOException {
        if (lockers.isEmpty())
        {
            CellariumUtil.displayJFXDialog(stackPane, "Attention!", "Vous n'avez pas de casier pour ajouter de bouteilles", "Okay");
        }
        else
        {
            Stage primaryStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/addBottle.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Ajouter une bouteille");
            primaryStage.setScene(new javafx.scene.Scene(root, AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE));
            primaryStage.setResizable(false);
            primaryStage.show();
            ((AddBottleController)loader.getController()).setParentController(this);
        }
    }

    public void addBottle(Bottle bottle, Locker l)
    {
        l.addBottle(bottle);
        listView.getItems().add(bottle);
        couleurCount[bottle.getVin().getCouleur().ordinal()]++;
        updateCount();
        generateListView();
    }

    private int[] couleurCount = new int[Couleurs.size];

    private void updateCount()
    {
        nbBlanc.setText(Integer.toString(couleurCount[Couleurs.Blanc.ordinal()]));
        nbRouge.setText(Integer.toString(couleurCount[Couleurs.Rouge.ordinal()]));
        nbRose.setText(Integer.toString(couleurCount[Couleurs.Rose.ordinal()]));

        int total = 0;
        for (int i : couleurCount) {
            total += i;
        }
        nbBottleGlobal.setText(Integer.toString(total));
    }

    public void testSync(ActionEvent actionEvent) {
        synchronise();
    }

    public void synchronise()
    {
        lockers.forEach((Locker l)-> {
            try {
                l.synchronize();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void geolocalisation(ActionEvent actionEvent) {
        CellariumUtil.createNewStage("/fxml/map.fxml", "Cave aux alentours", false);
    }

    public void removeBottle(Bottle b, Connexion con) throws SQLException {
        Locker locker = null;
        for(Locker l : lockers){
            if(l.getBottles().contains(b)){
                locker =l;
                break;
            }
        }
        if(locker!= null){
            locker.removeBottle(b,con);
            listView.getItems().remove(b);
            couleurCount[b.getVin().getCouleur().ordinal()]--;
            updateCount();
            generateListView();
        }
    }

    public void delBottle() throws SQLException {
        Bottle selectedItem = listView.getSelectionModel().getSelectedItem();
        removeBottle(selectedItem, new Connexion());
    }

    public void switchViews() {
        if(statusView == 1) {
            treeView.toFront();
            statusView = 2;
        }

        else if(statusView == 2) {
            treeView.toBack();
            statusView = 1;
        }
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void supprBouteilleScan(ActionEvent actionEvent) {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            CellariumUtil.displayJFXDialog(stackPane, "Attention !", "Veuillez brancher une webcam.", "Okay");
        } else {
            CellariumUtil.createWebcamCaptureStage(this);
        }
    }

    @Override
    public void setBarcode(String barcode) {
        //Bottle found = listView.getItems().stream().filter(bottle -> bottle.getBarcode().equalsIgnoreCase(barcode)).findFirst().orElse(null);

        Bottle found = null;

        for (Bottle b : listView.getItems())
        {
            if (b.getBarcode() != null && b.getBarcode().equalsIgnoreCase(barcode))
            {
                found = b;
                break;
            }
        }

        if (found == null)
        {
            CellariumUtil.displayJFXDialog(stackPane, "Attention !", "Aucune bouteille comportant ce code barre n'a été trouvée.", "Okay");
        }
        else
        {
            listView.getSelectionModel().select(found);
            try {
                refreshFiche();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
