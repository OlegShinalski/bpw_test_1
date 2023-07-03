package com.example.betpawa.exception;

import java.time.Instant;

import com.betpawa.wallet.WalletExceptionResponse;
import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;

import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class BusinessExceptionHandler {

    @GrpcExceptionHandler(BusinessException.class)
    public StatusRuntimeException handleValidationError(BusinessException cause) {

        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();

        WalletExceptionResponse exceptionResponse =
                WalletExceptionResponse.newBuilder()
                        .setErrorCode(cause.getErrorCode())
                        .setMessage(cause.getMessage())
                        .setTimestamp(timestamp)
                        .build();

        Status status = Status.newBuilder()
                .setCode(Code.INVALID_ARGUMENT.getNumber())
                .setMessage(cause.getErrorCode().name())
                .addDetails(Any.pack(exceptionResponse))
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }
}
