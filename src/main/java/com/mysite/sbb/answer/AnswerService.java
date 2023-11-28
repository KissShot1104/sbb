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
    
    public Answer getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        this.answerRepository.save(answer);
    }
    
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
    
    public void vote(Answer answer, SiteUserForm siteUserForm) {
        SiteUser siteUser = userService.siteUserFormToSiteUser(siteUserForm);
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}