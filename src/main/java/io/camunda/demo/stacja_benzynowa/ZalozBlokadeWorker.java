package io.camunda.demo.stacja_benzynowa;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class ZalozBlokadeWorker {
    @Autowired
    ZeebeClient zeebeClient;
    private final static Logger LOG = LoggerFactory.getLogger(PobierzZaplateWorker.class);

    @JobWorker(type = "zaloz-blokade")
    public Map<String, Double> ZalozBlokade(@Variable(name = "kwota_wstepna") Double kwota_wstepna) {
        LOG.info("Zalozono blokade z kwota: {}", kwota_wstepna);
        zeebeClient.newPublishMessageCommand()
                .messageName("MsgZalozonoBlokade")
                .correlationKey("blokada_key")
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'zaloz-blokade'\n", throwable);
                });

        return Map.of("kwota_w_blokadzie", kwota_wstepna);
    }
}
