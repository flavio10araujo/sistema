package com.polifono.common.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "app.configs.credits")
public class ConfigsCreditsProperties {

    @Getter private static int creation;
    @Getter private static int levelCompleted;
    @Getter private static int gameCompleted;
    @Getter private static double priceForEachUnityRange01;
    @Getter private static double priceForEachUnityRange02;
    @Getter private static double priceForEachUnityRange03;
    @Getter private static int minBuyCredits;
    @Getter private static int maxBuyCredits;

    @Value("${app.configs.credits.buy.min}")
    public void setMinBuyCredits(int minBuyCredits) {
        ConfigsCreditsProperties.minBuyCredits = minBuyCredits;
    }

    @Value("${app.configs.credits.buy.max}")
    public void setMaxBuyCredits(int maxBuyCredits) {
        ConfigsCreditsProperties.maxBuyCredits = maxBuyCredits;
    }
}
