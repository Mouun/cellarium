package controlers;

import classes.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.CellariumUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static util.CellariumUtil.createNewStage;
import static util.CellariumUtil.displayLabel;

public class LoginControler implements Initializable {

    @FXML
    private Label lblStatus;
    @FXML
    private JFXTextField textUsr;
    @FXML
    private JFXPasswordField textPwd;

    /**
     * Permet la connexion à la base de données
     * @param event
     * @throws SQLException Si la connexion à la base de données ne réussie pas
     */
    @FXML
    public void login(ActionEvent event) throws SQLException {
        Boolean tmp = User.login(textUsr.getText(), textPwd.getText());
        if (tmp) {
            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            displayLabel(lblStatus, Pos.CENTER, Paint.valueOf("#388e3c"), "Connexion réussie");
            exec.schedule(new Runnable() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader(CellariumUtil.class.getResource("/fxml/mainPanel.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            stage.setTitle("Cellarium");
                            stage.setScene(new Scene(root, 1300, 900));
                            stage.setMinWidth(1300);
                            stage.setMinHeight(830);
                            stage.setMaximized(true);
                            stage.setResizable(true);
                            stage.getIcons().add(new Image("res/img/icon.png"));
                            stage.show();

                            stage.setOnHidden(new EventHandler<WindowEvent>() {
                                @Override
                                public void handle(WindowEvent event) {
                                    ((MainPanelControler)loader.getController()).synchronise();
                                    System.exit(0);
                                }
                            });

                            Window theStage = lblStatus.getScene().getWindow();
                            theStage.hide();
                        }
                    });
                }
            }, 1, TimeUnit.SECONDS);


        } else {
            displayLabel(lblStatus, Pos.CENTER, Paint.valueOf("#d32f2f"), "L'e-mail ou le mot de passe est incorrect");
        }
    }

    @FXML
    public void addMember(ActionEvent ae) throws IOException {

        Node source = (Node) ae.getSource();
        Window theStage = source.getScene().getWindow();
        theStage.hide();

        createNewStage("/fxml/addMember.fxml", "Inscription", false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textUsr.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        login(new ActionEvent());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        textPwd.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        login(new ActionEvent());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
