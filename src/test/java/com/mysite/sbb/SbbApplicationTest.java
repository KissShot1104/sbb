package com.mysite.sbb;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@SpringBootTest
@Transactional
@Commit
class SbbApplicationTest {

    @Autowired
    private QuestionRepository questionRepository;

    /*@Test
    public void init() {
        for (int i = 0; i < 156; i++) {
            questionRepository.save(Question.builder()
                    .subject("test Subject " + i)
                    .content("test Content " + i)
                    .createDate(LocalDateTime.now())
                    .build());
        }
    }*/

}