package com.example.usermanagement.services;

import com.example.usermanagement.models.User;
import com.example.usermanagement.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean blockUsers(List<Long> userIds) {
        boolean flag = isCurrentUserAffected(userIds);
        for (Long userId : userIds) {
            updateBlockedStatus(userId, true);
        }
        return flag;
    }

    public void unblockUsers(List<Long> userIds) {
        for (Long userId : userIds) {
            updateBlockedStatus(userId, false);
        }
    }

    public boolean deleteUsers(List<Long> userIds) {
        boolean flag = isCurrentUserAffected(userIds);
        for (Long userId : userIds) {
            userRepository.deleteById(userId);
        }
        return flag;
    }

    private boolean isCurrentUserAffected(List<Long> userIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            currentUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        User currentUser = userRepository.findByEmail(currentUserEmail);
        return userIds.contains(currentUser.getId());
    }

    private void updateBlockedStatus(Long userId, boolean status) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBlocked(status);
            userRepository.save(user);
        }
    }
}
