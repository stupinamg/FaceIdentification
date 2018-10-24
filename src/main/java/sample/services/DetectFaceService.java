package sample.services;

import javafx.scene.image.Image;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.entity.DetectedResponseEntity;
import sample.utils.Utils;
import sample.utils.UtilsCommon;

import java.io.IOException;
import java.util.List;

public class DetectFaceService {

    private static final Logger logger = LoggerFactory.getLogger(DetectFaceService.class);

    private static final String SUBSCRIPTION_KEY = UtilsCommon.getProperty("subscriptionKey");
    private static final String URI_BASE = UtilsCommon.getProperty("uriBase");
    private static final String FACE_ATTRIBUTES = UtilsCommon.getProperty("faceAttributes");
    private static final String CONTENT_TYPE = "application/octet-stream";
    /**
     * Клиент, который используется для передачи и получения запроса и ответа
     */
    public HttpClient httpclient = HttpClientBuilder.create().build();
    /**
     * Сервис формирования запроса для передачи http клиенту
     */
    public HttpServiceForMSFaceApi httpServiceForMSFaceApi = new HttpServiceForMSFaceApi();

    /**
     * Метод определяет уникальные характеристики лица на переданной фотографии.
     *
     * @param image фото загруженное или полученное из видео-потока веб-камеры
     * @return уникальный id клиента, полученный от сервиса MS Face API, в виде строки.
     * @throws Exception во время выполнения
     */
    public String detect(Image image) throws Exception {
        HttpPost request = httpServiceForMSFaceApi.getHttpRequestForDetect(URI_BASE, FACE_ATTRIBUTES, CONTENT_TYPE, SUBSCRIPTION_KEY);

        HttpEntity entity1 = new ByteArrayEntity(convertImgToByteArray(image));
        request.setEntity(entity1);

        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        List<DetectedResponseEntity> myObjects = null;
        if (entity != null) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = EntityUtils.toString(entity).trim();

            myObjects = mapper.readValue(jsonString,
                    mapper.getTypeFactory().constructCollectionType(List.class, DetectedResponseEntity.class));
            logger.info(jsonString);
            logger.info(myObjects.get(0).getFaceId());
        }
        return myObjects.get(0).getFaceId();
    }

    private byte[] convertImgToByteArray(Image image) throws IOException {
        byte[] fileContent = Utils.imageToByteArray(image);
        logger.info("метод convertImgToByteArray - convert image to byte[] , размер массива[]" + fileContent.length);
        return fileContent;
    }
}