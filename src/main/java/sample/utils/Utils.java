package sample.utils;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public final class Utils {

    private static BufferedImage matToBufferedImage(Mat original) {
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    /**
     * Метод конвертирует полученную матрицу из видеo-потока веб-камеры в объект типа Image
     *
     * @param frame представляет собой n-мерную плотную числовую одноканальную или многоканальную матрицу
     * @return графическое изображение
     */
    public static Image mat2Image(Mat frame) {
        return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
    }

    /**
     * Общий метод для установки элемента, выполняющегося в потоке, отличном от JavaFX
     *
     * @param property параметры
     * @param value    мзначение заданное для установки
     * @param <T>      дженерализированный тип
     */
    public static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
        Platform.runLater(() -> {
            property.set(value);
        });
    }

    /**
     * Метод конвертирует графическое изображение в массив байт
     *
     * @param image графическое изображение
     * @return массив байт
     * @throws IOException во время выполнения
     */
    public static byte[] imageToByteArray(Image image) throws IOException {
        BufferedImage bufferImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferImage, "jpg", output);
        byte[] data = output.toByteArray();
        return data;
    }

}
