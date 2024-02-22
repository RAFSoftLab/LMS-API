## Uvod

Ovo je open source API razvijen u Java Spring-u, pružajući endpoint-ove za upravljanje informacijama o studentima. API je kontejnerizovan pomoću Dockera, a baza podataka (PostgreSQL) takođe je kontejnerizovana. Celokupno postavljanje može se lako implementirati korišćenjem Docker Compose alata.

### Pretpostavke

Proverite da li imate instalirane sledeće softvere:

Docker (verzija >= 20.0)\
Docker Compose (verzija >= 2.21.0)\
Java Development Kit (JDK) 17 ili novije

Proverite da imate sledecu konfiguraciju unutar application.properties:

```
spring.datasource.url=jdbc:postgresql://172.21.0.2:5432/mydatabase
```

#### Napomena 1:
Kada zelite da napravite docker image za produkciju, potrebno je da gornju konfiguraciju promenite u sledecu:

```
spring.datasource.url=jdbc:postgresql://172.19.0.2:5432/mydatabase
```

### Početak rada

1. Klonirajte repozitorijum:

```
git clone https://github.com/your/repo.git
cd your-repo
```

2. Izgradite projekat i Docker kontejnere i pokrenite ih:

```
./gradlew bootJar   
docker-compose build --no-cache
docker-compose up --force-recreate
```

Docker compose ce automatski preuzeti i pokrenuti najnoviju verziju oficijalnog postgres kontejnera

Sačekajte da se kontejneri pokrenu. Kada su spremni, API bi trebalo da bude dostupan na http://localhost:8091.

#### Napomena 2:

postgressql container mozda na vasoj masini nece biti pokrenut na adresi 172.21.0.2, proverite na kojoj
adresi je pokrenut kontejner koristeci sledece komande nakon sto je kontejner pokrenut

Da biste izlistali pokrenute kontejnere

```
docker ps
```

Pornadjite CONTAINER ID vaseg kontejnera i pokrenite sledecu komandu

```
docker inspect <ID VASEG KONEJNERA>
```

Na dnu konfiguracije potrazite vrednost polja "IPAddress", izgledace otprilike ovako


```
"IPAddress": "172.21.0.2",
```

Dodajte pronadjenu adresu u application.properties:

```
spring.datasource.url=jdbc:postgresql://<IP ADRESA NA KOJOJ JE POKRENUT POSTGRESQL KONTEJNER>:5432/mydatabase
```

Nakon toga ponovite korak broj 2 iz uputstva "Izgradite projekat i Docker kontejnere i pokrenite ih" 



### API Endpoint-ovi

#### Dobijanje svih studenata

URL: /api/v1/students\
Metoda: GET\
Opis: Dobavlja listu svih studenata.\
Odgovor: Lista objekata Student.\

#### Autorizacija studenta

URL: /api/v1/students/{id}/authorize\
Metoda: POST\
Opis: Autorizuje studenta za polaganje ispita.\
Parametri:\
{id}: ID studenta\
Odgovor: JSON objekat koji sadrži informacije o autorizacionom tokenu.\

#### Dobijanje studenta po ID-ju

URL: /api/v1/students/{id}\
Metoda: GET\
Opis: Dobavlja informacije o studentu po ID-ju.\
Parametri:\
{id}: ID studenta\
Odgovor: JSON objekat koji sadrži informacije o studentu.\

#### Kreiranje studenta

URL: /api/v1/students\
Metoda: POST\
Opis: Kreira novog studenta.\
Telo zahteva: Objekat Student\
```
{
    "firstName": "Foo",
    "lastName": "Bar",
    "indexNumber": "100",
    "startYear": "2024",
    "studiesGroup": "120",
    "major": "SI",
    "studyProgram": "M"
}
```
Odgovor: JSON objekat koji sadrži informacije o kreiranom studentu.\

#### Dobijanje repozitorijuma

URL: /api/v1/students/{id}/repository/{token}/exam/{exam}\
Metoda: GET\
Opis: Dobavlja repozitorijum za ispit za studenta.\
Parametri:\
{id}: ID studenta\
{token}: Autorizacioni token\
{exam}: Naziv ispita\
Odgovor: JSON objekat koji sadrži informacije o repozitorijumu za ispit.\

#### Dobijanje naziva forka

URL: /api/v1/students/{id}/repository/{token}/fork\
Metoda: GET\
Opis: Dobavlja naziva forka za repozitorijum studenta.\
Parametri:\
{id}: ID studenta\
{token}: Autorizacioni token\
Odgovor: JSON objekat koji sadrži ime forka.\

#### Zadatak kloniran

URL: /api/v1/students/{id}/task_cloned\
Metoda: POST\
Opis: Označava da je student klonirao zadatak ispita.\
Parametri:\
{id}: ID studenta\
Telo zahteva: Objekat ExamInfo\
```
{
    "taskGroup": "21",
    "classroom": "RAF10"
}
```
Odgovor: JSON objekat koji sadrži ažurirane informacije o studentu.\

#### Zadatak predat

URL: /api/v1/students/{id}/task_submitted\
Metoda: POST\
Opis: Označava da je student predao zadatak ispita.\
Parametri:\
{id}: ID studenta\
Telo zahteva: Objekat TaskSubmissionInfo\
Odgovor: JSON objekat koji sadrži ažurirane informacije o studentu.\

#### Brisanje studenta

URL: /api/v1/students/{id}\
Metoda: DELETE\
Opis: Briše studenta po ID-ju.\
Parametri:\
{id}: ID studenta\
Odgovor: Poruka o uspehu.\

#### Upload studenata

URL: /api/v1/students/upload\
Metoda: POST\
Opis: Uploaduje CSV fajl koji sadrži informacije o studentima.\
Telo zahteva: CSV Fajl koji ukljucuje nazive kolona \
Odgovor: Poruka o uspehu.\

Postmen primer:
![image](https://github.com/RAFSoftLab/LMS-API/assets/43738975/51bfabbe-190f-43bd-8c10-f451135ee333)

Model Studenta

```
{
    "id": "MSI1002024",
    "firstName": "Foo",
    "lastName": "Bar",
    "indexNumber": "100",
    "startYear": "2024",
    "studiesGroup": "120",
    "taskGroup": "21",
    "taskCloned": "true",
    "taskClonedTime": "2024-01-14T12:36:47.928476495",
    "taskSubmitted": "false",
    "taskSubmittedTime": "null",
    "major": "SI",
    "studyProgram": "M",
    "classroom": "RAF10",
    "forkName": "null"
}
```

Slobodno istražujte API i modifikujte ga prema svojim potrebama. Ako naiđete na bilo kakve probleme ili imate sugestije, molimo vas da doprinesete projektu otvaranjem pitanja ili slanjem zahteva za izmenama (pull request).
