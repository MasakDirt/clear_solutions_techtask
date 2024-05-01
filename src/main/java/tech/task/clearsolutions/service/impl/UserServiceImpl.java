package tech.task.clearsolutions.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.task.clearsolutions.domain.RefreshToken;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.dto.LoginRequest;
import tech.task.clearsolutions.dto.UserSpecialFieldsUpdate;
import tech.task.clearsolutions.repository.UserRepository;
import tech.task.clearsolutions.service.RoleService;
import tech.task.clearsolutions.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Value("${required.users.age}")
    private int requiredAge;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User createUser(User user) {
        user.checkForValidBirthDate();
        user.isUserReachedRequiredAge(requiredAge);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.getByName("USER"));
        log.info("Creating user: {}, with valid age", user);
        return userRepository.save(user);
    }

    @Override
    public User updateSpecialFields(UserSpecialFieldsUpdate specialFieldsUpdate) {
        Long id = specialFieldsUpdate.getId();
        var user = getById(id);
        log.info("Trying to update user special fields with id: {}", id);
        user.mapUserSpecialFields(specialFieldsUpdate.getFirstname(),
                specialFieldsUpdate.getLastname(), specialFieldsUpdate.getEmail());
        log.info("Updating user special fields with id: {}", id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id {}", id);
        userRepository.delete(getById(id));
    }

    @Override
    public User getById(Long id) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id +
                        " not found, please check if it valid!"));
        log.info("Getting user by id: {}", id);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email " + email +
                        " not found, please check if it valid!"));
        log.info("Getting user by email: {}", email);
        return user;
    }

    @Override
    public List<User> getAllUsersByDates(LocalDate from, LocalDate to) {
        ifDateFromIsAfterDateToThrowExc(from, to);
        log.info("Dates 'from': {} and 'to': {} are valid, and user get it!", from, to);
        return userRepository.findByBirthDateRange(from, to);
    }

    private void ifDateFromIsAfterDateToThrowExc(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            log.error("Date 'from': {} is after 'to': {}", from, to);
            throw new IllegalArgumentException("Date 'from' must goes before date 'to'");
        }
    }

    @Override
    public User updateFullUser(User user, String oldPassword) {
        var readedUser = getById(user.getId());
        ifPasswordNotMatchesThrowExc(oldPassword, readedUser);
        user.checkForValidBirthDate();
        user.isUserReachedRequiredAge(requiredAge);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsersRoleAndRefreshToken(readedUser.getRole(), readedUser.getRefreshToken());
        return userRepository.save(user);
    }

    @Override
    public User proceedLogin(LoginRequest loginRequest) {
        var user = getByEmail(loginRequest.getEmail());
        ifPasswordNotMatchesThrowExc(loginRequest.getPassword(), user);
        log.info("Password matches, proceeding login with email: {}", loginRequest.getEmail());
        return user;
    }

    private void ifPasswordNotMatchesThrowExc(String rawPassword, User user) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            log.error("Password does not matches, for user with email: {}", user.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid password, please re-write it!");
        }
    }

    @Override
    public void setRefreshToken(User user, RefreshToken refreshToken) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

}
