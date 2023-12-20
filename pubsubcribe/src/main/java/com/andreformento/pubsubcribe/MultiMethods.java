package com.andreformento.pubsubcribe;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Gary Russell
 * @since 5.1
 *
 */
@Component
@KafkaListener(id = "multiGroup", topics = { "USER_EVALUATION_SUBMITTED" })
public class MultiMethods {

	private final TaskExecutor exec = new SimpleAsyncTaskExecutor();


	@KafkaHandler
	public void bar(Employee employee) {
		System.out.println("Received: " + employee);
		terminateMessage();
	}

	@KafkaHandler(isDefault = true)
	public void unknown(Object object) {
		System.out.println("Received unknown: " + object);
		terminateMessage();
	}

	private void terminateMessage() {
		this.exec.execute(() -> System.out.println("Hit Enter to terminate..."));
	}

}
