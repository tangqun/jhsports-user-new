package com.jhsports.user.helper;

import java.util.Random;

/**
 * Created by TQ on 2017/11/23.
 */
public class RandomHelper {
    public static String getRandom(int length) {
        Random random = new Random();

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < length; i++) {
            int rand = random.nextInt(10);

            stringBuffer.append(rand);
        }

        return stringBuffer.toString();
    }
}
