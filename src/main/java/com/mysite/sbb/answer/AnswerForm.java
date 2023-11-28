package com.mysite.sbb.answer;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.SiteUserForm;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AnswerForm {

    private Long id;

    private String content;

    private Question question;

    private SiteUserForm author;//이름을 author? siteUserForm? siteUser?

    Set<SiteUser> voter;
}