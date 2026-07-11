package com.careeros.auth.service;

import com.careeros.auth.dto.request.RegisterRequest;
import com.careeros.auth.dto.response.RegisterResponse;

public interface AuthService {


    RegisterResponse register(RegisterRequest request);


}
