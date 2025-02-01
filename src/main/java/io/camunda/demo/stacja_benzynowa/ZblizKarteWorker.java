package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import io.camunda.zeebe.client.api.response.Topology;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class ZblizKarteWorker {
    @Autowired
    ZeebeClient zeebeClient;

    @JobWorker(type = "zbliz-karte")
    public void ZblizKarte(@Variable(name = "kwota_wstepna") Double kwota_wstepna) {
        zeebeClient.newPublishMessageCommand()
                .messageName("MsgPobranoDoBlokady")
                .correlationKey("")
                .variables(Map.of("kwota_wstepna", kwota_wstepna))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'zbliz-karte'\n", throwable);
                });
    }
}
