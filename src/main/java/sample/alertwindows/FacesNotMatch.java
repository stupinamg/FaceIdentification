package sample.alertwindows;

import javafx.scene.control.Alert;

public class FacesNotMatch {

    /**
     * Метод создания сообщения в случае не успешной идентификации пользователя
     */
    public static void showAlert() {
        Alert warn = new Alert(Alert.AlertType.ERROR);

        warn.setTitle("Результат идентификации пользователя");
        warn.setHeaderText("Ваша личность не подтверждена");
        warn.setContentText("Попробуйте еще раз");

        warn.showAndWait();
    }
}
