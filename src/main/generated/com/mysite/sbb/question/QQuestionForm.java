package com.mysite.sbb.question;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.mysite.sbb.question.QQuestionForm is a Querydsl Projection type for QuestionForm
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QQuestionForm extends ConstructorExpression<QuestionForm> {

    private static final long serialVersionUID = -1707743543L;

    public QQuestionForm(com.querydsl.core.types.Expression<String> subject, com.querydsl.core.types.Expression<? extends java.util.List<com.mysite.sbb.answer.AnswerForm>> answerList, com.querydsl.core.types.Expression<? extends com.mysite.sbb.user.SiteUserForm> author, com.querydsl.core.types.Expression<java.time.LocalDateTime> createDate) {
        super(QuestionForm.class, new Class<?>[]{String.class, java.util.List.class, com.mysite.sbb.user.SiteUserForm.class, java.time.LocalDateTime.class}, subject, answerList, author, createDate);
    }

}

