package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
  @Autowired private QuestionDao questionDao;
  @Autowired private UserDao userDao;

  @Transactional(propagation = Propagation.REQUIRED)
  public QuestionEntity createQuestion(
      QuestionEntity questionEntity, final String authorizationToken)
      throws AuthorizationFailedException {
    UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);
    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "User is signed out.Sign in first to post a question");
    }

    questionEntity.setUser(userAuthTokenEntity.getUser());
    return questionDao.createQuestion(questionEntity);
  }

  public QuestionEntity getAllQuestions(final String authorization)
      throws AuthorizationFailedException {
    UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorization);
    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "'User is signed out.Sign in first to get all questions");
    }

    return questionDao.getAllQuestions().get(0);
  }

  public QuestionEntity editQuestion(final String authorizationToken, QuestionEntity questionEntity)
      throws InvalidQuestionException, AuthorizationFailedException {
    UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);
    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "'User is signed out.Sign in first to get all questions");
    }

    return questionDao.editQuestion(questionEntity);
  }

  public String deleteQuestion(final String authorizationToken, String questionId)
      throws InvalidQuestionException, AuthorizationFailedException {
    UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorizationToken);
    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "'User is signed out.Sign in first to get all questions");
    }

    return questionDao.deleteQuestion(questionId);
  }

  public List<QuestionEntity> getAllQuestionsByUser(String userId, String authorization)
          throws AuthorizationFailedException, UserNotFoundException {
    UserAuthTokenEntity userAuthTokenEntity = questionDao.getUserAuthToken(authorization);
    UserEntity requestedUser = userDao.getUserByUuid(userId);
    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    } else if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "'User is signed out.Sign in first to get all questions");
    }else if(requestedUser == null){
      throw new UserNotFoundException(
              "USR-001", "User with entered uuid whose question details are to be seen does not exist");
    }

    return questionDao.getAllQuestionsByUser(userId);
  }
}
