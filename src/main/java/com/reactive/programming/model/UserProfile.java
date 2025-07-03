package com.reactive.programming.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private User user;
    private Address address;
}
