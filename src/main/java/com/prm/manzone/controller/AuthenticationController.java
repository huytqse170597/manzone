package com.prm.manzone.controller;

import com.prm.manzone.payload.user.ApiResponse;
import com.prm.manzone.payload.user.request.AuthenticationRequest;
import com.prm.manzone.payload.user.request.VerifyTokenRequest;
import com.prm.manzone.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user authentication")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @Operation(summary = "User login", description = "Authenticate user with email and password")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody @Valid AuthenticationRequest request){
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .errors(null)
                .message("Đăng nhập thành công")
                .data(authenticationService.login(request))
                .build()
        );
    }

    @Operation(summary = "Verify token", description = "Verify if a JWT token is valid")
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verifyToken(@RequestBody @Valid VerifyTokenRequest request) {
        return ResponseEntity.ok(
            ApiResponse.builder()
                .success(true)
                .errors(null)
                .message("Xác thực token thành công")
                .data(authenticationService.verifyToken(request.getToken()))
                .build()
        );
    }
}
