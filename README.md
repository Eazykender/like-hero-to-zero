
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

## verwendete Technologien und Werkzeuge

- Java
- Jakarta Faces (JSF)
- Jakarta CDI
- Jakarta Persistence (JPA)
- Hibernate ORM
- MySQL
- Apache TomEE
- Maven
- HTML und CSS

## Datenquelle

Die verwendeten Ausgangsdaten stammen von:

[Our World in Data – Annual CO₂ Emissions](https://ourworldindata.org/grapher/annual-co2-emissions-per-country)

Die Daten enthalten CO₂-Emissionen aus fossilen Brennstoffen und
industriellen Prozessen. Emissionen aus Landnutzungsänderungen sind nicht
enthalten.

Die Quelldaten werden in Tonnen bereitgestellt und beim Import durch 1.000
geteilt, damit die Anwendung die Werte in Kilotonnen speichert und darstellt.

Die CSV-Datei befindet sich unter:

```text
src/main/resources/data/annual-co2-emissions-per-country.csv

## IU
## Iskender Dumlu 
## IPWA2