package com.mysite.sbb.question;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

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
    private List<Answer> answerList;

    private LocalDateTime createDate;
    
    private LocalDateTime modifyDate;

    private SiteUser author;

    private Set<Long> voter;
}