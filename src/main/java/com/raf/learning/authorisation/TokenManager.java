package com.raf.learning.authorisation;

import com.raf.learning.model.Token;
import com.raf.learning.repository.TokensRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenManager {
    private final TokensRepository tokensRepository;

    public TokenManager(TokensRepository tokensRepository) {
        this.tokensRepository = tokensRepository;
    }

    public Token generateToken(String studentId) {
        String tokenValue = UUID.randomUUID().toString();
        var token = new Token(studentId, tokenValue);
        tokensRepository.save(token);
        return token;
    }

    public String  deleteToken(String studentId, String tokenValue) {
        var result = tokensRepository.findById(studentId);
        if (result.isEmpty()){
            return String.format("Token for the student with id: %s doesn't exist in the db", studentId);
        }
        if (!tokenValue.equals(result.get().getValue())) {
            return String.format("Wrong token value provided: %s", tokenValue);
        }
        tokensRepository.deleteById(studentId);
        return String.format("Token for the student with id: %s has been successfully deleted", studentId);
    }

    public boolean verifyToken(String studentId, String tokenValue) {
        var result = tokensRepository.findById(studentId);
        if (result.isEmpty()){
            return false;
        }

        if (!tokenValue.equals(result.get().getValue())) {
            return false;
        }

        return true;
    }
}
