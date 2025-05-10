package example.com.usersubscriptionservice.application.service;

import example.com.usersubscriptionservice.adapter.out.mapper.UserMapper;
import example.com.usersubscriptionservice.application.exception.NotFoundException;
import example.com.usersubscriptionservice.application.exception.ValidationException;
import example.com.usersubscriptionservice.application.port.in.UserUseCase;
import example.com.usersubscriptionservice.application.port.out.UserRepositoryPort;
import example.com.usersubscriptionservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;
    private final UserRepositoryPort userRepositoryPort;

    @Transactional
    @Override
    public User createUser(User user) {
        logger.info("Создание нового пользователя с email: {}", user.getEmail());
        validateUser(user);
        User saved = userRepositoryPort.save(user);
        logger.debug("Пользователь успешно создан: {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        logger.info("Получение списка всех пользователей");
        List<User> users = userRepositoryPort.findAll();
        logger.debug("Найдено {} пользователей", users.size());
        return users;
    }

    @Transactional
    @Override
    public User updateUser(Long id, User user) {
        logger.info("Обновление данных пользователя с ID: {}", id);
        validateUserUpdate(user, id);
        User existing = getUser(id);
        userMapper.mergeUser(existing, user);
        User updated = userRepositoryPort.save(existing);
        logger.debug("Пользователь с ID {} успешно обновлён: {}", id, updated);
        return updated;
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        logger.info("Попытка удалить пользователя с ID: {}", id);
        if (!userRepositoryPort.existsById(id)) {
            logger.warn("Попытка удалить несуществующего пользователя с ID: {}", id);
            throw new NotFoundException("Пользователь с указанным ID не найден");
        }
        userRepositoryPort.deleteById(id);
        logger.debug("Пользователь с ID {} успешно удалён", id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(Long id) {
        logger.info("Получение данных пользователя с ID: {}", id);
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Пользователь с ID {} не найден", id);
                    return new NotFoundException("Пользователь с таким ID не существует");
                });
    }

    private void validateUser(User user) {
        if (user == null) {
            logger.error("Попытка создать пользователя с пустыми данными (null)");
            throw new ValidationException("Необходимо предоставить данные для создания пользователя");
        }
        if (userRepositoryPort.existsByEmail(user.getEmail())) {
            logger.warn("Пользователь с email {} уже существует", user.getEmail());
            throw new ValidationException("Пользователь с таким email уже зарегистрирован");
        }
    }

    private void validateUserUpdate(User user, Long id) {
        if (user == null) {
            logger.error("Попытка обновить пользователя с пустыми данными (null), ID: {}", id);
            throw new ValidationException("Необходимо предоставить данные для обновления пользователя");
        }
        if (userRepositoryPort.existsByEmailAndIdNot(user.getEmail(), id)) {
            logger.warn("Пользователь с email {} уже существует (обновление ID: {})", user.getEmail(), id);
            throw new ValidationException("Пользователь с таким email уже зарегистрирован, обновление невозможно");
        }
    }
}
