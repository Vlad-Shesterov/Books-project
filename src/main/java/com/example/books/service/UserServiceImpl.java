package com.example.books.service;

import com.example.books.dto.UserDto;
import com.example.books.entity.Role;
import com.example.books.entity.User;
import com.example.books.repository.RoleRepository;
import com.example.books.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        ;
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("READ_ONLY");
        Role role1 = roleRepository.findByName("ROLE_USER");
        Role role2 = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = checkRoleExist();
        }
        if (role1 == null) {
            role1 = checkRoleExist1();
        }
        if (role2 == null) {
            role2 = checkRoleExist2();
        }



        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        org.springframework.security.core.userdetails.User userDetails = ( org.springframework.security.core.userdetails.User) principal;
        return userRepository.findByEmail(userDetails.getUsername());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("READ_ONLY");
        return roleRepository.save(role);
    }

    private Role checkRoleExist1(){
        Role role1 = new Role();
        role1.setName("ROLE_USER");
        return roleRepository.save(role1);
    }

    private Role checkRoleExist2(){
        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");
        return roleRepository.save(role2);
    }



}
