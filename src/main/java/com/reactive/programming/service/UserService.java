package com.reactive.programming.service;

import com.reactive.programming.exception.NotFoundException;
import com.reactive.programming.model.Address;
import com.reactive.programming.model.User;
import com.reactive.programming.model.UserProfile;
import com.reactive.programming.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .delayElements(Duration.ofMillis(300)) // Simulate streaming
                .log();
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found with id: " + id)));
    }

    public Mono<User> createUser(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            return Mono.error(new IllegalArgumentException("Name and email are required"));
        }
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public Mono<User> updateUser(String id, User newUser) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .flatMap(existingUser -> {
                    existingUser.setName(newUser.getName());
                    existingUser.setEmail(newUser.getEmail());
                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User not found")))
                .flatMap(user -> userRepository.deleteById(id));
    }

    // Combine with another service (e.g., AddressService)
    public Mono<UserProfile> getUserProfile(String userId) {
        Mono<User> userMono = getUserById(userId);
        Mono<Address> addressMono = getAddressForUser(userId); // Imagine this from another service

        return Mono.zip(userMono, addressMono, (user, address) ->
                UserProfile.builder()
                        .user(user)
                        .address(address)
                        .build());
    }

    private Mono<Address> getAddressForUser(String userId) {
        // Simulate external call
        return Mono.just(Address.builder().street("123 Main St").city("Springfield").zipCode("12345").build())
                .delayElement(Duration.ofMillis(500));
    }
}

