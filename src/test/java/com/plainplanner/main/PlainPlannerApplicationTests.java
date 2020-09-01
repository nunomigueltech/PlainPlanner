package com.plainplanner.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EntityScan("com.plainplanner.entities")
class PlainPlannerApplicationTests {

	@Test
	void contextLoads() {
	}

}
