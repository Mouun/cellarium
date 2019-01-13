package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import controlers.IBarcodeReceiver;
import controlers.WebCamPreviewController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CellariumUtil {

    public static Stage createNewStage(String fxmlPath, String stageTitle, boolean resizable) {
        Stage primaryStage = new Stage();

        Parent root = null;

        try {
            root = FXMLLoader.load(CellariumUtil.class.getResource(fxmlPath));
            primaryStage.setTitle(stageTitle);
            primaryStage.setScene(new javafx.scene.Scene(root, AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE));
            primaryStage.setResizable(resizable);
            primaryStage.getIcons().add(new Image("res/img/icon.png"));
            primaryStage.show();
            return primaryStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Stage createNewStageWithBaseSize(String fxmlPath, String stageTitle, int width, int height, boolean resizable) {
        Stage primaryStage = new Stage();

        Parent root = null;

        try {
            root = FXMLLoader.load(CellariumUtil.class.getResource(fxmlPath));
            primaryStage.setTitle(stageTitle);
            primaryStage.setScene(new Scene(root, width, height));
            primaryStage.setMinWidth(width);
            primaryStage.setMinHeight(830);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(resizable);
            primaryStage.getIcons().add(new Image("res/img/icon.png"));
            primaryStage.show();
            return primaryStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object createNewStageController(String fxmlPath, String stageTitle, int width, int height, boolean resizable) {
        Stage primaryStage = new Stage();

        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(CellariumUtil.class.getResource(fxmlPath));
            root = loader.load();
            primaryStage.setTitle(stageTitle);
            primaryStage.setScene(new Scene(root, width, height));
            primaryStage.setMinWidth(width);
            primaryStage.setMinHeight(830);
            primaryStage.setMaximized(true);
            primaryStage.setResizable(resizable);
            primaryStage.getIcons().add(new Image("res/img/icon.png"));
            primaryStage.show();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Stage createWebcamCaptureStage(IBarcodeReceiver receiver) {
        Stage primaryStage = new Stage();

        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(CellariumUtil.class.getResource("/fxml/cameraPreview.fxml"));
            root = loader.load();
            primaryStage.setTitle("Previsualisation camera");
            primaryStage.setScene(new javafx.scene.Scene(root, AnchorPane.USE_COMPUTED_SIZE, AnchorPane.USE_COMPUTED_SIZE));
            primaryStage.setResizable(false);
            primaryStage.show();
            ((WebCamPreviewController)loader.getController()).setReceiver(receiver);
            return primaryStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void displayJFXDialog(StackPane stackPane, String heading, String body, String buttonText) {
        stackPane.toFront();
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(heading));
        content.setBody(new Text(body));
        JFXDialog jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
        JFXButton button = new JFXButton(buttonText);
        button.setOnAction(event -> {
            jfxDialog.close();
            stackPane.toBack();
        });
        content.setActions(button);
        jfxDialog.show();
    }

    public static String[] sortClearBOM(String[] tab)
    {
        for (int i=0;i<tab.length;i++) tab[i] = tab[i].replace("\uFEFF", "");

        Arrays.sort(tab);
        //Arrays.sort(tab, Comparator.comparing(a -> Normalizer.normalize(a, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+", "")));

        return tab;
    }

    public static String clearBOM(String str)
    {
        return str.replace("\uFEFF", "");
    }

    public static List<String> sortClearBOM(List<String> list)
    {
        for (String s : list)
        {
            if (s.contains("\uFEFF"))
            {
                list.remove(s);
                list.add(s.replace("\uFEFF", ""));
            }
        }

        Collections.sort(list);

        return list;
    }

    public static void displayLabel(Label lbl, Pos position, Paint colour, String labelText) {
        lbl.setAlignment(position);
        lbl.setTextFill(colour);
        lbl.setText(labelText);
        lbl.setVisible(true);
    }
}
