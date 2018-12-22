package com.frosqh.botpaikeaserver.ts3api.spellchecker;

import org.junit.Assert;
import org.junit.Test;

public class LevenshteinDistanceUnitTest {

    @Test
    public void sameWordTest(){
        String word = "Paikea";
        Assert.assertEquals(0,LevenshteinDistance.getDistance(word,word));
    }

    @Test
    public void oneSwapLetterTest(){
        String base = "whale";
        String swapped = "whele";
        Assert.assertEquals(1,LevenshteinDistance.getDistance(base,swapped));
    }
}
