package com.ukma.competition.platform.logging;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Plugin(name = "CustomLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomLayout extends AbstractStringLayout {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private CustomLayout() {
        super(Charset.defaultCharset());
    }

    @Override
    public String toSerializable(final LogEvent event) {
        Marker marker = event.getMarker();
        String markerString = (marker != null) ? "[" + marker.getName() + "] " : "";

        StringBuilder threadContextInfo = new StringBuilder();
        ReadOnlyStringMap contextData = event.getContextData();
        if (!contextData.isEmpty()) {
            threadContextInfo.append("[");
            int count = 0;
            int size = contextData.toMap().size();
            for (Map.Entry<String, String> entry : contextData.toMap().entrySet()) {
                threadContextInfo.append(entry.getKey()).append("=").append(entry.getValue());
                count++;
                if (count < size) {
                    threadContextInfo.append(", ");
                }
            }
            threadContextInfo.append("]");
        }

        return String.format("Custom LAYOUT [%s] [%-5s] %s %s- %s %s%n",
                LocalDateTime.now().format(TIME_FORMATTER),
                event.getLevel(),
                markerString,
                event.getLoggerName(),
                event.getMessage().getFormattedMessage(),
                threadContextInfo.toString().trim());
    }

    @PluginFactory
    public static CustomLayout createLayout() {
        return new CustomLayout();
    }
}
