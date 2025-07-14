package com.prm.manzone.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.prm.manzone.entities.User;
import com.prm.manzone.payload.user.request.AuthenticationRequest;
import com.prm.manzone.payload.user.response.AuthenticationResponse;
import com.prm.manzone.payload.user.response.VerifyTokenResponse;
import com.prm.manzone.repository.UserRepository;
import com.prm.manzone.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String secretKey;
    @NonFinal
    @Value("${jwt.validDuration}")
    protected Long duration;

    @Override
    public VerifyTokenResponse verifyToken(String token) {
        boolean isValid = true;
        try {
            SignedJWT signedJWT = verifyAuthToken(token);
        } catch (Exception e) {
            isValid = false;
        }
        return VerifyTokenResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        // Find user by email
        User authUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // Check if user is active (not deleted)
        if (authUser.getDeleted()) {
            throw new RuntimeException("Tài khoản đã bị vô hiệu hóa");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), authUser.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }
        // Verify active status
        if (!authUser.getActive()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt");
        }
        // Generate token
        String authToken = generateAuthToken(authUser);

        return AuthenticationResponse.builder()
                .token(authToken)
                .build();
    }

    private String generateAuthToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .issuer("manzone")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(duration, ChronoUnit.MINUTES).toEpochMilli()))
                .claim("email", user.getEmail())
                .claim("scope", user.getRole().name()) // Use name() to get just CUSTOMER or ADMIN
                .jwtID(String.valueOf(UUID.randomUUID()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyAuthToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(jwsVerifier);
        if (!(verified && expTime.after(new Date()))) {
            throw new JOSEException("Invalid token");
        }
        return signedJWT;
    }
}
