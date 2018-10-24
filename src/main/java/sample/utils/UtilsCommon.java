package sample.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class UtilsCommon {
    private static final Logger logger = LoggerFactory.getLogger(UtilsCommon.class);

    /**
     * Метод возвращает значение свойств
     *
     * @param parameter название параметра, который необходимо вернуть
     * @return значение параметра
     */
    public static String getProperty(String parameter){
        try (InputStream is = UtilsCommon.class.getResourceAsStream("/config.properties");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            Properties property = new Properties();
            property.load(reader);
            return property.getProperty(parameter);
        } catch (IOException e) {
            logger.error("Не удалось загрузить config.properties");
            throw new RuntimeException(e);
        }
    }
}
