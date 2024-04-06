package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
  User findUserByUsername(String username);

  User findUserByEmail(String email);

  @Transactional
  @Modifying
  @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
  void updateUserPassword(String email, String password);}
