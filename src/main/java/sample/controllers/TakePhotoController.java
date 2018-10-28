package sample.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.Main;
import sample.alertwindows.FaceNotDetected;
import sample.alertwindows.FacesNotMatch;
import sample.alertwindows.IdentifyingSuccessful;
import sample.alertwindows.ShouldMakePhoto;
import sample.entity.ValidateResponseEntity;
import sample.entity.VerifyRequestEntity;
import sample.exceptions.FaceNotDetectedException;
import sample.services.DetectFaceService;
import sample.services.VerifyFaceService;
import sample.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TakePhotoController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(TakePhotoController.class);

    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView imageView;

    /**
     * Переменная для сохранения фото, полученного из видео-потока веб-камеры
     */
    public static Image tookFromWebCamImage;
    private boolean cameraActive = false;
    private ScheduledExecutorService timer;
    private VideoCapture camera = new VideoCapture(0);
    private DetectFaceService detectFaceService = new DetectFaceService();
    private VerifyFaceService verifyFaceService = new VerifyFaceService();


    /**
     * Метод записывает захваченный кадр из видео-потока в переменную tookFromWebCamImage текущего класса.
     *
     * @param actionEvent событие нажатия по кнопке
     */
    public void takePhoto(ActionEvent actionEvent) {
        tookFromWebCamImage = WebCameraController.grabFrame(camera);
        WebCameraController.stopTimer(timer);
        if (tookFromWebCamImage != null) {
            logger.info("Изображение с веб-камеры захвачено и записано в память");
        }
    }

    /**
     * Метод переключает текущий вид на предыдущее окно.
     *
     * @param actionEvent событие нажатия по кнопке
     */
    public void goBack(ActionEvent actionEvent) {
        makeFadeOut();
        tookFromWebCamImage = null;
        DownloadPhotoController.downloadedImage = null;
        logger.info("Нажатие кнопки Назад окна веб-контроллера");
    }

    /**
     * Метод проводит сравнение идентичности лиц по двум фотографиям.
     *
     * @param actionEvent событие нажатия по кнопке
     * @throws Exception
     */
    public void compareFaces(ActionEvent actionEvent) throws Exception {
        String faceId2 = null;
        String faceId1 = null;
        if (tookFromWebCamImage != null && DownloadPhotoController.downloadedImage != null) {
           try {
               faceId2 = detectFaceService.detect(TakePhotoController.tookFromWebCamImage);
               faceId1 = detectFaceService.detect(DownloadPhotoController.downloadedImage);

            VerifyRequestEntity verifyRequestEntity = new VerifyRequestEntity();
                verifyRequestEntity.setFaceIdDownload(faceId1);
                verifyRequestEntity.setFaceIdSnapshot(faceId2);
                showResultOfComparison(verifyRequestEntity);

           }catch (FaceNotDetectedException e){
               FaceNotDetected.showAlert();
               makeFadeOut();
               tookFromWebCamImage = null;
               DownloadPhotoController.downloadedImage = null;
               logger.error("Выскочила ошибка распознавания лица. Загружаем предыдущую страницу");
           }
               logger.info("Нажата кнопка Инициализировать веб-контроллера");
        } else {
            ShouldMakePhoto.showAlert();
        }
    }

    /**
     * Метод выводит результат проведенного сравнения.
     *
     * @param verifyRequestEntity сущьность, которая содержит результат сравнения
     * @throws Exception
     */
    public void showResultOfComparison(VerifyRequestEntity verifyRequestEntity) throws Exception {
        ValidateResponseEntity result = verifyFaceService.verify(verifyRequestEntity);
        logger.info("в takePhotoContrоller получен ответ от запроса " + result.isIdentical() + " " + result.getConfidence());

        if (result.isIdentical()) {
            IdentifyingSuccessful.showAlertWithDefaultHeaderText(result.getConfidence());
        } else {
            FacesNotMatch.showAlert();
        }
    }

    /**
     * Метод завершает работу приложения.
     *
     * @param event событие нажатия по кнопке
     */
    public void exit(ActionEvent event) {
        System.exit(0);
        logger.info("Выход из программы");
    }

    /**
     * Метод вызывается для инициализации контроллера после того, как его корневой элемент был полностью обработан.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        borderPane.setOpacity(0);
        makeFadeInTransition();
        initController();
        logger.info("Произведена инициализация класса TakePhotoController");
    }

    private void initController() {
        Stage stage = (Stage) Main.window.getScene().getWindow();
        Scene scene = new Scene(borderPane, 600, 600);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(800);
        imageView.setFitWidth(550);
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 1024);
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 768);
        stage.setScene(scene);
        stage.show();
        logger.info("Метод донастройки инит работает");
        startWebcam();
        logger.info("Метод инициализации вызвал дополнительный метод для донастройки камеры и stage");
    }

    private void startWebcam() {
        if (!cameraActive) {
            camera.open(0);
            if (camera.isOpened()) {
                cameraActive = true;

                Runnable frameGrabber = new Runnable() {
                    @Override
                    public void run() {
                        Image imageToShow = WebCameraController.grabFrame(camera);
                        updateImageView(imageView, imageToShow);
                    }
                };

                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

            } else {
                System.err.println("Не удалось подключиться к камере");
            }
        } else {
            cameraActive = false;
            WebCameraController.stopTimer(timer);
        }
        logger.info("Веб-камера отработала успешно");
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    private void makeFadeOut() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setNode(borderPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> loadPreviousScene());
        fadeTransition.play();
    }

    private void loadPreviousScene() {
        try {
            Parent secondView = FXMLLoader.load(getClass().getResource("/fxml/DownloadPhotoWindow.fxml"));
            Scene newScene = new Scene(secondView, 600, 600);
            Stage currentStage = (Stage) borderPane.getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeFadeInTransition() {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.millis(1200));
        fadeTransition.setNode(borderPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
}
