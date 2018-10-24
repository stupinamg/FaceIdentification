package sample.alertwindows;

import javafx.scene.control.Alert;

public class IdentifyingSuccessful {

    /**
     * Метод создания сообщения в случае если фото совпадают, идентификация успешна
     */
    public static void showAlertWithDefaultHeaderText(String data) {
        Alert successful = new Alert(Alert.AlertType.INFORMATION);

        successful.setTitle("Результат идентификации пользователя");
        successful.setHeaderText("Ваша личность подтверждена");
        successful.setContentText("Лица совпадают. \n" +
                "Достоверность cходства составляет: " + data);

        successful.showAndWait();
    }
}
