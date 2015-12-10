package com.yourblogz.busstop.example;

import org.junit.Test;

import junit.framework.TestCase;

public class DongGuanFileBuslineGeneratorTest extends TestCase {

    @Test
    public void testGenerator() {
        DongGuanFileBuslineGenerator generator = new DongGuanFileBuslineGenerator("classpath:dongguan.bl");
        generator.setConnectTimeout(10000).start();
    }
}
