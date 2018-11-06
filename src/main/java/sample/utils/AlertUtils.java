package sample.utils;

import javafx.scene.control.Alert;

/**
 * Утилитный класс для сообщений
 */
public class AlertUtils {

    /**
     * Создать информационное сообщение
     * @param title заголовок
     * @param header подзаголовок
     * @param content содержимое
     * @return инф. сообщение
     */
    public static Alert makeInfo(String title, String header, String content) {
        Alert inf = new Alert(Alert.AlertType.INFORMATION);
        return fillAlert(inf, title, header, content);
    }

    /**
     * Создать сообщение ошибки
     * @param title заголовок
     * @param header подзаголовок
     * @param content содержимое
     * @return сообщение ошибки
     */
    public static Alert makeError(String title, String header, String content) {
        Alert err = new Alert(Alert.AlertType.ERROR);
        return fillAlert(err, title, header, content);
    }

    /**
     * Заполнить сообщение
     * @param alert сообщение
     * @param title заголовок
     * @param header подзаголовок
     * @param content содержимое
     * @return сообщение
     */
    public static Alert fillAlert(Alert alert, String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}