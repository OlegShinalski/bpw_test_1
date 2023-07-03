package com.example.betpawa.exception;

import java.util.Optional;

import com.betpawa.wallet.WalletErrorCode;
import com.betpawa.wallet.WalletExceptionResponse;
import com.google.protobuf.Any;
import com.google.rpc.Status;

import io.grpc.protobuf.StatusProto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessExceptionUtil {

    public static BusinessException transform(Throwable t) {
        Status status = StatusProto.fromThrowable(t);

        BusinessException result;

        Optional<WalletExceptionResponse> exceptionResponse = status.getDetailsList().stream()
                .filter(any -> any.is(WalletExceptionResponse.class))
                .findFirst()
                .map(BusinessExceptionUtil::unpackToWalletExceptionResponse);
        if (exceptionResponse.isPresent()) {
            log.debug("timestamp={}, error={}, message={}",
                    exceptionResponse.get().getTimestamp(), exceptionResponse.get().getErrorCode(), exceptionResponse.get().getMessage());
            result = new BusinessException(exceptionResponse.get().getErrorCode(),
                    exceptionResponse.get().getErrorCode() + " " + exceptionResponse.get().getMessage());
        } else {
            result = new BusinessException(WalletErrorCode.forNumber(status.getCode()), status.getMessage());
        }

        return result;
    }

    @SneakyThrows
    private static WalletExceptionResponse unpackToWalletExceptionResponse(Any any) {
        return any.unpack(WalletExceptionResponse.class);
    }
}
