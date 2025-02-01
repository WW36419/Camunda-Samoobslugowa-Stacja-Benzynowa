package io.camunda.demo.stacja_benzynowa;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class WybierzRodzajPaliwaWorker {
    @Autowired
    ZeebeClient zeebeClient;
    Scanner scanner = new Scanner(System.in);

    private String getRodzajPaliwa() {
        String rodzaj_paliwa;
        while (true) {
            System.out.println("Wybierz rodzaj paliwa (95, 98, ON):");
            rodzaj_paliwa = scanner.nextLine();
            if (Objects.equals(rodzaj_paliwa, "95") || Objects.equals(rodzaj_paliwa, "98") || Objects.equals(rodzaj_paliwa, "ON"))
                return rodzaj_paliwa;
            else
                System.out.println("Rodzaj paliwa '" + rodzaj_paliwa + "' nie istnieje! Proszę wybrać inny lub opuścić stację");
        }
    }

    @JobWorker(type = "wybierz-rodzaj-paliwa")
    public void WybierzRodzajPaliwa() {
        String rodzaj_paliwa = getRodzajPaliwa();

        zeebeClient.newPublishMessageCommand()
                .messageName("MsgRodzajPaliwa")
                .correlationKey("rodzaj_paliwa_key1")
                .variables(Map.of("rodzaj_paliwa", rodzaj_paliwa))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'wybierz-rodzaj-paliwa'\n", throwable);
                });
    }
}
