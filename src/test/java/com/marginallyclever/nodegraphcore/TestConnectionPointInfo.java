package com.marginallyclever.nodegraphcore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestConnectionPointInfo {
    @Test
    public void testArguments() {
        /*
        // these tests are no longer needed because of @Nonnull
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            new ConnectionPointInfo(null,0,ConnectionPointInfo.IN);
        });
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            new ConnectionPointInfo(null,0,ConnectionPointInfo.OUT);
        });
         */
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            new ConnectionPointInfo(new Node("test") {
                @Override
                public void update() {}
            },-1,ConnectionPointInfo.IN);
        });
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            new ConnectionPointInfo(new Node("test") {
                @Override
                public void update() {}
            },0,0);
        });
    }
}
