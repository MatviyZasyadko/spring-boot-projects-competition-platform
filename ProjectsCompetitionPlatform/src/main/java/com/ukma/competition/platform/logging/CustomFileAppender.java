package com.ukma.competition.platform.logging;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

@Plugin(name = "CustomFileAppender", category = "Core", elementType = Appender.ELEMENT_TYPE, printObject = true)
public class CustomFileAppender extends AbstractAppender {
    private FileWriter fileWriter;

    protected CustomFileAppender(String name, String filePath, Layout<? extends Serializable> layout, Filter filter) {
        super(name, filter, layout, true);

        try {
            fileWriter = new FileWriter(filePath, true);
        } catch (IOException e) {
            LOGGER.error("Failed to initialize FileWriter for CustomFileAppender", e);
        }
    }

    @Override
    public void append(LogEvent event) {
        if (fileWriter == null) {
            return;
        }

        try {
            String logMessage = new String(getLayout().toByteArray(event));
            fileWriter.write(logMessage);
            fileWriter.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to write log event to file", e);
        }
    }

    @PluginFactory
    public static CustomFileAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("filePath") String filePath,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") Filter filter) {

        if (name == null) {
            LOGGER.error("No name provided for CustomFileAppender");
            return null;
        }

        if (filePath == null) {
            LOGGER.error("No filePath provided for CustomFileAppender");
            return null;
        }

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        return new CustomFileAppender(name, filePath, layout, filter);
    }
}

