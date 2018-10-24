package sample.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.entity.ValidateResponseEntity;
import sample.entity.VerifyRequestEntity;
import sample.utils.UtilsCommon;

public class VerifyFaceService {

    private static final Logger logger = LoggerFactory.getLogger(VerifyFaceService.class);
    private static final String URI_VERIFY = UtilsCommon.getProperty("uriVerify");
    private static final String CONTENT_TYPE = "application/json";
    private static final String SUBSCRIPTION_KEY = UtilsCommon.getProperty("subscriptionKey");
    /**
     * Сервис формирования запроса для передачи http клиенту
     */
    public HttpServiceForMSFaceApi httpServiceForMSFaceApi = new HttpServiceForMSFaceApi();

    /**
     * Метод определяет процент схожести лиц на переданных фотографиях с помощью сервиса MS Face API
     *
     * @param verifyRequestEntity сущность, которая хранит уникальные id лиц для сравнения
     * @return сущность, которая показывает процент схожести
     * @throws Exception во время выполнения
     */
    public ValidateResponseEntity verify(VerifyRequestEntity verifyRequestEntity) throws Exception {
        HttpPost request = httpServiceForMSFaceApi.getHttpRequestForVerify(URI_VERIFY, CONTENT_TYPE, SUBSCRIPTION_KEY);
        ValidateResponseEntity resultResponse = null;
        HttpClient httpclient = HttpClients.createDefault();

        ObjectMapper mapper = new ObjectMapper();
        String verifyRequest = mapper.writeValueAsString(verifyRequestEntity);

        StringEntity reqEntity = new StringEntity(verifyRequest);
        request.setEntity(reqEntity);

        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String jsonString = EntityUtils.toString(entity).trim();
            resultResponse = mapper.readValue(jsonString, ValidateResponseEntity.class);
            logger.info("in verify service get: " + resultResponse.isIdentical() + " " + resultResponse.getConfidence());
        }
        return resultResponse;
    }

}