
# Like Hero To Zero

Webanwendung zur öffentlichen Darstellung von CO₂-Emissionen nach Ländern.  
Angemeldete Wissenschaftler können Emissionswerte hinzufügen oder korrigieren.

## Zugang

Für die FUnktionsprüfung steht ein vorkonfiguriertes Demokonto zur Verfügung.
Das Konto wird beim ersten Anmeldeversuch automatisch in der lokalen MySQL-Datenbank angelegt.

- Name: Tester
- E-Mail-Adresse: tester@iu.de
- Passwort: LHTZ-Demo-2026!

## Funktion

- öffentliche Darstellung der jeweils neuesten Emissionswerte
- Import von CO₂-Daten aus einer CSV-Datei
- Speicherung der Daten in einer MySQL-Datenbank
- geschützter Wissenschaftsbereich
- Anmeldung mit E-Mail-Adresse und Passwort
- Hinzufügen neuer Emissionswerte
- Korrigieren bestehender Emissionswerte
- Zuordnung einer Änderung zum angemeldeten Wissenschaftler
- responsive Benutzeroberfläche

## Verwendete Technologien und Werkzeuge

- Java (entwickelt mit JDK 21, Maven-Kompilierung: Java11)
- Jakarta EE 10
- Jakarta Faces (JSF)
- Jakarta CDI
- Jakarta Persistence (JPA)
- Hibernate ORM 6.4.10
- MySQL 8.4.11
- MySQL Workbench 8.0.47
- Apache TomEE 10.2.0
- Maven und WAR-Paketierung
- HTML und CSS

## Voraussetzungen

- JDK 21
- Apache Maven
- MySQL Server
- Apache TomEE 10.2.0 WebProfile
- NetBeans 31
- MySQL Workbench

- localhost: 3306

## Projekt herunterladen

git clone https://github.com/Eazykender/like-hero-to-zero.git
cd like-hero-to-zero

Alternativ kann das Repository über GitHub als ZIP-Datei heruntergeladen und entpackt werden.

## Datenbank einrichten

Das Datenbankschema kann in MySQL Workbench mit folgendem Befehl angelegt werden:

CREATE DATABASE like_hero_to_zero
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

Die Tabellen werden beim ersten Start durch Hibernate erzeugt beziehungsweise aktualisiert. Das Schema selbst muss vorher vorhanden sein.

## Datenbankzugang konfigurieren

Die Anwendung liest Benutzername und Passwort der lokalen MySQL-Installation aus Umgebungsvariablen. Unter Windows können diese in PowerShell gesetzt werden:

[Environment]::SetEnvironmentVariable("LHTZ_DB_USER", "MYSQL_BENUTZER", "User")
[Environment]::SetEnvironmentVariable("LHTZ_DB_PASSWORD", "MYSQL_PASSWORT", "User")

MYSQL_BENUTZER und MYSQL_PASSWORT sind durch die eigenen MySQL-Zugangsdaten zu ersetzen. Anschließend müssen NetBeans und TomEE neu gestartet werden, damit sie die Variablen übernehmen.

## Optionales persönliches Wissenschaftlerkonto

Zusätzlich zum Demokonto kann beim Start ein persönliches Konto angelegt werden. Dafür sind folgende Umgebungsvariablen erforderlich:

[Environment]::SetEnvironmentVariable("LHTZ_SCIENTIST_NAME", "VORNAME NACHNAME", "User")
[Environment]::SetEnvironmentVariable("LHTZ_SCIENTIST_EMAIL", "NAME@BEISPIEL.DE", "User")
[Environment]::SetEnvironmentVariable("LHTZ_SCIENTIST_PASSWORD", "EIGENES_PASSWORT", "User")

Sind diese Angaben vollständig vorhanden, wird das Konto bei Bedarf in der lokalen Datenbank angelegt.

## Anwendung mit NetBeans starten

MySQL Server starten.

Das geklonte Projekt in NetBeans über File > Open Project öffnen.

Apache TomEE 10.2.0 unter Tools > Servers einbinden und dem Projekt zuweisen.

Das Projekt mit Clean and Build erstellen.

Das Projekt mit Run auf TomEE bereitstellen.

Die öffentliche Übersicht im Browser aufrufen:

http://localhost:8080/like-hero-to-zero/faces/index.xhtml

Beim ersten Aufruf wird die mitgelieferte CSV-Datei importiert, sofern noch keine Emissionsdatensätze vorhanden sind.

## Build mit Maven

Der Build kann alternativ im Projektverzeichnis ausgeführt werden:

mvn clean package

Nach einem erfolgreichen Build befindet sich die WAR-Datei im Verzeichnis target. Sie kann auf einem kompatiblen Jakarta-EE-Anwendungsserver bereitgestellt werden.

## Demokonto für die Funktionsprüfung

Für die lokale Bewertung steht ein vorkonfiguriertes Demokonto zur Verfügung:

Name: Tester

E-Mail-Adresse: tester@iu.de

Passwort: LHTZ-Demo-2026!

Das Konto wird beim ersten Anmeldeversuch automatisch in der lokalen MySQL-Datenbank angelegt. 

## Datenquelle

Die verwendeten Ausgangsdaten stammen von:

[Our World in Data – Annual CO₂ Emissions](https://ourworldindata.org/grapher/annual-co2-emissions-per-country)

Die Daten enthalten CO₂-Emissionen aus fossilen Brennstoffen und
industriellen Prozessen. Emissionen aus Landnutzungsänderungen sind nicht
enthalten.

Die Werte werden in Tonnen bereitgestellt und beim Import durch 1.000 geteilt, sodass die Anwendung sie in Kilotonnen speichert und darstellt. Die CSV-Datei liegt unter:

src/main/resources/data/annual-co2-emissions-per-country.csv

Projektstruktur

src/main/java/de/iu/likeherotozero/
├── controller   JSF-Controller für Übersicht, Anmeldung und Backend
├── dao          Datenzugriff für Länder, Emissionswerte und Konten
├── model        JPA-Entitäten
├── persistence  Aufbau und Verwaltung der JPA-Verbindung
├── security     Passwort-Hashing und Passwortprüfung
└── service      CSV-Import und Initialisierung der Konten

src/main/webapp/
├── index.xhtml    öffentliche Übersicht
├── login.xhtml    Anmeldeseite
├── backend.xhtml  geschützter Wissenschaftsbereich
└── resources/css  Stylesheet

Fehlerbehebung

Datenbankverbindung schlägt fehl: Prüfen, ob MySQL läuft und LHTZ_DB_USER sowie LHTZ_DB_PASSWORD gesetzt sind.

Schema wird nicht gefunden: Das Schema like_hero_to_zero vor dem Start manuell anlegen.

Emissionswerte fehlen: Die Tabelle emission_records muss beim Erstimport leer sein; anschließend die öffentliche Übersicht neu laden.

Port 8080 ist belegt: Den belegenden Prozess beenden oder den HTTP-Port von TomEE ändern.

Projektkontext

Das Projekt wurde im Rahmen der IU-Fallstudie des Moduls IPWA02-01 entwickelt.

Autor: Iskender Dumlu
## IU
## IPWA2-01