package com.mysite.sbb.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -468377499L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final com.mysite.sbb.QDateTime _super = new com.mysite.sbb.QDateTime(this);

    public final ListPath<com.mysite.sbb.answer.Answer, com.mysite.sbb.answer.QAnswer> answerList = this.<com.mysite.sbb.answer.Answer, com.mysite.sbb.answer.QAnswer>createList("answerList", com.mysite.sbb.answer.Answer.class, com.mysite.sbb.answer.QAnswer.class, PathInits.DIRECT2);

    public final com.mysite.sbb.user.QSiteUser author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath subject = createString("subject");

    public final SetPath<com.mysite.sbb.user.SiteUser, com.mysite.sbb.user.QSiteUser> voter = this.<com.mysite.sbb.user.SiteUser, com.mysite.sbb.user.QSiteUser>createSet("voter", com.mysite.sbb.user.SiteUser.class, com.mysite.sbb.user.QSiteUser.class, PathInits.DIRECT2);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.mysite.sbb.user.QSiteUser(forProperty("author")) : null;
    }

}

