package net.mcon.citnow.line.app;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
@RestController
public class LineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LineApplication.class, args);
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		System.out.println("event: " + event);
		System.out.println("source: " + event.getSource().getUserId() + " | " + event.getSource().getClass());
		final String originalMessageText = event.getMessage().getText();
		return new TextMessage(originalMessageText);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

	@Autowired
	LineMessagingClient client;

	@GetMapping("/api/push")
	public String pushMessageToTester(@RequestParam String userId, @RequestParam String name,
			@RequestParam String phone) throws InterruptedException, ExecutionException {
		System.out.println(" ===== " + userId + " | " + name + " | " + phone);
		if(StringUtils.isAnyBlank(userId, name, phone)) {
			System.out.println("parameters are not correct!");
			return "FAILURE";
		}
		System.out.println("send out text message to " + name);
		PushMessage msg = new PushMessage(userId, Arrays.asList(new TextMessage("Thank you, " + name  + " !")));
		CompletableFuture<BotApiResponse> response = client.pushMessage(msg);
		if (response != null) {
			return response.get().getMessage();
		}
		return "why I am here?";
	}
}
