package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(UserCreateForm userCreateForm) {

        SiteUser user = SiteUser.builder()
                .username(userCreateForm.getUsername())
                .email(userCreateForm.getEmail())
                .password(passwordEncoder.encode(userCreateForm.getPassword1()))
                .build();

        userRepository.save(user);
    }
    
    public SiteUserForm getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);

        if (siteUser.isEmpty()) {
            throw new DataNotFoundException("siteuser not found");
        }

        return SiteUserForm.builder()
                .id(siteUser.get().getId())
                .password(siteUser.get().getPassword())
                .username(siteUser.get().getUsername())
                .email(siteUser.get().getEmail())
                .build();
    }

    public SiteUser siteUserFormToSiteUser(SiteUserForm siteUserForm) {
        return SiteUser.builder()
                .id(siteUserForm.getId())
                .username(siteUserForm.getUsername())
                .password(siteUserForm.getPassword())
                .email(siteUserForm.getEmail())
                .build();
    }

    public SiteUserForm siteUserToSiteUserForm(SiteUser siteUser) {
        return SiteUserForm.builder()
                .id(siteUser.getId())
                .username(siteUser.getUsername())
                .password(siteUser.getPassword())
                .email(siteUser.getEmail())
                .build();
    }


}