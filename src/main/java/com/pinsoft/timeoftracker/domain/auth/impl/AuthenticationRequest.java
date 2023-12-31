package com.pinsoft.timeoftracker.domain.auth.impl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {

    private final String email;
    private final String password;

}
