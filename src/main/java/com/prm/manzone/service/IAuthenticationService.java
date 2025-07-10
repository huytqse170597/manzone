package com.prm.manzone.service;


import com.prm.manzone.payload.user.request.AuthenticationRequest;
import com.prm.manzone.payload.user.response.AuthenticationResponse;
import com.prm.manzone.payload.user.response.VerifyTokenResponse;

public interface IAuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);
    VerifyTokenResponse verifyToken(String token);
}
