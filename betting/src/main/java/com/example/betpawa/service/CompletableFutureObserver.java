package com.example.betpawa.service;

import com.example.betpawa.exception.BusinessException;
import com.example.betpawa.exception.BusinessExceptionUtil;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
public class CompletableFutureObserver<ResponseType, ReturnType> implements StreamObserver<ResponseType> {

    private ResponseType response;
    private CompletableFuture<ReturnType> future;
    private Function<ResponseType, ReturnType> postComplete = (e) -> null;

    public CompletableFutureObserver(Function<ResponseType, ReturnType> postComplete) {
        this.future = new CompletableFuture();
        this.postComplete = postComplete;
    }

    public CompletableFuture getFuture() {
        return future;
    }

    @Override
    public void onNext(ResponseType response) {
        this.response = response;
        log.info("onNext: {}", response);
    }

    @Override
    public void onCompleted() {
        ReturnType result = postComplete.apply(this.response);
        future.complete(result);
    }

    @Override
    public void onError(Throwable t) {
        log.info("Error while reading response fromServer: ", t);
        BusinessException businessException = BusinessExceptionUtil.transform(t);
        future.completeExceptionally(businessException);
    }

}
