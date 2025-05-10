package example.com.usersubscriptionservice.adapter.out.persistence;

import example.com.usersubscriptionservice.adapter.out.entity.UserEntity;
import example.com.usersubscriptionservice.adapter.out.mapper.UserMapper;
import example.com.usersubscriptionservice.adapter.out.repository.JpaUserRepository;
import example.com.usersubscriptionservice.application.port.out.UserRepositoryPort;
import example.com.usersubscriptionservice.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {
    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;
    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedUserEntity = jpaUserRepository.save(userEntity);
        return  userMapper.toDomain(savedUserEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUserRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return jpaUserRepository.existsByEmailAndIdNot(email, id);
    }
}
