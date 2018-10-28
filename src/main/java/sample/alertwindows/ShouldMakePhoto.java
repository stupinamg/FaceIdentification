package sample.alertwindows;

import javafx.scene.control.Alert;

public class ShouldMakePhoto {

    /**
     * Метод создания сообщения в случае если не было сделано фото веб-камерой
     */
    public static void showAlert() {
        Alert inf = new Alert(Alert.AlertType.INFORMATION);

        inf.setTitle("Сделайте фото");
        inf.setHeaderText("Вы не сделали фото.");
        inf.setContentText("Попробуйте еще раз");
        inf.showAndWait();
    }
}
