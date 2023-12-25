package keycloak.mongodb.users.federation.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HashUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashUtil.class);

    public static String hashData(String data){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(data.getBytes(UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.debug(e.toString());
            return data;
        }
    }
}
