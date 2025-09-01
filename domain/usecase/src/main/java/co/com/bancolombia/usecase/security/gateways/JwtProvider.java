package co.com.bancolombia.usecase.security.gateways;

import co.com.bancolombia.model.user.User;

public interface JwtProvider {
    String generateToken(User user);
}
