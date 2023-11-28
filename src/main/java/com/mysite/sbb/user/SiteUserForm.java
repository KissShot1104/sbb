package com.mysite.sbb.user;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUserForm {


    private Long id;

    private String username;

    private String password;

    private String email;
}
