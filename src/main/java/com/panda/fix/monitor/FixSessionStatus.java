package com.panda.fix.monitor;

public record FixSessionStatus(String sessionName, String sourceCompId, String targetCompId, String status) {
}
