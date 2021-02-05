package com.epam.admissions.service;

import com.epam.admissions.entity.User;
import com.epam.admissions.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("unknown username"));
    }

    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void blockUser(Long id) {
        userRepository.blockUser(id);
    }

    public void unblockUser(Long id) {
        userRepository.unblockUser(id);
    }
}
