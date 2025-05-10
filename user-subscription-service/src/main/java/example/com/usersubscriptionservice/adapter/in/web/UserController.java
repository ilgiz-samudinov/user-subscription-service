package example.com.usersubscriptionservice.adapter.in.web;

import example.com.usersubscriptionservice.adapter.in.web.dto.UserRequest;
import example.com.usersubscriptionservice.adapter.in.web.dto.UserResponse;
import example.com.usersubscriptionservice.adapter.out.mapper.UserMapper;
import example.com.usersubscriptionservice.application.service.UserService;
import example.com.usersubscriptionservice.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        logger.info("Запрос на создание нового пользователя с email: {}", userRequest.getEmail());
        User user  = userMapper.toDomain(userRequest);
        User createdUser = userService.createUser(user);
        logger.debug("Пользователь с email: {} успешно создан", userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(createdUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.info("Запрос на получение всех пользователей");
        List<UserResponse> response = userService.getAllUsers().stream()
                .map(userMapper::toResponse)
                .toList();
        logger.debug("Получено {} пользователей", response.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest userRequest) {
        logger.info("Запрос на обновление пользователя с ID: {}", id);
        User user  = userMapper.toDomain(userRequest);
        User updatedUser = userService.updateUser(id, user);
        logger.debug("Пользователь с ID: {} успешно обновлён", id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toResponse(updatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        logger.info("Запрос на получение пользователя с ID: {}", id);
        User user = userService.getUser(id);
        logger.debug("Пользователь с ID: {} успешно получен", id);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        logger.info("Запрос на удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
        logger.debug("Пользователь с ID: {} успешно удалён", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
