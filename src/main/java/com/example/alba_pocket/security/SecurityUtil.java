package com.example.alba_pocket.security;

import com.example.alba_pocket.entity.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    public static Boolean isLogin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }else {
            return true;
        }
    }

    public static User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("시큐리티 인증정보 없음");
        }

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetailsImpl springSecurityUser = (UserDetailsImpl) authentication.getPrincipal();
            return springSecurityUser.getUser();
        }else {
            return null;
        }
    }

}