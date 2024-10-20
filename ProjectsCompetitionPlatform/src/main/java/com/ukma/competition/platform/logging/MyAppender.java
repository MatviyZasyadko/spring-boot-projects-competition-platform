package com.ukma.competition.platform.logging;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

import java.io.Serializable;

public class MyAppender implements Appender {
    @Override
    public void append(LogEvent event) {
        
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Layout<? extends Serializable> getLayout() {
        return null;
    }

    @Override
    public boolean ignoreExceptions() {
        return false;
    }

    @Override
    public ErrorHandler getHandler() {
        return null;
    }

    @Override
    public void setHandler(ErrorHandler handler) {

    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}
