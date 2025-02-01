Projekt samoobsługowej stacji benzynowej z Camundą (lokalnie)

Wymagania:
- Camunda Modeler Desktop (361 MB) - https://camunda.com/download/modeler/
- Camunda 8 run (wersja 8.6.2 lub nowsza; 1.34 GB) - https://github.com/camunda/camunda/releases/tag/8.6.5
- IntelliJ IDEA lub inne IDE
- JDK 21 lub nowszy

Jak wersje Camundy 8 run lub JDK są nowsze, musisz zmienić wartości "pom.xml" w projekcie.


Otwarcie modelu stacji benzynowej w Camunda Modeler:
1. Otwórz program Camunda Modeler
2. W "File / Open File..." otwórz plik w 
  (stacja_benzynowa/src/main/resources/obsluga_stacji_benzynowej.bpmn)

Uruchamianie Camunda Operate (z Camunda 8 Run)
1. Z terminala przejdź do folderu c8run w wyodrębnionym repozytorium "camunda8-run".
2. W konsoli wpisz "./c8run.exe start"
3. Poczekaj...
4. Strona Camunda Operate powinna się automatycznie otwierać - wpisz:
 - login: demo
 - hasło: demo


Odpalenie projektu:
1. Otwórz projekt "stacja-benzynowa" w IntelliJ IDEA, otwórz terminal i wpisz "mvn spring-boot:run".
2. Na stronie Camunda Operate wyszukaj odpalony proces w zakładcę "Processes".
