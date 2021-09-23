package com.authorization.domain.member.model.entity;

import com.authorization.common.entity.Common;
import com.authorization.domain.member.enums.MemberType;
import com.authorization.domain.memberSocial.model.entity.MemberSocial;
import com.authorization.domain.role.model.entity.Role;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Common {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    private String id;

    private String password;

    private String name;

    private String email;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    private String oauthId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "member_role_mapping", joinColumns = @JoinColumn(name = "memberNo"), inverseJoinColumns = @JoinColumn(name = "roleNo"))
    private List<Role> roles = new ArrayList<>();

    private LocalDateTime todayLogin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "MEMBER_SOCIAL_NO")
    private MemberSocial memberSocial;

    @Builder
    public Member(
            String id, String password, String email, String name,
            MemberType memberType, LocalDateTime todayLogin, Role role
    ) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.memberType = memberType;
        this.roles.add(role);
        this.todayLogin = todayLogin;
    }

    //public Collection<? extends GrantedAuthority> getAuthorities() {
    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
