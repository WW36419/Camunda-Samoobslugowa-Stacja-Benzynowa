package io.camunda.demo.stacja_benzynowa;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

@Component
public class WprowadzKwoteWorker {
    private final Scanner scanner = new Scanner(System.in);

    @JobWorker(type = "wprowadz-kwote")
    public Map<String, Double> WprowadzKwote() {
        System.out.println("Wprowadź kwotę do zatankowania:");
        Double kwota_wstepna = scanner.nextDouble();
        return Map.of("kwota_wstepna", kwota_wstepna);
    }
}
