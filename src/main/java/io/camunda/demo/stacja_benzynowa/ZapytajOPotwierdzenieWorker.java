package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Scanner;

@Component
public class ZapytajOPotwierdzenieWorker {
    @Autowired
    ZeebeClient zeebeClient;
    private final Scanner scanner = new Scanner(System.in);
    @JobWorker(type = "zapytaj-o-potwierdzenie")
    public void ZapytajOPotwierdzenie() {
        System.out.println("Chcesz wydruk potwierdzenia? Jak tak to który rodzaj? (paragon lub faktura)");
        String potwierdzenie = scanner.nextLine();
        zeebeClient.newPublishMessageCommand()
                .messageName("MsgPotwierdzenie")
                .correlationKey("potwierdzenie_key")
                .variables(Map.of("potwierdzenie", potwierdzenie))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'zapytaj-o-potwierdzenie'\n", throwable);
                });
    }
}
