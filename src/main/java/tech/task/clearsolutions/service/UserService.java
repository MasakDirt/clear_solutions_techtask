package tech.task.clearsolutions.service;

import org.springframework.stereotype.Service;
import tech.task.clearsolutions.domain.RefreshToken;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.dto.LoginRequest;
import tech.task.clearsolutions.dto.UserSpecialFieldsUpdate;

import java.time.LocalDate;
import java.util.List;

@Service
public interface UserService {

    User createUser(User user);

    User updateSpecialFields(UserSpecialFieldsUpdate specialFieldsUpdate);

    User updateFullUser(User user, String oldPassword);

    void deleteUser(Long id);

    User getById(Long id);

    User getByEmail(String email);

    List<User> getAllUsersByDates(LocalDate from, LocalDate to);

    User proceedLogin(LoginRequest loginRequest);

    void setRefreshToken(String email, RefreshToken refreshToken);

}
