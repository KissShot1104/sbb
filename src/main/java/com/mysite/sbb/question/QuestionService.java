package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.user.SiteUserForm;
import com.mysite.sbb.user.UserService;
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
    private final UserService userService;
    private final AnswerService answerService;

    public Page<Question> getList(int page, String kw) {
    	List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }


    public Page<Answer> getAnswerList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        return this.questionRepository.findAnswerAll(pageable);
    }


    public QuestionForm getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);

        if (question.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }

        Set<Long> voter = question.get().getVoter().stream()
                .map(SiteUser::getId)
                .collect(Collectors.toSet());

        SiteUserForm authorForm = userService.siteUserToSiteUserForm(question.get().getAuthor());
        List<AnswerForm> answerForms = question.get().getAnswerList().stream()
                .map(answerService::answerToAnswerForm)
                .toList();



        return QuestionForm.builder()
                .id(question.get().getId())
                .subject(question.get().getSubject())
                .content(question.get().getContent())
                .author(authorForm)
                .answerList(answerForms)
                .createDate(question.get().getCreateDate())
                .modifyDate(question.get().getModifyDate())
                .voter(voter)
                .build();
    }

    @Transactional
    public void create(QuestionForm questionForm, SiteUserForm siteUserForm) {

        SiteUser siteUser = userService.siteUserFormToSiteUser(siteUserForm);

        Question q = Question.builder()
                .subject(questionForm.getSubject())
                .content(questionForm.getContent())
                .author(siteUser)
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
    public void vote(Long questionId, SiteUserForm siteUserForm) {

        Optional<Question> question = questionRepository.findById(questionId);

        if (question.isEmpty()) {
            throw new DataNotFoundException("question not found");
        }

        SiteUser siteUser = userService.siteUserFormToSiteUser(siteUserForm);

        question.get().getVoter().add(siteUser);

        this.questionRepository.save(question.get());
    }

}