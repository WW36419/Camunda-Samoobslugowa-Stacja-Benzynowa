package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Component
public class WlozLojalnosciowaWorker {
    @Autowired
    ZeebeClient zeebeClient;
    Scanner scanner = new Scanner(System.in);
    @JobWorker(type = "wloz-lojalnosciowa")
    public void WlozLojalnosciowa() {
        boolean karta = false;
        boolean kupon = false;

        System.out.println("Masz karte lojalnościową? (y/n)");
        karta = Objects.equals(scanner.nextLine(), "y");
        if (karta) {
            System.out.println("Masz aktywny kupon w karcie? (y/n)");
            kupon = Objects.equals(scanner.nextLine(), "y");
        }

        zeebeClient.newPublishMessageCommand()
                .messageName("MsgKartaLojalnosciowa")
                .correlationKey("")
                .variables(Map.of("karta", karta))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'wloz-lojalnosciowa' nr.1 \n", throwable);
                });

        zeebeClient.newPublishMessageCommand()
                .messageName("MsgKartaKupon")
                .correlationKey("kupon_key")
                .variables(Map.of("kupon", kupon))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'wloz-lojalnosciowa' nr.2 \n", throwable);
                });
    }
}
