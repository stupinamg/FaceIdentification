package sample.alertwindows;

import javafx.scene.control.Alert;

public class CameraNotFound {

    /**
     * Метод создания сообщения в случае если возникла ошибка при подключении к камере
     */
    public static void showAlert() {
        Alert inf = new Alert(Alert.AlertType.ERROR);
        inf.setTitle("Веб-камера не обнаружена");
        inf.setHeaderText("Не удалось подключиться к камере");
        inf.setContentText("Настройте подключение к веб-камере \n" +
                            "и повторите попытку");
        inf.showAndWait();
    }
}
