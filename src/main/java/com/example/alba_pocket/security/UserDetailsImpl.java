package com.example.alba_pocket.security;

import com.example.alba_pocket.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    //인증이 완료된 사용자 추가
    private final User user; // 인증완료된 User 객체
    private final String username; // 인증완료된 User의 ID
    private final String password; // 인증완료된 User의 PWD
    private final String nickName;


    ////사용자의 권한 GrantedAuthority 로 추상화 및 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//          UserRoleEnum role = user.getRole();
//          String authority = role.getAuthority();
        // ROLE_ADMIN 이 없음. 다 기본유저
        String authority = "ROLE_USER";

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }
    ////사용자의 권한 GrantedAuthority 로 추상화 및 반환
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
