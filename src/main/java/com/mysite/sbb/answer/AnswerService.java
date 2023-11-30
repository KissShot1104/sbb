package com.mysite.sbb.answer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.user.SiteUserForm;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final UserRepository userRepository;


    public Long create(Long questionId, AnswerForm answerForm, SiteUserForm author) {

        Optional<Question> question = questionRepository.findById(questionId);

        Answer answer = Answer.builder()
                .content(answerForm.getContent())
                .question(question.get())
                .author(userService.siteUserFormToSiteUser(author))
                .build();

        this.answerRepository.save(answer);

        return answer.getId();
    }
    
    public AnswerForm getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);

        if (answer.isEmpty()) {
            throw new DataNotFoundException("answer not found");
        }

        SiteUserForm siteUser = userService.siteUserToSiteUserForm(answer.get().getAuthor());

        Set<Long> voter = answer.get().getVoter().stream()
                .map(SiteUser::getId)
                .collect(Collectors.toSet());


        return AnswerForm.builder()
                .id(answer.get().getId())
                .content(answer.get().getContent())
                .question(answer.get().getQuestion())
                .author(siteUser)
                .voter(voter)
                .build();
    }

    @Transactional
    public void modify(Long answerId ,AnswerForm answerForm) {
        Optional<Answer> answer = answerRepository.findById(answerId);

        if (answer.isEmpty()) {
            throw new DataNotFoundException("Not Found Answer");
        }

        answer.get().modifyAnswer(answerForm);
    }

    @Transactional
    public void delete(Long answerId) {

        Optional<Answer> answer = answerRepository.findById(answerId);

        if (answer.isEmpty()) {
            throw new DataNotFoundException("Not Found Answer");
        }

        this.answerRepository.delete(answer.get());
    }
    
    public void vote(AnswerForm answerForm, SiteUserForm siteUserForm) {
        SiteUser siteUser = userService.siteUserFormToSiteUser(siteUserForm);

        Answer answer = answerFormToAnswer(answerForm);

        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }


    public Answer answerFormToAnswer(AnswerForm answerForm) {
        SiteUser author = userService.siteUserFormToSiteUser(answerForm.getAuthor());

        Set<SiteUser> voter = answerForm.getVoter().stream()
                .map(userRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        return Answer.builder()
                .id(answerForm.getId())
                .content(answerForm.getContent())
                .question(answerForm.getQuestion())
                .author(author)
                .voter(voter)
                .build();
    }

    public AnswerForm answerToAnswerForm(Answer answer) {

        SiteUserForm author = userService.siteUserToSiteUserForm(answer.getAuthor());
        Set<Long> voter = answer.getVoter().stream()
                .map(SiteUser::getId)
                .collect(Collectors.toSet());

        return AnswerForm.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .question(answer.getQuestion())
                .createDate(answer.getCreateDate())
                .modifyDate(answer.getModifyDate())
                .author(author)
                .voter(voter)
                .build();
    }

}