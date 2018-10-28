package sample.alertwindows;

import javafx.scene.control.Alert;

public class FaceNotDetected {

    /**
     * Метод создания сообщения в случае если не было распознано лицо на фото
     */
    public static void showAlert() {
        Alert inf = new Alert(Alert.AlertType.ERROR);
        inf.setTitle("Лицо не распознано");
        inf.setHeaderText("Лицо на фото не распознано");
        inf.setContentText("Повторите попытку. \n" +
                            "\n" +
                            "Возможно с диска Вы загрузили фото в низком \n" +
                            "качестве или фото, на котором отсутсвует \n" + "изображение лица");
        inf.showAndWait();
    }
}
