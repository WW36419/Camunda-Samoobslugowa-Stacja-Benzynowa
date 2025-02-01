package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class WydrukujFaktureWorker {
    @Autowired
    ZeebeClient zeebeClient;
    @JobWorker(type = "wydrukuj-fakture")
    public void WydrukujFakture(@Variable(name = "kwota_zaplacona") Double kwota_zaplacona) {
        System.out.println("----Faktura----\n Zapłacono: "+ kwota_zaplacona + "zł");

        zeebeClient.newPublishMessageCommand()
                .messageName("MsgFaktura")
                .correlationKey("faktura_key")
                .variables(Map.of("kwota_zaplacona", kwota_zaplacona))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'wydrukuj-fakture'\n", throwable);
                });
    }
}
