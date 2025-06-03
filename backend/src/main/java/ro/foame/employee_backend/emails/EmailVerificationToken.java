package ro.foame.employee_backend.emails;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmailVerificationToken {
    private static final Map<String, Long> TOKENS = new HashMap<>();

    public static String createToken(Long userId) {
        String token = UUID.randomUUID().toString();
        TOKENS.put(token, userId);
        return token;
    }

    public static Long getUserId(String token) {
        return TOKENS.get(token);
    }

    public static void invalidateToken(String token) {
        TOKENS.remove(token);
    }
}