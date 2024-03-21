package org.zheleznikov.authservice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zheleznikov.authservice.entity.UserEntity;
import org.zheleznikov.authservice.exception.EmailConfirmException;
import org.zheleznikov.authservice.exception.LoginException;
import org.zheleznikov.authservice.exception.SignupException;
import org.zheleznikov.authservice.exception.SignupExceptionEmailAlreadyExists;
import org.zheleznikov.authservice.repository.UserRepository;
import org.zheleznikov.generated.model.LoginRequest;
import org.zheleznikov.generated.model.SignupRequest;

public class ValidationService {

    public static void validateIfEmailAlreadyExists(SignupRequest request, UserRepository userRepository) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new SignupExceptionEmailAlreadyExists("Email already registered");
        }
    }

    public static void validateIfPasswordsDoNotMatch(SignupRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new SignupException("Passwords do not match");
        }
    }


    public static void validateIfUserExists(UserEntity user) {
        if (user == null) {
            throw new LoginException("Invalid email or password");
        }
    }

    public static void validateIfConfirmationCodeCreated(UserEntity user) {
        if (user.getUserCode() == null) {
            throw new SignupException("No information about confirmation code");
        }
    }

    public static void validateIfPasswordCorrect(LoginRequest request, UserEntity user, BCryptPasswordEncoder bCryptPasswordEncoder) {
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginException("Invalid email or password");
        }
    }

    public static void validateIfPossibleToSendCode(UserEntity user) {
        String validationMessage = "Something went wrong...";

        if (user == null) {
            throw new EmailConfirmException(validationMessage);
        }

        if (user.getIsConfirmed()) {
            throw new EmailConfirmException(validationMessage);
        }

    }
}
