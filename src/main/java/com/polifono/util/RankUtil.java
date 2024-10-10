package com.polifono.util;

import com.polifono.domain.enums.Rank;

public class RankUtil {

    public static Rank getRankByScore(int score) {
        if (score <= 1000) {
            return Rank.WHITE;
        } else if (score <= 3000) {
            return Rank.YELLOW;
        } else if (score <= 5000) {
            return Rank.ORANGE;
        } else if (score <= 6500) {
            return Rank.RED;
        } else if (score <= 9000) {
            return Rank.PURPLE;
        } else if (score <= 14000) {
            return Rank.BROWN;
        } else if (score <= 18000) {
            return Rank.BLACK;
        } else if (score <= 21000) {
            return Rank.COPPER;
        } else if (score <= 24000) {
            return Rank.SILVER;
        } else {
            return Rank.GOLD;
        }
    }
}
