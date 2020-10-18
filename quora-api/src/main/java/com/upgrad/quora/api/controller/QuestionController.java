package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping
public class QuestionController {
  @Autowired private QuestionService questionService;

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/question/create",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(
      @RequestHeader("authorization") final String authorization,
      final QuestionRequest questionRequest)
      throws AuthorizationFailedException {

    final QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setContent(questionRequest.getContent());
    questionEntity.setDate(ZonedDateTime.now());

    final QuestionEntity createQuestionEntity =
        questionService.createQuestion(questionEntity, authorization.split("Bearer ")[1]);

    QuestionResponse questionResponse =
        new QuestionResponse().id(createQuestionEntity.getUuid()).status("QUESTION CREATED");
    return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
  }

  @RequestMapping(
      method = RequestMethod.GET,
      path = "/question/all",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionDetailsResponse> getAllQuestions(
      @RequestHeader("authorization") final String authorization)
      throws AuthorizationFailedException {
    final QuestionEntity allQuestionEntity =
        questionService.getAllQuestions(authorization.split("Bearer ")[1]);
    QuestionDetailsResponse detailsResponse =
        new QuestionDetailsResponse()
            .content(allQuestionEntity.getContent())
            .id(allQuestionEntity.getUuid());
    return new ResponseEntity<QuestionDetailsResponse>(detailsResponse, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/question/edit/{questionId}",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestionContent(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("questionId") String questionId,
      final QuestionEditRequest questionEditRequest)
      throws AuthorizationFailedException, InvalidQuestionException {

    final QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setUuid(questionId);
    questionEntity.setContent(questionEditRequest.getContent());
    questionEntity.setDate(ZonedDateTime.now());

    QuestionEntity editedQuestion =
        questionService.editQuestion(authorization.split("Bearer ")[1], questionEntity);

    QuestionEditResponse editResponse =
        new QuestionEditResponse().id(editedQuestion.getUuid()).status("QUESTION EDITED");

    return new ResponseEntity<QuestionEditResponse>(editResponse, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.DELETE,
      path = "/question/delete/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("questionId") String questionId)
      throws AuthorizationFailedException, InvalidQuestionException {
    String uuid = questionService.deleteQuestion(authorization.split("Bearer ")[1], questionId);

    QuestionDeleteResponse status =
        new QuestionDeleteResponse().id(uuid).status("Entered question uuid does not exist");

    return new ResponseEntity<>(status, HttpStatus.NO_CONTENT);
  }

  @RequestMapping(
      method = RequestMethod.GET,
      path = "/question/all/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser(
      @RequestHeader("authorization") final String authorization,
      @PathVariable("userId") String userId)
      throws AuthorizationFailedException, UserNotFoundException {
    questionService.getAllQuestionsByUser(userId, authorization.split("Bearer ")[1]);
    return null;
  }
}
