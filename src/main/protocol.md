# **Protokoll**

### - technische Schritte
Ich habe erst die Klassen User und Card erstellt. Bei den Cards dachte ich zuerst, <br>
dass eine Card-Klasse genügt, da sie sich lediglich dadurch unterscheiden, <br>
ob sie eine Spell-Karte ist, oder nicht. Daher hatte ich zu Beginn auch keine <br>
Vererbung benutzt.

Als nächstes habe ich die Kampflogik in der Game-Klasse erstellt. Diese konnte ich<br>
bereits ziemlich zu Beginn erstellen, allerdings habe ich die speziellen Kämpfe <br>
wie zB mit KRAKEN/DRAGONS/... erst noch ausgelassen. Als ich diesen Teil auch <br>
programmieren wollte, stellte ich leider fest, dass ich wohl doch mit Vererbung arbeiten<br>
sollte. Also habe ich eine Elternklasse Card erstellt mit den Kindklassen SpellCard und <br>
MonsterCard.

Auch die ersten Unit-Tests habe ich erstellt, diese musste ich später jedoch wieder <br>
anpassen.

Danach habe ich versucht, den Server zu programmieren. Hierbei habe ich mich an <br>
der Vorlage im Moodle gerichtet, da dies ein voll funktionierendes Beispiel ist. <br>

Als Datenbank habe ich eine lokale Postgres Datenbank erstellt.
Bei der DB-Anbindung ist mir aufgefallen, dass es einfach wäre, die Datenbank wie folgt <br>
zu gliedern:
- Tabelle Users:
  - username (String)
  - name (String)
  - password (String)
  - coins (Int)
  - eloValue (Int)
  - bio (String)
  - image (String)
  - lostGames (Int)
  - wonGames (Int)
  - token (String)
- Tabelle Cards:
  - id (String)
  - name (String)
  - cardType (Enum)
  - elementType (Enum)
  - damage (Int)
  - username (String)
  - deck (Boolean)

Durch diese Aufteilung habe ich in der Cards-Tabelle alle Karteninformationen <br>
und muss nicht mit Beziehungen arbeiten. Wird eine neue Karte erstellt, so bleibt <br>
der username leer und deck wird mit false initialisiert. Sobald ein User eine Karte <br>
(bzw ein Package) kauft, ändert sich der Username auf den jetzigen Besitzer und Deck <br>
bleibt immer noch false. Wenn dann der User die Karte in sein Deck aufnimmt, ändert man <br>
einfach deck auf true. Dies erschien mir als eine sehr simple Lösung. Außerdem speichere <br>
ich in der Spalte cardType entweder SPELL oder eben diese Speziellen Monster wie KRAKEN/DRAGON <br>
dadurch musste ich den Aufbau mit der Vererbung der Karten wieder umändern, weil ich ihn so <br>
nicht benötigte. Also gibt es wieder nur eine Klasse Card ohne Kindklassen. <br>

Bei den einzelnen Anfragen ging ich wie folgt vor (ich habe mich grundsätzlich <br>
an das vorhandene CURL-file gerichtet):

- java/app/App
  - hier wird der jeweilige Pfad mit der jeweiligen Rest Methode eingetragen<br>
- java/app/controller
  - hier gibt es je nach Bereich einen UserController, CardController und GameController
- java/app/service
  - hier gibt es je nach Bereich eine UserService, CardService und GameService

Ich habe mir Mühe gegeben, die einzelnen Funktionen jeweils richtig einzuordnen, <br>
jedoch ist es leider nicht immer eindeutig, ob eine Funktion eher zum UserController (samt UserService)<br>
oder doch eher zum CardController (samt CardService) gehören solle.

Nachdem die Aufrufe funktionierten, musste ich am Schluss noch die Kampflogik anpassen. <br>
Zum Trading bin ich leider nicht mehr gekommen.

### - Unit Tests
Ich habe für alle meine Klassen im app/model/ (User, Card, Game) Unit-Test erstellt.
- User-Tests:
  - hier wird eigentlich nur der Constructor, sowie Getter und Setter getestet, da <br>
    die Klasse sonst keine Funktionen hat.
- Card-Tests:
  - hier wird auch nur der Constructor, sowie Getter und Setter getestet, da <br>
    auch die Card-Klasse sonst keine Funktionen hat.
- Game-Tests:
  - hier sind die meisten Unit-Test, da die Kampflogik hier getestet werden soll.
  - ich habe alle Beispiele aus der Angabe als Test geschrieben

### - benötigte Zeit
Die genaue Zeit habe ich nicht notiert. Nachträglich kann ich die Zeit nur abschätzen:
- bis zu den Weihnachtsferien ca. 15 Stunden
- in den Weihnachtsferien ca 20 Stunden
- nach den Weihnachtsferien ca 15 Stunden
- in den Semesterferien für den Zweitantritt ca 30 Stunden

### - Link zu git


