package com.mysite.sbb.question;


import com.mysite.sbb.user.QSiteUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.mysite.sbb.answer.QAnswer.answer;
import static com.mysite.sbb.question.QQuestion.question;

public class QuestionRepositoryImpl implements QuestionRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public QuestionRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);

    }
    //어떻게 해야하지? answerList와 author는 form인데

//    @Override
//    public Page<QuestionForm> findAllByKeyword(String kw, Pageable pageable) {
//        return queryFactory
//                .select(new QQuestionForm(
//                        question.subject,
//                        question.answerList,
//                        answer.author,
//                        answer.createDate
//                ))
//                .from(question)
//                .leftJoin(question.author)
//                .where (
//                        questionSubjectContains(kw),
//                        questionContentContains(kw),
//                        questionAuthorContains(kw),
//                        answerContentContains(kw),
//                        answerAuthorContains(kw)
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//    }
//
    @Override
    public Page<Question> findAllByKeyword(String kw, Pageable pageable) {

        QSiteUser u1 = new QSiteUser("u1");
        QSiteUser u2 = new QSiteUser("u2");

        List<Question> questionList = queryFactory
                .selectFrom(question)
                .leftJoin(question.author, u1)
                .leftJoin(answer).on(answer.question.eq(question))
                .leftJoin(answer.author, u2)
                .where(
                        questionSubjectContains(kw)
                                .or(questionContentContains(kw))
                                .or(questionAuthorContains(kw))
                                .or(answerContentContains(kw))
                                .or(answerAuthorContains(kw))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = questionList.size();



        return new PageImpl<>(questionList, pageable, total);
    }




    public BooleanExpression questionSubjectContains(String questionSubjectCond) {
        if (questionSubjectCond == null) {
            return null;
        }
        return question.subject.contains(questionSubjectCond);
    }
    public BooleanExpression questionContentContains(String questionContentCond) {
        if (questionContentCond == null) {
            return null;
        }
        return question.content.contains(questionContentCond);
    }

    public BooleanExpression questionAuthorContains(String questionAuthorCond) {
        if (questionAuthorCond == null) {
            return null;
        }
        return question.author.username.contains(questionAuthorCond);
    }

    public BooleanExpression answerContentContains(String answerContentCond) {
        if (answerContentCond == null) {
            return null;
        }
        return answer.content.contains(answerContentCond);
    }

    public BooleanExpression answerAuthorContains(String answerAuthorCond) {
        if (answerAuthorCond == null) {
            return null;
        }
        return answer.author.username.contains(answerAuthorCond);
    }

}
