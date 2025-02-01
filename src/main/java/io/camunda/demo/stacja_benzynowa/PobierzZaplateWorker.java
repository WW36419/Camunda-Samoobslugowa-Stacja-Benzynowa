package io.camunda.demo.stacja_benzynowa;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

@Component
public class PobierzZaplateWorker {
    private final static Logger LOG = LoggerFactory.getLogger(PobierzZaplateWorker.class);
    @JobWorker(type = "pobierz-zaplate")
    public Map<String, Double> PobierzZaplate(@Variable(name = "kwota_obliczona") Double kwota_obliczona) {
        LOG.info("Zaplacono ostatecznie: {}", kwota_obliczona);
        return Map.of("kwota_zaplacona", kwota_obliczona);
    }
}
