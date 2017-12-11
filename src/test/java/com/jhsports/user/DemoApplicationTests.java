package com.jhsports.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.logging.java.SimpleFormatter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {

		Date date = new Date();

		long t = date.getTime() - 30 * 60 * 1000;

		System.out.println(t);

		Date date1 = new Date(t);

		System.out.println(date1.getTime());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		String d = simpleDateFormat.format(date1);

		System.out.println(d);
	}

}
