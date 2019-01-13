package controlers;

import bdd.Connexion;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import classes.User;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static util.CellariumUtil.*;

public class AddMemberControler {

    @FXML
    private JFXTextField textID;

    @FXML
    private JFXTextField textSurname;

    @FXML
    private JFXTextField textFirstname;

    @FXML
    private JFXPasswordField textPwd;

    @FXML
    private JFXTextField textMail;

    @FXML
    private JFXTextField textCity;

    @FXML
    private JFXDatePicker date;

    @FXML
    private Label lblStatus;

    @FXML
    void subscription(ActionEvent event) {
        if (textID.getText() == null || textSurname.getText() == null || textFirstname.getText() == null || date.getValue() == null || textPwd.getText() == null || textMail.getText() == null || textCity.getText() == null) {
            displayLabel(lblStatus, Pos.CENTER, Paint.valueOf("#d32f2f"), "Veuillez renseigner tous les champs");

        } else {
            displayLabel(lblStatus, Pos.CENTER, Paint.valueOf("#388e3c"), "Votre compte a été créé");
            new Thread(() -> {
                User.newUser(Objects.requireNonNull(Connexion.connexionBDD()), textID.getText(), textSurname.getText(), textFirstname.getText(), date.getValue().toString(), textPwd.getText(), textMail.getText(), textCity.getText());
            }).start();
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            exec.schedule(new Runnable() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            createNewStage("/fxml/login.fxml", "Connexion", false);
                            Node source = (Node) event.getSource();
                            Window theStage = source.getScene().getWindow();
                            theStage.hide();
                        }
                    });
                }
            }, 1, TimeUnit.SECONDS);
        }
    }

    public void connexion(ActionEvent ae) throws IOException {
        Node source = (Node) ae.getSource();
        Window theStage = source.getScene().getWindow();
        theStage.hide();
        createNewStage("/fxml/login.fxml", "Inscription", false);
    }
}
