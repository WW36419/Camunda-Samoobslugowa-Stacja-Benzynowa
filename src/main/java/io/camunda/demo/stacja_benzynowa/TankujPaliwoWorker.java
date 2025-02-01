package io.camunda.demo.stacja_benzynowa;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.spring.client.annotation.JobWorker;

@Component
public class TankujPaliwoWorker {
    @Autowired
    ZeebeClient zeebeClient;
    private final static Logger LOG = LoggerFactory.getLogger(TankujPaliwoWorker.class);
    Scanner scanner = new Scanner(System.in);

    private double tankuj(double max_kwota) {
        double kwota = 0;
        double moc = max_kwota/10;

        while (true) {
            System.out.println("Kwota tankowania " + kwota + "zł.");
            System.out.println("Napsz 'stop', by przerwać tankowanie. Jak nie, naciśnij Enter.");
            String command = scanner.nextLine();
            if (Objects.equals(command, "stop"))
                return kwota;

            double extra = Math.floor(Math.random()*100)/100;
            kwota += moc + extra;
            kwota = Math.floor(kwota * 100)/100;

            if (kwota > max_kwota-(max_kwota/100))
                return kwota;
            else if (kwota > max_kwota-(max_kwota/10)-(max_kwota/100)) {
                moc = max_kwota/100;
                System.out.println("Zmniejszono moc tankowania.");
            }
        }
    }

    @JobWorker(type = "tankuj-paliwo")
    public void TankujPaliwo(@Variable(name = "kwota_wstepna") Double kwota_wstepna) {
        //Double kwota_do_zaplaty = kwota_wstepna - (((double) Math.round(Math.random()*10))/100);
        Double kwota_do_zaplaty = tankuj(kwota_wstepna);
        LOG.info("Zatankowano paliwo z kwota: {}", kwota_do_zaplaty);

        zeebeClient.newPublishMessageCommand()
                .messageName("MsgKwotaDoZaplaty")
                .correlationKey("kwota_zaplata_key1")
                .variables(Map.of("kwota_do_zaplaty", kwota_do_zaplaty))
                .timeToLive(Duration.ofMinutes(1))
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Problem z procesem 'tankuj-paliwo'\n", throwable);
                });
    }
}
