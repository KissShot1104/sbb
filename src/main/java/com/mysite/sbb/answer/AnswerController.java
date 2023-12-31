package com.mysite.sbb.answer;

import java.security.Principal;

import com.mysite.sbb.question.QuestionForm;
import com.mysite.sbb.user.SiteUserForm;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model,
                               @PathVariable("id") Long id,
                               @Validated AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal) {

        QuestionForm question = this.questionService.getQuestion(id);
        SiteUserForm siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        Long answerId = answerService.create(id, answerForm, siteUser);

        return String.format("redirect:/question/detail/%s#answer_%s", 
                id, answerId);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm,
                               @PathVariable("id") Long id,
                               Principal principal) {

        AnswerForm existingAnswer = answerService.getAnswer(id);
        if (!existingAnswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerForm.setContent(existingAnswer.getContent());

        return "answer_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Validated AnswerForm answerForm,
                               BindingResult bindingResult,
                               @PathVariable("id") Long id,
                               Principal principal) {

        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        AnswerForm existingAnswer = answerService.getAnswer(id);
        if (!existingAnswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerService.modify(id, answerForm);

        return String.format("redirect:/question/detail/%s#answer_%s",
                existingAnswer.getQuestion().getId(), existingAnswer.getId());
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal,
                               @PathVariable("id") Long id) {
        AnswerForm answer = answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        answerService.delete(id);

        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal,
                             @PathVariable("id") Long id) {

        AnswerForm answer = this.answerService.getAnswer(id);
        SiteUserForm siteUser = this.userService.getUser(principal.getName());
        answerService.vote(answer, siteUser);

        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());
    }
}