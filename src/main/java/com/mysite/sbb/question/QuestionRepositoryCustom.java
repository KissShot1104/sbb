package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepositoryCustom {

    //Page<QuestionForm> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
    Page<Question> findAllByKeyword(String kw, Pageable pageable);
}
