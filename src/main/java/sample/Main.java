package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    /**
     * Kонтейнер верхнего уровня
     */
    public static Stage window;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    static {
        nu.pattern.OpenCV.loadShared();
    }

    /**
     * Метод для запуска приложения
     *
     * @param primaryStage главное окно
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DownloadPhotoWindow.fxml"));
        primaryStage.setTitle("Идентификация пользователя");
        primaryStage.setMinHeight(550);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(new Scene(root, 600, 550));
        primaryStage.show();
        logger.info("Программа запустилась, открылось главное окно");
    }

    /**
     * Метод для начала работы приложения
     *
     * @param args массив строк
     */
    public static void main(String[] args) {
        launch(args);
    }
}
