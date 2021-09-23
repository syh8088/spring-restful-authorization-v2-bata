package com.authorization.common.config.handler;


import com.authorization.common.config.authentication.UserDetailsImpl;
import com.authorization.domain.member.model.entity.Member;
import com.authorization.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceHandler implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByIdAndUseYn(username, true);

        if (member == null) {
            throw new UsernameNotFoundException("unsername not found");
        }

        List<SimpleGrantedAuthority> grants = member.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(member.getMemberNo())
                .username(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .memberType(member.getMemberType())
                .authorities(member.getAuthorities())
                .build();

        return userDetails;
    }
}
