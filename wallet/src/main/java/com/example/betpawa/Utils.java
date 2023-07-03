package com.example.betpawa;

import java.time.Instant;

import com.google.protobuf.Timestamp;

public class Utils {

    public static Timestamp getTimestamp() {
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        return timestamp;
    }

}
