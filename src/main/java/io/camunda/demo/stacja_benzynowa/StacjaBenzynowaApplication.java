package io.camunda.demo.stacja_benzynowa;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Deployment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath:obsluga_stacji_benzynowej.bpmn")
public class StacjaBenzynowaApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(StacjaBenzynowaApplication.class);

	@Autowired
	private ZeebeClient zeebeClient;

	@Override
	public void run(final String... args) {
		var bpmnProcessId = "stacja-benzynowa";
		var event = zeebeClient.newCreateInstanceCommand()
				.bpmnProcessId(bpmnProcessId)
				.latestVersion()
				.send()
				.join();
		LOG.info("started a process instance: {}", event.getProcessInstanceKey());
	}

	public static void main(String[] args) {
		SpringApplication.run(StacjaBenzynowaApplication.class, args);
	}

}
