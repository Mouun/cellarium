package controlers;

import classes.Locker;
import classes.User;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DrawerControler {

    public JFXToggleButton toggleView;
    private MainPanelControler parent;

    @FXML
    public void openAddBottleBDD(MouseEvent mouseEvent) throws IOException {
        Stage primaryStage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/addWine.fxml"));
        primaryStage.setTitle("Connexion");
        primaryStage.setScene(new Scene(root, AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void addLocker(MouseEvent mouseEvent) throws IOException {
        parent.addCasier(new Locker(User.active));
    }

    public void setParentController(MainPanelControler con)
    {
        parent = con;
    }

    public void testAddBottleLocker(ActionEvent actionEvent) throws IOException {
        parent.testAddBottleLocker(actionEvent);
    }
    public void leaveCellarium(MouseEvent mouseEvent) {
        parent.synchronise();
        System.exit(0);
    }

    public void delBottle(ActionEvent actionEvent) throws SQLException {
        parent.delBottle();
    }

    public void switchViews(ActionEvent actionEvent) {
        parent.switchViews();
    }
}
