package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDao {
  @PersistenceContext private EntityManager entityManager;

  public QuestionEntity createQuestion(QuestionEntity questionEntity) {
    entityManager.persist(questionEntity);
    return questionEntity;
  }

  public UserAuthTokenEntity getUserAuthToken(String authorizationToken) {
    try {
      return entityManager
          .createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class)
          .setParameter("accessToken", authorizationToken)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public QuestionEntity getAllQuestions() {
    return null;
    //    return entityManager.createNamedQuery("getAllQuestions",
    // QuestionEntity.class).getResultList();
  }

  public QuestionEntity editQuestion(QuestionEntity questionEntity)
      throws InvalidQuestionException {
    try {
      QuestionEntity existingQuestion =
          entityManager
              .createNamedQuery("questionByUuid", QuestionEntity.class)
              .setParameter("uuid", questionEntity.getUuid())
              .getSingleResult();

      questionEntity.setId(existingQuestion.getId());

      return entityManager.merge(questionEntity);
    } catch (NoResultException nre) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
  }
}
