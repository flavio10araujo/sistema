package com.polifono.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.polifono.domain.enums.Rank;

public class RankUtilTest {

    @Test
    public void givenPlayer_WhenGetRankColor_ThenReturnCorrectColor() {
        Assertions.assertEquals(Rank.WHITE, RankUtil.getRankByScore(0));
        Assertions.assertEquals(Rank.WHITE, RankUtil.getRankByScore(1000));
        Assertions.assertEquals(Rank.YELLOW, RankUtil.getRankByScore(1001));
        Assertions.assertEquals(Rank.ORANGE, RankUtil.getRankByScore(3001));
        Assertions.assertEquals(Rank.RED, RankUtil.getRankByScore(5001));
        Assertions.assertEquals(Rank.PURPLE, RankUtil.getRankByScore(6501));
        Assertions.assertEquals(Rank.BROWN, RankUtil.getRankByScore(9001));
        Assertions.assertEquals(Rank.BLACK, RankUtil.getRankByScore(14001));
        Assertions.assertEquals(Rank.COPPER, RankUtil.getRankByScore(18001));
        Assertions.assertEquals(Rank.SILVER, RankUtil.getRankByScore(21001));
        Assertions.assertEquals(Rank.GOLD, RankUtil.getRankByScore(24001));
    }
}
