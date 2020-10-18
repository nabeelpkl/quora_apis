package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import java.util.List;

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

  public QuestionEntity getQuestionByQUuid(final String uuid) {
    try {
      return entityManager
          .createNamedQuery("questionByQUuid", QuestionEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public List<QuestionEntity> getAllQuestionsByUser(final String uuid) {
    try {
      return entityManager
          .createNamedQuery("allQuestionsByUserId", QuestionEntity.class)
          .setParameter("uuid", uuid)
          .getResultList();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public List<QuestionEntity> getAllQuestions() {
    try {
      return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
    } catch (NoResultException nre) {

      return null;
    }
  }

  public QuestionEntity editQuestion(QuestionEntity questionEntity)
      throws InvalidQuestionException {
    try {
      QuestionEntity existingQuestion =
          entityManager
              .createNamedQuery("questionByQUuid", QuestionEntity.class)
              .setParameter("uuid", questionEntity.getUuid())
              .getSingleResult();

      questionEntity.setId(existingQuestion.getId());

      return entityManager.merge(questionEntity);
    } catch (NoResultException nre) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
  }

  public String deleteQuestion(final String uuid) throws InvalidQuestionException {
    try {
      QuestionEntity questionEntity = getQuestionByQUuid(uuid);
      entityManager.remove(questionEntity);
      return uuid;
    } catch (NoResultException nre) {
      throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
  }

  /**
   * Method to get the QuestionEntity by uuid
   *
   * @param questionId
   * @return QuestionEntity
   */
  public QuestionEntity getQuestionById(String questionId) {
    try {
      return entityManager
          .createNamedQuery("questionById", QuestionEntity.class)
          .setParameter("uuid", questionId)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }
}
