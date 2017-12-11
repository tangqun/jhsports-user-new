package com.jhsports.user;

import com.jhsports.user.entity.App;
import com.jhsports.user.mapper.AppMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@EnableTransactionManagement
@MapperScan("com.jhsports.user.mapper")
@SpringBootApplication
public class DemoApplication {
	@Autowired
	private AppMapper appMapper;

	@Bean
	public List<App> getAll() {
		return appMapper.selectAll();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
