package net.mcon.citnow.line.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import net.mcon.citnow.line.app.model.VideoInfo;

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
		if (StringUtils.isAnyBlank(userId, name, phone)) {
			System.out.println("parameters are not correct!");
			return "FAILURE";
		}
		System.out.println("send out text message to " + name);
		PushMessage msg = new PushMessage(userId, Arrays.asList(new TextMessage("Thank you, " + name + " !")));
		CompletableFuture<BotApiResponse> response = client.pushMessage(msg);
		if (response != null) {
			return response.get().getMessage();
		}
		return "why I am here?";
	}

	static Map<String, String> userMap = new HashMap<>();
	{
		userMap.put("13520346656", "U55dbae93eeaee85433eeb60d77461e0b");
	}

	@PostMapping("/api/send")
	public boolean sendNotification(@RequestBody VideoInfo videoInfo) throws InterruptedException, ExecutionException {
		if (videoInfo != null && !StringUtils.isAnyBlank(videoInfo.getChannelId(), videoInfo.getUserPhone(),
				videoInfo.getVideoUrl(), videoInfo.getPicUrl())) {
			System.out.println(" ===== " + videoInfo);
			//TODO: get userId by Phone
			PushMessage msg = new PushMessage("U55dbae93eeaee85433eeb60d77461e0b",
					Arrays.asList(new VideoMessage(videoInfo.getVideoUrl(), videoInfo.getPicUrl())));
			CompletableFuture<BotApiResponse> response = client.pushMessage(msg);
			if (response != null) {
				return StringUtils.isNotBlank(response.get().getMessage());
			}
		}
		return false;
	}
}
