package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class WydrukujParagonWorker {
    @Autowired
    ZeebeClient zeebeClient;
    @JobWorker(type = "wydrukuj-paragon")
    public void WydrukujParagon(@Variable(name = "kwota_zaplacona") Double kwota_zaplacona) {
        System.out.println("----Paragon----\n Zapłacono: "+ kwota_zaplacona + "zł");

        zeebeClient.newPublishMessageCommand()
                .messageName("MsgParagon")
                .correlationKey("paragon_key")
                .variables(Map.of("kwota_zaplacona", kwota_zaplacona))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'wydrukuj-paragon'\n", throwable);
                });
    }
}
