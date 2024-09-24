package com.polifono.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.configs.credits")
@Data
public class ConfigsCreditsProperties {
    private int creation;
    private int levelCompleted;
    private int gameCompleted;
    private double priceForEachUnityRange01;
    private double priceForEachUnityRange02;
    private double priceForEachUnityRange03;
    private int minBuyCredits;
    private int maxBuyCredits;
}
