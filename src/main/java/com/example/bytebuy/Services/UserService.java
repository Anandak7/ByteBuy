package com.example.bytebuy.Services;

import com.example.bytebuy.Entities.Users;
import com.example.bytebuy.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public Users createUser(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    public List<Users> getUsers(){
        return userRepository.findAll();
    }
    public Users getUserByName(String name){
        return userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
