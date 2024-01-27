1. Neovlašćen pristup repozitorijumima ispita:

Rizik: Studenti mogu dobiti neovlašćen pristup repozitorijumima ispita drugih studenata.
Eliminacija/Ublažavanje:
Implementirati snažne mehanizme autentifikacije na Git serveru, dodati random genersian kod ID-u studenda kao sufix.
Koristiti kontrolu pristupa (npr. dozvole za repozitorijume) kako bi ograničili koji studenti mogu klonirati i dostavljati rešenja.
Redovno vršiti reviziju i pratiti dnevnike pristupa radi identifikacije sumnjive aktivnosti.

2. Manipulacija koda u IntelliJ IDEA pluginu:

Rizik: Zlonamerni studenti mogu pokušati manipulisati ili narušiti IntelliJ IDEA plugin kako bi varali tokom ispita.
Eliminacija/Ublažavanje:
Implementirati potpisivanje koda za IntelliJ IDEA plugin.
Redovno ažurirati i zakrpljivati plugin kako bi se rešili bezbednosne ranjivosti.
Koristiti alate za statičku analizu koda kako biste skenirali potencijalne bezbednosne probleme u kodu plugina, npr SonarQube.

3. Nesiguran REST API:

Rizik: REST API može biti podložan neovlašćenom pristupu, narušavanju podataka ili drugim napadima.
Eliminacija/Ublažavanje:
Implementirati snažne mehanizme autentifikacije (npr. OAuth tokeni, API ključevi) za REST API.
Koristiti HTTPS za enkripciju podataka koji se prenose između klijenata i API-ja.
Validirati i čistiti korisničke unose kako biste sprečili SQL injection i druge vrste napada ubrizgavanjem.
Implementirati logovanje i redovno vršiti reviziju logova API-ja radi identifikacije sumnjivih aktivnosti.

4. Bezbednost baze podataka:

Rizik: Nedovoljne bezbednosne mere na PostgreSQL bazi podataka mogu dovesti do curenja podataka ili neovlašćenih izmena.
Eliminacija/Ublažavanje:
Implementirati odgovarajuću autentifikaciju i autorizaciju za pristup bazi podataka.
Enkriptovati osetljive podatke koji se čuvaju u bazi podataka.
Redovno primenjivati sigurnosne zakrpe za sistem upravljanja bazom podataka.
Sprovoditi redovne bezbednosne revizije i procene ranjivosti na bazi podataka.

5. Nedovoljna autentifikacija i autorizacija korisnika:

Rizik: Slabe ili kompromitovane korisničke akreditacije mogu dovesti do neovlašćenog pristupa sistemu.
Eliminacija/Ublažavanje:
Nametnuti jake politike šifri za studentske naloge.
Implementirati višestruku autentifikaciju (MFA) za dodatnu sigurnost.
Redovno pregledavati i ažurirati dozvole za pristup korisnika na osnovu uloga i odgovornosti.

6. Nesigurni kanali komunikacije:

Rizik: Podaci koji se prenose između komponenti mogu biti presretnuti ako kanali komunikacije nisu pravilno obezbeđeni.
Eliminacija/Ublažavanje:
Koristiti sigurne protokole poput HTTPS za komunikaciju između IntelliJ IDEA plugina, Git servera i REST API-ja.
Implementirati sigurnosne mere na nivou mreže, poput firewall-a, radi ograničavanja neovlašćenog pristupa.

7. Briga o privatnosti podataka:

Rizik: Podaci studenata mogu biti izloženi ili nepravilno obrađivani, što može izazvati zabrinutost u vezi sa privatnošću.
Eliminacija/Ublažavanje:
Implementirati anonimizaciju podataka gde je to moguće.
Pridržavati se regulativa o zaštiti podataka (npr. GDPR) i najboljih praksi u vezi sa privatnošću.
Redovno sprovoditi procene uticaja na privatnost kako biste identifikovali i rešili potencijalne rizike.

8. Integritet procesa ocenjivanja:

Rizik: Proces ocenjivanja može biti ugrožen ako su korisnički nalozi profesora kompromitovani ili ako su podaci o ocenjivanju narušeni.
Eliminacija/Ublažavanje:
Implementirati sigurnu autentifikaciju za profesore.
Ograničiti pristup funkcionalnostima ocenjivanja na osnovu logina.
Zabeležiti aktivnosti ocenjivanja i redovno pregledavati zapise radi identifikacije anomalija.

9. Fizička sigurnost servera:

Rizik: Server koji hostuje Git server i REST API fizički se nalazi na univerzitetskom mestu, što stvara rizik od neovlašćenog fizičkog pristupa.
Eliminacija/Ublažavanje:
Obezbediti da server bude smešten na sigurnom, pristupom kontrolisanom mestu.
Implementirati fizičke sigurnosne mere, kao što su zaključane server sobe.
Ograničiti pristup ovlašćenom osoblju i pratiti dnevne zapise fizičkog pristupa.

10. Redovne bezbednosne revizije i ažuriranja:

Rizik: Nemanje redovnih revizija i ažuriranja sistema može ga učiniti ranjivim na promenljive pretnje.
Eliminacija/Ublažavanje:
Sprovoditi redovne bezbednosne revizije radi identifikacije i rešavanja potencijalnih ranjivosti.
Držati sve komponente sistema, uključujući softver i biblioteke, ažurirane sigurnosnim zakrpama.