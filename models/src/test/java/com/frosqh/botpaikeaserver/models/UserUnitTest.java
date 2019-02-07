package com.frosqh.botpaikeaserver.models;

import org.junit.Assert;
import org.junit.Test;

public class UserUnitTest {

    @Test
    public void toStringTest(){
        User user = new User(0,"john","","user@mail.xyz","","","","");
        Assert.assertEquals("User : john at user@mail.xyz",user.toString());
    }
}
