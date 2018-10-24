package sample.services;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpServiceForMSFaceApi {

    /**
     * Метод формирует и возращает Http запрос для сервиса MS Face API, который определяет уникальные
     * характеристики лица
     *
     * @param uriBase         ссылка для обращения к сервису
     * @param faceAttributes  характеристики лица, которые необходимы для проверки
     * @param contentType     тип сформированного запроса
     * @param subscriptionKey токен для доступа к сервису MS Face API
     * @return запрос для сервиса
     * @throws URISyntaxException во время выполнения
     */
    public HttpPost getHttpRequestForDetect(String uriBase, String faceAttributes,
                                            String contentType, String subscriptionKey) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(uriBase);
        builder.setParameter("returnFaceId", "true");
        builder.setParameter("returnFaceLandmarks", "false");
        builder.setParameter("returnFaceAttributes", faceAttributes);

        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);

        request.setHeader("Content-Type", contentType);
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        return request;
    }

    /**
     * Метод формирует и возращает Http запрос для сервиса MS Face API,
     * который определяет процент схожести лиц на фотографиях
     *
     * @param uriVerify       ссылка для обращения к сервису
     * @param contentType     тип сформированного запроса
     * @param subscriptionKey токен для доступа к сервису MS Face API
     * @return запрос для сервиса
     * @throws URISyntaxException во время работы
     */
    public HttpPost getHttpRequestForVerify(String uriVerify, String contentType,
                                            String subscriptionKey) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(uriVerify);
        URI uri = builder.build();
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type", contentType);
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
        return request;
    }
}
