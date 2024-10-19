package com.ukma.competition.platform.logging;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Plugin(name = "CustomLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomLayout extends AbstractStringLayout {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private CustomLayout() {
        super(Charset.defaultCharset());
    }

    @Override
    public String toSerializable(final LogEvent event) {
        return String.format("SONYA'S LAYOUT [%s] [%-5s] %s - %s%n",
                LocalDateTime.now().format(TIME_FORMATTER),
                event.getLevel(),
                event.getLoggerName(),
                event.getMessage().getFormattedMessage());
    }

    @PluginFactory
    public static CustomLayout createLayout() {
        return new CustomLayout();
    }

}
