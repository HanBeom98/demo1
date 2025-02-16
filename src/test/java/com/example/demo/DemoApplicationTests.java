package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("H2 Test Running...");
	}

}
