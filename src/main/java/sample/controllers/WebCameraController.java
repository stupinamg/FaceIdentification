package sample.controllers;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.utils.Utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebCameraController {

    private static final Logger logger = LoggerFactory.getLogger(TakePhotoController.class);

    /**
     * Метод совершает захват кадра из текущего видео-потока веб-камеры.
     * Принимает входящий @param camera - объект класса VideoCapture библиотеки openCV.
     *
     * @return захваченный кадр.
     */
    public static Image grabFrame(VideoCapture camera) {
        Mat frame = new Mat();
        Image imgSnapshot = null;

        if (camera.isOpened()) {
            if (camera.grab()) {
                if (camera.retrieve(frame)) {
                    imgSnapshot = Utils.mat2Image(frame);
                } else {
                    logger.info("Не удалось обработать кадр");
                }
            } else {
                logger.info("Не удалось захватить кадр");
            }
        }
        return imgSnapshot;
    }

    /**
     * Метод для остановки таймера отсчета количества кадров.
     *
     * @param timer вызывает упорядоченное завершение работы ранее поставленных задач
     */
    public static void stopTimer(ScheduledExecutorService timer) {
        if (timer != null && !timer.isShutdown()) {
            try {
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.error("Ошибка во время остановки камеры..." + e);
            }
        }
        logger.info("Таймер остановлен");
    }
}


