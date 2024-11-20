package com.vishal.project.moveInSync.moveInSyncApp;

import com.vishal.project.moveInSync.moveInSyncApp.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MoveInSyncAppApplicationTests {

	@Autowired
	private EmailSenderService emailSenderService;


	@Test
	void contextLoads() {
		emailSenderService.sendEmail("vshlsgite.97.vg@gmail.com",
				"test email",
				"body email");
	}

	@Test
	void sendMultipleEmails() {
		String[] emails= {"vshlsgite.97.vg@gmail.com","vishalgite1056@gmail.com"};
		emailSenderService.sendEmails(emails,
				"test email",
				"body email");
	}

}