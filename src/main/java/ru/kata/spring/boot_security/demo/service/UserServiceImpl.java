package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
   private final RoleService roleService;

   @Autowired
   public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
      this.userRepository = userRepository;
      this.roleService = roleService;
   }

   @Override
   public List<User> getAllUsers() {
      return userRepository.findAll();
   }

   @Override
   public void saveUser(User user, List<Long> selectedRolesIds) {
      userRepository.save(user);
   }

   @Override
   public User getUser(int id) {
      return userRepository.findById(id).orElse(null);
   }

   @Override
   public void deleteUser(int id) {
      userRepository.deleteById(id);
   }

   @Override
   public void updateUser(int id, User updatedUser, List<Long> selectedRolesIds) {
      User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

      existingUser.setUsername(updatedUser.getUsername());
      existingUser.setUserLastName(updatedUser.getUserLastName());
      existingUser.setUserAge(updatedUser.getUserAge());
      existingUser.setEmail(updatedUser.getEmail());
      existingUser.setPassword(updatedUser.getPassword());

      if (selectedRolesIds != null && !selectedRolesIds.isEmpty()) {
         existingUser.getRoles().clear();
         selectedRolesIds.forEach(roleId -> {
            Role role = roleService.getRoleById(roleId.longValue());
            existingUser.getRoles().add(role);
         });
      }

      userRepository.save(existingUser);
   }

   @Override
   public User findByEmail(String email) {
      return userRepository.findByEmail(email);
   }
}