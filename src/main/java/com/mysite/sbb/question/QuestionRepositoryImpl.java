package com.mysite.sbb.question;


import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.QSiteUser;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
                .leftJoin(answer).on(answer.question.eq(question)).fetchJoin()
                .leftJoin(answer.author, u2)
                .where(
                        questionSubjectContains(kw)
                                .or(questionContentContains(kw))
                                .or(questionAuthorContains(kw))
                                .or(answerContentContains(kw))
                                .or(answerAuthorContains(kw))
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(question)
                .leftJoin(question.author, u1)
                .leftJoin(answer).on(answer.question.eq(question)).fetchJoin()
                .leftJoin(answer.author, u2)
                .where(
                        questionSubjectContains(kw)
                                .or(questionContentContains(kw))
                                .or(questionAuthorContains(kw))
                                .or(answerContentContains(kw))
                                .or(answerAuthorContains(kw))
                )
                .distinct()
                .fetch().size();


//        long total = questionList.size();//10개밖에 안뽑아오니까 그럼 최대한 끌어오고 나중에 다시 계산

        return new PageImpl<>(questionList, pageable, total);
    }

    @Override
    public Page<Answer> findAnswerAll(Pageable pageable) {

        List<Answer> answerList = queryFactory
                .selectFrom(answer)
                .where(answer.question.eq(question))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(answer)
                .from(answer)
                .where(answer.question.eq(question))
                .stream().count();

        return new PageImpl<>(answerList, pageable, total);
    }



    public BooleanExpression questionSubjectContains(String questionSubjectCond) {
        if (questionSubjectCond == null || questionSubjectCond.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return question.subject.contains(questionSubjectCond);
    }
    public BooleanExpression questionContentContains(String questionContentCond) {
        if (questionContentCond == null || questionContentCond.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return question.content.contains(questionContentCond);
    }

    public BooleanExpression questionAuthorContains(String questionAuthorCond) {
        if (questionAuthorCond == null || questionAuthorCond.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return question.author.username.contains(questionAuthorCond);
    }

    public BooleanExpression answerContentContains(String answerContentCond) {
        if (answerContentCond == null || answerContentCond.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return answer.content.contains(answerContentCond);
    }

    public BooleanExpression answerAuthorContains(String answerAuthorCond) {
        if (answerAuthorCond == null || answerAuthorCond.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return answer.author.username.contains(answerAuthorCond);
    }

}
