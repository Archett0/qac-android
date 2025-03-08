package sg.edu.nus.qac_android.utils;

import java.util.UUID;

public class UUIDConverter {

    // 将 String 转换为 UUID（如果转换失败，则返回 null）
    public static UUID convertStringToUUID(String idString) {
        try {
            return UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;  // 这里返回 null 说明转换失败
        }
    }

    // 将 UUID 转换为 String（方便传递到 Intent 或 API）
    public static String convertUUIDToString(UUID id) {
        return id != null ? id.toString() : null;
    }
}
