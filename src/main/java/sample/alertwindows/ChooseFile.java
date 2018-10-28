package sample.alertwindows;

import javafx.scene.control.Alert;

public class ChooseFile {

    /**
     * Метод создания сообщения в случае если не было загружено фото с диска
     */
    public static void showAlert() {
        Alert inf = new Alert(Alert.AlertType.INFORMATION);
        inf.setTitle("Выберите файл");
        inf.setHeaderText("Вы не загрузили фото");
        inf.setContentText("Попробуйте еще раз");
        inf.showAndWait();
    }
}
