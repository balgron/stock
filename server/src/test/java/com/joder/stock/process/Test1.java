package com.joder.stock.process;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Joder 2020/8/19 22:23
 */
class Test1 {

    @Test
    void test1() {
        System.out.println(ResourceUtil.readStr("./strategyParams.json", Charset.defaultCharset()));
    }

    @Test
    void test2() throws IOException {
        InputStream streamSafe = new FileInputStream("read.day");
        byte[] bytes = streamSafe.readAllBytes();
        System.out.println(new String(bytes));
        FileOutputStream fileOutputStream = new FileOutputStream("read.day");
        fileOutputStream.write("2020-01-01".getBytes());
    }

    @Test
    void test3() throws IOException {
        InputStream streamSafe = new FileInputStream("read.day");
        byte[] bytes = streamSafe.readAllBytes();
        Date date;
        if (bytes.length != 4) {
            date = DateUtil.parseDate("2000-01-01");
        } else {
            long time = NumberUtil.toInt(bytes) * 1000L;
            date = new Date(time);
        }
        String now = DateUtil.formatDate(new Date());
        int i = 1;
        String start;
        List<String> list = new ArrayList<>();
        while ((start = DateUtil.formatDate(DateUtil.offsetDay(date, i++))).compareTo(now) <= 0) {
            list.add(start);
        }
        System.out.println(list);
    }

    @Test
    void test4() {
        System.out.println(DateUtil.dayOfWeek(new Date()));
        System.out.println(DateUtil.dayOfWeek(DateUtil.parse("2020-08-21")));
    }

}
