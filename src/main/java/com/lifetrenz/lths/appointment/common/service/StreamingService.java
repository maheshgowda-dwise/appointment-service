package com.lifetrenz.lths.appointment.common.service;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lifetrenz.lths.appointment.common.app.ApplicationResponse;
import com.lifetrenz.lths.appointment.common.app.constant.CommonConstants;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;


@Service
public class StreamingService {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;
    private static final int BATCH_SIZE=400;

    public StreamingService(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }
    
    public <T, D> SseEmitter streamData(Supplier<List<Query>> queryBuilder, Class<T> entityType, Function<T, D> convertToDto) {
        SseEmitter emitter = new SseEmitter(-1L);
      Flux.fromIterable(queryBuilder.get())
              .flatMap(query -> Flux.fromStream(mongoTemplate.stream(query, entityType)))
              .map(convertToDto::apply)
              .buffer(BATCH_SIZE)
                .subscribeOn(Schedulers.boundedElastic())
                .doFinally(signal -> handleFinalSignal(signal, emitter))
                .subscribe(
                        items -> handleItems(items, emitter),
                        error -> handleStreamError(error, emitter)
                );
          return emitter;
    }
    
    public <T, D> SseEmitter streamData(Query query, Class<T> entityType, Function<T,D> convertToDto) {
        SseEmitter emitter = new SseEmitter(-1L);
        Stream<T> stream = mongoTemplate.stream(query, entityType);
        Flux.fromStream(stream)
                .subscribeOn(Schedulers.boundedElastic())
                .map(convertToDto::apply)
                .buffer(BATCH_SIZE)
                .doFinally(signal -> handleFinalSignal(signal, emitter))
                .subscribe(
                        items -> handleItems(items, emitter),
                        error -> handleStreamError(error,emitter)
                );
        return emitter;
    }


   private <D> void handleItems(List<D> items, SseEmitter emitter) {
        try {
            ApplicationResponse<ArrayNode> response = createResponse(items);
            String jsonResponse = objectMapper.writeValueAsString(response);
             emitter.send(SseEmitter.event()
                     .data(jsonResponse, MediaType.APPLICATION_JSON)
                     .build());
        } catch (Exception e) {
            handleStreamError(e, emitter);
        }

    }

    private void handleFinalSignal(SignalType signal, SseEmitter emitter) {
        if (signal == SignalType.ON_COMPLETE) {
            emitter.complete();
        } else if (signal == SignalType.ON_ERROR) {
            try {
                ApplicationResponse<Object> errorResponse = createErrorResponse(new Exception("Error occurred."));
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                 emitter.send(SseEmitter.event()
                         .data(jsonResponse, MediaType.APPLICATION_JSON)
                         .build());
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
            emitter.completeWithError(new Exception("Error occured"));
        }
    }

    private void handleStreamError(Throwable error, SseEmitter emitter){
        try {
           ApplicationResponse<Object> errorResponse = createErrorResponse(new Exception(error));
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
             emitter.send(SseEmitter.event()
                    .data(jsonResponse, MediaType.APPLICATION_JSON)
                    .build());

        } catch (IOException ex) {
            emitter.completeWithError(ex);
        }
            emitter.completeWithError(error);
    }


    private <D> ApplicationResponse<ArrayNode> createResponse(List<D> items) {
          ArrayNode arrayNode = objectMapper.createArrayNode();
         items.forEach(arrayNode::addPOJO);
        return new ApplicationResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK.value()),
                CommonConstants.OK, arrayNode);

    }
    private ApplicationResponse<Object> createErrorResponse(Exception e) {
        return new ApplicationResponse<>(CommonConstants.ERROR,
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage(), null);
    }
}