package sample.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Main;
import sample.alertwindows.AlertUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadPhotoController implements Initializable{

    private static final Logger logger = LoggerFactory.getLogger(DownloadPhotoController.class);

    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView imgDown;
    private Image icon = new Image("data/ic.jpg");

    /**
     * Переменная для сохранения фото, загруженного с диска пользователем
     */
    public static Image downloadedImage;

    /** Отобразить информационное сообщение выбора файла */
    private void showFileChoosingInfo() {
        AlertUtils.makeInfo("Выберите файл", "Вы не загрузили фото", "Попробуйте еще раз")
                .showAndWait();
    }

    /**
     * Метод загружает фото с диска в статическую переменную downloadedImage при нажтии кнопки.
     *
     * @param actionEvent событие нажатия по кнопке
     */
    public void downloadImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать изображние для загрузки");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("jpeg, jpg, png, bmp, gif",
                "*.jpeg", "*.jpg", "*.png", "*.bmp", "*.gif"));
        File file = fileChooser.showOpenDialog(Main.window);

        if (file != null) {
            Image imageDownloaded = new Image(file.toURI().toString());
            imgDown.setImage(imageDownloaded);
            imgDown.setPreserveRatio(true);
            imgDown.setFitHeight(420);
            imgDown.setFitWidth(380);

        } else {
            showFileChoosingInfo();
        }
        downloadedImage = imgDown.getImage();
        logger.info("Пользователь загрузит фото с диска");
    }

    /**
     * Метод переключает следующее окно в случае успешной записи файла в переменную.
     *
     * @param actionEvent событие нажатия по кнопке
     */
    public void goForward(ActionEvent actionEvent) {
        if (downloadedImage != null) {
            makeFadeOut(actionEvent)
                    .play();
            logger.info("Переход к следующему окну после загрузки фото с диска");
        } else {
            showFileChoosingInfo();
        }
    }

    /**
     * Метод закрывает текущее окно.
     *
     * @param actionEvent событие нажатия по кнопке
     **/
    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Метод вызывается для инициализации контроллера после того, как его корневой элемент был полностью обработан.
     *
     * @param location
     * @param resources
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgDown.setImage(icon);
        imgDown.setFitHeight(400);
        imgDown.setFitWidth(350);
        borderPane.setOpacity(0);
        makeFadeInTransition()
                .play();
        logger.info("Произошла инициализаця класса DownloadPhotoController");
    }

    private Transition makeFadeOut(ActionEvent actionEvent) {
        FadeTransition fadeTransition = makeFadeTransition(1, 0);
        fadeTransition.setOnFinished(event -> loadNextScene());
        return fadeTransition;
    }

    private void loadNextScene() {
        try {
            Parent secondView = FXMLLoader.load(getClass().getResource("/fxml/TakePhotoWindow.fxml"));
        } catch (IOException e) {
            logger.error("При чтении файла произошла ошибка " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Transition makeFadeInTransition() {
        FadeTransition fadeTransition = makeFadeTransition(0, 1);
        return fadeTransition;
    }

    private FadeTransition makeFadeTransition(double from, double to) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(borderPane);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        return fadeTransition;
    }
}

