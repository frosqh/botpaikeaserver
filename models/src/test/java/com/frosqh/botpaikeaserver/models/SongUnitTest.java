package com.frosqh.botpaikeaserver.models;

import org.junit.Assert;
import org.junit.Test;

public class SongUnitTest {

    @Test
    public void toStringTest(){
        Song song = new Song(0,"Never Gonna Give You Up","Rick Astley","","");
        Assert.assertEquals("Rick Astley - Never Gonna Give You Up",song.toString());
    }
}
