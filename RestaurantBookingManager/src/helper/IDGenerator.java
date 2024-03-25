package helper;

import java.util.UUID;

public class IDGenerator {

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

}
