package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mysite.sbb.answer.Answer;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String kw) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
    
    public QuestionForm getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);

        if (question.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }

        return QuestionForm.builder()
                .id(question.get().getId())
                .subject(question.get().getSubject())
                .content(question.get().getContent())
                .author(question.get().getAuthor())
                .answerList(question.get().getAnswerList())
                .createDate(question.get().getCreateDate())
                .modifyDate(question.get().getModifyDate())
                .voter(question.get().getVoter())
                .build();
    }

    @Transactional
    public void create(QuestionForm questionForm, SiteUser user) {
        Question q = Question.builder()
                .subject(questionForm.getSubject())
                .content(questionForm.getContent())
                .author(user)
                .build();
        this.questionRepository.save(q);
    }

    @Transactional
    public void modify(Long questionId, QuestionForm questionForm) {

        Optional<Question> question = questionRepository.findById(questionId);

        if (question.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }

        question.get().modifyQuestion(questionForm);

    }
    
    public void delete(Long questionId) {

        Optional<Question> question = questionRepository.findById(questionId);

        if (question.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }

        this.questionRepository.delete(question.get());
    }

    @Transactional
    public void vote(Long questionId, SiteUser siteUser) {

        Optional<Question> question = questionRepository.findById(questionId);

        if (question.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }
        question.get().getVoter().add(siteUser);

        this.questionRepository.save(question.get());
    }

}