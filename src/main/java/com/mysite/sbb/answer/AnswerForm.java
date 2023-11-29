package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.SiteUserForm;
import jakarta.validation.constraints.NotEmpty;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerForm {

    private Long id;

    @NotEmpty(message = "댓글 내용을 필수입니다.")
    private String content;

    private Question question;

    private SiteUserForm author;//이름을 author? siteUserForm? siteUser?

    Set<SiteUser> voter;
}