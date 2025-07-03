package com.reactive.programming.repository;

import com.reactive.programming.model.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private final Map<String, User> userDb = new ConcurrentHashMap<>();

    public Flux<User> findAll() {
        return Flux.fromIterable(userDb.values());
    }

    public Mono<User> findById(String id) {
        User user = userDb.get(id);
        return user != null ? Mono.just(user) : Mono.empty();
    }

    public Mono<User> save(User user) {
        userDb.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<Void> deleteById(String id) {
        userDb.remove(id);
        return Mono.empty();
    }
}

