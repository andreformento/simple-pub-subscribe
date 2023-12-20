package com.andreformento.pubsubcribe;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;
@SpringBootApplication
public class PubsubcribeApplication {
	private final Logger logger = LoggerFactory.getLogger(PubsubcribeApplication.class);

	private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

	public static void main(String[] args) {
		SpringApplication.run(PubsubcribeApplication.class, args);
	}

	@Bean
	public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
		return new DefaultErrorHandler(
				new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
	}

	@Bean
	public RecordMessageConverter converter() {
		return new JsonMessageConverter();
	}

	@KafkaListener(id = "fooGroup", topics = "USER_EVALUATION_SUBMITTED")
	public void listen(Employee foo) {
		logger.info("Received: " + foo);
//		if (foo.getFoo().startsWith("fail")) {
//			throw new RuntimeException("failed");
//		}
		this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
	}

	@KafkaListener(id = "dltGroup", topics = "USER_EVALUATION_SUBMITTED.DLT")
	public void dltListen(byte[] in) {
		logger.info("Received from DLT: " + new String(in));
		this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
	}

	@Bean
	public NewTopic topic() {
		return new NewTopic("USER_EVALUATION_SUBMITTED", 1, (short) 1);
	}

	@Bean
	public NewTopic dlt() {
		return new NewTopic("USER_EVALUATION_SUBMITTED.DLT", 1, (short) 1);
	}

	@Bean
	@Profile("default") // Don't run from test(s)
	public ApplicationRunner runner() {
		return args -> {
			System.out.println("Hit Enter to terminate...");
			System.in.read();
		};
	}

}