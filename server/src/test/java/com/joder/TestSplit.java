package com.joder;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joder 2020/8/29 16:27
 */
class TestSplit {

    @Test
    void test1() {
        String str = "var hq_str_sh601006=\"大秦铁路,6.580,6.580,6.600,6.620,6.540,6.600,6.610,23594267,155455819.000,662375,6.600,405000,6.590,248900,6.580,212600,6.570,394000,6.560,30100,6.610,884440,6.620,797380,6.630,593400,6.640,806600,6.650,2020-08-28,15:00:00,00,\";\n\r" +
                "var hq_str_sh601006=\"大秦铁路1,6.580,6.580,6.600,6.620,6.540,6.600,6.610,23594267,155455819.000,662375,6.600,405000,6.590,248900,6.580,212600,6.570,394000,6.560,30100,6.610,884440,6.620,797380,6.630,593400,6.640,806600,6.650,2020-08-28,15:00:00,00,\";";
        Pattern pattern = Pattern.compile("var .*_.*_(.*)=\"(.*)\";");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.groupCount());
            System.out.println(matcher.group(1) + "\t"+matcher.group(2));
        }
    }
}
