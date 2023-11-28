package com.mysite.sbb.answer;

import java.util.Optional;

import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.user.SiteUserForm;
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


    public Answer create(Long questionId, String content, SiteUserForm author) {

        Optional<Question> question = questionRepository.findById(questionId);

        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestion(question.get());
        answer.setAuthor(userService.siteUserFormToSiteUser(author));
        this.answerRepository.save(answer);
        return answer;
    }
    
    public AnswerForm getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);

        if (answer.isEmpty()) {
            throw new DataNotFoundException("answer not found");
        }

        SiteUserForm siteUser = userService.siteUserToSiteUserForm(answer.get().getAuthor());

        return AnswerForm.builder()
                .id(answer.get().getId())
                .content(answer.get().getContent())
                .question(answer.get().getQuestion())
                .author(siteUser)
                .voter(answer.get().getVoter())
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
        return Answer.builder()
                .id(answerForm.getId())
                .content(answerForm.getContent())
                .question(answerForm.getQuestion())
                .author(author)
                .voter(answerForm.getVoter())
                .build();
    }




}