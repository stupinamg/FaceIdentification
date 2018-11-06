package sample.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.Transition;
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
import sample.entity.ValidateResponseEntity;
import sample.entity.VerifyRequestEntity;
import sample.exceptions.FaceNotDetectedException;
import sample.services.DetectFaceService;
import sample.services.VerifyFaceService;
import sample.utils.AlertUtils;
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

    /** Отобразить сообщение ошибки распознавания лица на фото */
    private void showFaceNotDetectedError() {
        AlertUtils.makeError("Лицо не распознано",
                "Лицо на фото не распознано",
                new StringBuilder("Повторите попытку. \n\n")
                        .append("Возможно с диска Вы загрузили фото в низком \n")
                        .append("качестве или фото, на котором отсутсвует \n")
                        .append("изображение лица")
                        .toString())
                .showAndWait();
    }

    /** Отобразить сообщение необходимости сделать фото веб-камерой */
    private void showShouldMakePhotoInfo() {
        AlertUtils.makeInfo("Сделайте фото",
                "Вы не сделали фото.",
                "Попробуйте еще раз")
                .showAndWait();
    }

    /**
     * Отобразить сообщение совпадения фото, идентификация успешна
     * @param data степерь сходства
     */
    private void showIdentifyingSuccessfulInfo(String data) {
        AlertUtils.makeInfo("Результат идентификации пользователя",
                "Ваша личность подтверждена",
                "Лица совпадают. \nДостоверность cходства составляет: " + data)
                .showAndWait();
    }

    /** Отобразить сообщения ошибки идентификации пользователя */
    private void showFacesNotMatchError() {
        AlertUtils.makeError("Результат идентификации пользователя","Ваша личность не подтверждена",
                "Попробуйте еще раз")
                .showAndWait();
    }

    /** Отобразить сообщение ошибки при подключении к камере */
    private void showCameraNotFoundError() {
        AlertUtils.makeError("Веб-камера не обнаружена","Не удалось подключиться к камере",
                "Настройте подключение к веб-камере \nи повторите попытку")
                .showAndWait();
    }

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
        makeFadeOut()
                .play();
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
               showFaceNotDetectedError();
               makeFadeOut()
                       .play();
               tookFromWebCamImage = null;
               DownloadPhotoController.downloadedImage = null;
               logger.error("Выскочила ошибка распознавания лица. Загружаем предыдущую страницу");
           }
               logger.info("Нажата кнопка Инициализировать веб-контроллера");
        } else {
            showShouldMakePhotoInfo();
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
            showIdentifyingSuccessfulInfo(result.getConfidence());
        } else {
            showFacesNotMatchError();
            makeFadeOut()
                    .play();
            tookFromWebCamImage = null;
            DownloadPhotoController.downloadedImage = null;
            logger.info("Личность не подтверждена. Загружаем предыдущую страницу");
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
        makeFadeInTransition()
                .play();
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
        scene.getStylesheets().add("style.css");
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
                showCameraNotFoundError();
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

    private Transition makeFadeOut() {
        FadeTransition fadeTransition = makeFadeTransition(Duration.millis(1000), 1, 0);
        fadeTransition.setOnFinished(event -> loadPreviousScene());
        return fadeTransition;
    }

    private void loadPreviousScene() {
        try {
            Parent secondView = FXMLLoader.load(getClass().getResource("/fxml/DownloadPhotoWindow.fxml"));
            Scene newScene = new Scene(secondView, 600, 600);
            Stage currentStage = (Stage) borderPane.getScene().getWindow();
            currentStage.setScene(newScene);
            newScene.getStylesheets().add("style.css");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Transition makeFadeInTransition() {
        return makeFadeTransition(Duration.millis(1200), 0, 1);
    }

    private FadeTransition makeFadeTransition(Duration duration, double from, double to) {
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(duration);
        fadeTransition.setNode(borderPane);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        return fadeTransition;
    }
}
