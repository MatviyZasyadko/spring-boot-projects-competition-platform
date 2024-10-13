package com.ukma.competition.platform.competitions.business_layer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "competition")
public class CompetitionProperties {

    private int minProjects;
    private int maxProjects;

}

