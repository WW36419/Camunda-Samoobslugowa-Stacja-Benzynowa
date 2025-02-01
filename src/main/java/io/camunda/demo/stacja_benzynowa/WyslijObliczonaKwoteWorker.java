package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class WyslijObliczonaKwoteWorker {
    @Autowired
    ZeebeClient zeebeClient;

    @JobWorker(type = "wyslij-obliczona-kwote")
    public void WyslijObliczonaKwote(@Variable(name = "kwota_do_zaplaty") Double kwota_do_zaplaty) {
        zeebeClient.newPublishMessageCommand()
                .messageName("MsgKwotaObl")
                .correlationKey("kwota_obliczona_key")
                .variables(Map.of("kwota_obliczona", kwota_do_zaplaty))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'wyslij-obliczona-kwote'\n", throwable);
                });
    }
}
