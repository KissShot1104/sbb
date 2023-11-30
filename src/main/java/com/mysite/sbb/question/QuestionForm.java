package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUserForm;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
public class QuestionForm {

    Long id;

    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;

    @NotEmpty(message="내용은 필수항목입니다.")
    private String content;

    //이거 수정해야함
    private List<AnswerForm> answerList;

    private LocalDateTime createDate;
    
    private LocalDateTime modifyDate;

    private SiteUserForm author;

    private Set<Long> voter;

    @QueryProjection
    public QuestionForm(String subject, List<AnswerForm>answerList, SiteUserForm author,LocalDateTime createDate) {
        this.subject = subject;
        this.author = author;
        this.createDate = createDate;
        this.answerList = answerList;
    }

}