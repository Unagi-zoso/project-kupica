package com.litaa.projectkupica.config.auth;

import com.litaa.projectkupica.domain.member.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author : Unagi_zoso
 * @date : 2022-12-31
 */

@Data
public class PrincipalDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Member member;private Map<String, Object> attributes;
    public PrincipalDetails(Member member) {this.member = member;}

    public PrincipalDetails(Member member, Map<String, Object> attributes) {this.member = member;}

    // 권한 : 한개가 아닐 수 있음. (3개 이상의 권한)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {Collection<GrantedAuthority> collector = new ArrayList<>();collector.add(() -> { return member.getRole();});return collector;}

    @Override
    public String getPassword() {return member.getMemberPassword();}

    @Override
    public String getUsername() {return member.getMemberNickname();}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}


}
