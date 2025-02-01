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
public class ZwrocKwoteWorker {
    @Autowired
    ZeebeClient zeebeClient;
    private final static Logger LOG = LoggerFactory.getLogger(PobierzZaplateWorker.class);

    @JobWorker(type = "zwroc-kwote")
    public void ZwrocKwote(@Variable(name = "kwota_obliczona") Double kwota_obliczona, @Variable(name = "kwota_w_blokadzie") Double kwota_w_blokadzie) {
        Double kwota_zwrocona = kwota_w_blokadzie-kwota_obliczona;

        LOG.info("Zwrocono z blokady: {}", kwota_zwrocona);
        zeebeClient.newPublishMessageCommand()
                .messageName("MsgZwrot")
                .correlationKey("zwrot_key")
                .variables(Map.of("kwota_zwrocona", kwota_zwrocona))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'zwroc-kwote'\n", throwable);
                });
    }
}
