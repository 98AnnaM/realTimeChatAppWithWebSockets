package com.devexperts.chatapp.service;

import com.devexperts.chatapp.exception.ObjectNotFoundException;
import com.devexperts.chatapp.exception.UserLimitExceededException;
import com.devexperts.chatapp.model.dto.UserSearchDto;
import com.devexperts.chatapp.model.dto.UserRegisterDto;
import com.devexperts.chatapp.model.dto.UserViewAndEditDto;
import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.model.enums.RoleEnum;
import com.devexperts.chatapp.model.enums.StatusEnum;
import com.devexperts.chatapp.repository.RoleRepository;
import com.devexperts.chatapp.repository.UserRepository;
import com.devexperts.chatapp.repository.UserSpecification;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Value("${app.maxUsersAllowed}")
    private int maxUsersAllowed;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Cacheable(value = "users", key = "'allUsers'")
    public List<UserViewAndEditDto> getAll() {
        log.info("Perform a request for getting all users from the database");
        return userRepository.findAll(Sort.by(Sort.Order.asc("username"), Sort.Order.desc("country")))
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserViewAndEditDto.class))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "users", allEntries = true)
    public void registerUser(UserRegisterDto userRegisterDto) {
        if (userRepository.count() >= maxUsersAllowed) {
            throw new UserLimitExceededException("Maximum number of users exceeded. Cannot save new user.");
        }

        UserEntity user = modelMapper.map(userRegisterDto, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        user.setRoles(roleRepository.findAll()
                .stream()
                .filter(r -> r.getRole() == (RoleEnum.USER))
                .collect(Collectors.toList()));

        userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long userId) {
        UserEntity userEntity = getUserById(userId);
        userRepository.delete(userEntity);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void updateUser(UserViewAndEditDto editUserDto, Long userId) {
        UserEntity updateUser = getUserById(userId);
        updateUser.setUsername(editUserDto.getUsername());
        updateUser.setEmail(editUserDto.getEmail());
        updateUser.setAge(editUserDto.getAge());
        updateUser.setCountry(editUserDto.getCountry());
        updateUser.setDate(new Date());

        userRepository.save(updateUser);
    }

    public UserViewAndEditDto getUserEditDetails(Long userId) {
        return modelMapper.map(getUserById(userId), UserViewAndEditDto.class);
    }

    public boolean usernameExists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with ID " + userId + " not found!"));
    }

    public List<UserViewAndEditDto> searchUsers(UserSearchDto searchUserDto) {
        return this.userRepository
                .findAll(new UserSpecification(searchUserDto))
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserViewAndEditDto.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "users", key = "'usersUnder18'")
    public List<UserViewAndEditDto> getUsersUnder18() {
        log.info("Perform a request for getting users under 18 from the database");
        return userRepository.findAllByAgeBeforeOrderByAgeAsc(18)
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserViewAndEditDto.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "users", key = "'usersFromBulgaria'")
    public List<UserViewAndEditDto> getUsersFromBulgaria() {
        log.info("Perform a request for getting users from Bulgaria from the database");
        return userRepository.findAllByCountryIsOrderByUsername("Bulgaria")
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserViewAndEditDto.class))
                .collect(Collectors.toList());
    }

    public void disconnect(String username) {
        UserEntity disconnectedUser = userRepository.findByUsername(username).orElse(null);
        if (disconnectedUser != null) {
            disconnectedUser.setStatus(StatusEnum.OFFLINE);
            userRepository.save(disconnectedUser);
        }
    }

    public void connect(String username) {
        UserEntity connectedUser = userRepository.findByUsername(username).orElse(null);
        if (connectedUser != null) {
            connectedUser.setStatus(StatusEnum.ONLINE);
            userRepository.save(connectedUser);
        }
    }

    public List<UserViewAndEditDto> findConnectedUsers() {
        return userRepository
                .findAllByStatus(StatusEnum.ONLINE)
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserViewAndEditDto.class))
                .collect(Collectors.toList());
    }


}
