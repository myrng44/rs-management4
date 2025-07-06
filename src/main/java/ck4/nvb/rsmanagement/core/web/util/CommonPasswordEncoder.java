package ck4.nvb.rsmanagement.core.web.util;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommonPasswordEncoder {
    // Get the only object available
    // Create an object
    @Getter
    private static final CommonPasswordEncoder instance = new CommonPasswordEncoder();

    @Getter
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(8);

    // Make the constructor private so that this class cannot be instantiated
    private CommonPasswordEncoder() {
    }

    public String encode(String raw) {
        return encoder.encode(raw);
    }

    public boolean matches(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
