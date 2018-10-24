package sample.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Main;
import sample.alertwindows.ChooseFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadPhotoController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(DownloadPhotoController.class);

    @FXML
    private VBox firstWinBox;
    @FXML
    private ImageView imgDown;

    /**
     * Переменная для сохранения фото, загруженного с диска пользователем
     */
    @FXML
    public static Image downloadedImage;

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
        } else {
            ChooseFile.showAlert();
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
            makeFadeOut(actionEvent);
            logger.info("Переход к следующему окну после загрузки фото с диска");
        } else {
            ChooseFile.showAlert();
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
        firstWinBox.setOpacity(0);
        makeFadeInTransition();
        logger.info("Произошла инициализаця класса DownloadPhotoController");
    }

    private void makeFadeOut(ActionEvent actionEvent) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(firstWinBox);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> loadNextScene());
        fadeTransition.play();
    }

    private void loadNextScene() {
        try {
            Parent secondView = FXMLLoader.load(getClass().getResource("/fxml/TakePhotoWindow.fxml"));
            Stage currentStage = (Stage) firstWinBox.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            logger.error("При чтении файла произошла ошибка " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(firstWinBox);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
}

