## Uvod

Ovo je open source API razvijen u Java Spring-u, pružajući endpoint-ove za upravljanje informacijama o studentima. API je kontejnerizovan pomoću Dockera, a baza podataka (PostgreSQL) takođe je kontejnerizovana. Celokupno postavljanje može se lako implementirati korišćenjem Docker Compose alata.

### Pretpostavke

Proverite da li imate instalirane sledeće softvere:

Docker (verzija >= 20.0)\
Docker Compose (verzija >= 2.21.0)\
Java Development Kit (JDK) 17 ili novije

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

### UML diagram

![image](https://github.com/RAFSoftLab/LMS-API/assets/43738975/77a0ff99-9c5e-40a1-849f-8ec76545ce50)

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
Napomena: Potrebno je da CSV Fajl sadrzi sledece zaglavlja: "Id", "Ime", "Prezime"

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

# Sistem za Upravljanje Testovima i Ispitima

## Struktura Podataka i Repozitorijuma
### Hijerarhijska Organizacija
RAF-LMS organizuje testove i ispite u hijerarhijskoj strukturi:
1. Predmet (Subject)
    - Naziv predmeta (npr. "OOP")
    - Puno ime predmeta (npr. "Objektno Orijentisano Programiranje")
2. Školska godina (School Year)
    - Format: "YYYY_YY" (npr. "2024_25")
3. Tip testa (Test Type)
    - Identifikacija testa (npr. "Prvi_kolokvijum", "Drugi_kolokvijum")
4. Grupa (Group)
    - Broj grupe (npr. "1", "2")
    - Git putanja do repozitorijuma

### Baza Podataka
Sistem koristi relacionu bazu podataka sa sledećim entitetima:

1. `Subject`:
    - `id`: Jedinstveni identifikator
    - `fullName`: Puno ime predmeta
    - `shortName`: Skraćeno ime predmeta

2. `TestType`:
    - `id`: Jedinstveni identifikator
    - `name`: Naziv tipa testa
    - `schoolYear`: Školska godina
    - `subjectId`: Referenca na predmet

3. `TestGroup`:
    - `id`: Jedinstveni identifikator
    - `groupNumber`: Broj grupe
    - `testTypeId`: Referenca na tip testa
    - `gitPath`: Putanja do Git repozitorijuma

## REST API Endpointi

### Pregled Strukture
RAF-LMS pruža sledeće REST endpointe za upravljanje testovima:

1. Pregled Predmeta:
```http
GET /api/v1/profesor/tests/subjects
```
Vraća listu svih dostupnih predmeta

2. Pregled Školskih Godina za Predmet:
```http
GET /api/v1/profesor/tests/subjects/{subject}/years
```
Vraća listu školskih godina za određeni predmet

3. Pregled Tipova Testova:
```http
GET /api/v1/profesor/tests/subjects/{subject}/years/{year}/types
```
Vraća listu tipova testova za određenu godinu i predmet

4. Pregled Grupa:
```http
GET /api/v1/profesor/tests/subjects/{subject}/years/{year}/types/{type}/groups
```
Vraća listu grupa za određeni tip testa

Kreiranje Novog Testa
Za kreiranje novog testa koristi se:
```http
POST /api/v1/directories/create
```
Telo zahteva (JSON):
```json
{
  "subject": "OOP",
  "year": "2024_25",
  "testType": "Prvi_kolokvijum",
  "group": "1"
}
```

### Primeri Korišćenja

1. Pregled Strukture Predmeta
```bash
# Dobavljanje svih predmeta
curl http://server:8091/api/v1/profesor/tests/subjects

# Dobavljanje godina za OOP
curl http://server:8091/api/v1/profesor/tests/subjects/OOP/years

# Dobavljanje tipova testova
curl http://server:8091/api/v1/profesor/tests/subjects/OOP/years/2024_25/types
```

2. Kreiranje Novog Testa
```bash
curl -X POST http://server:8091/api/v1/directories/create \
-H "Content-Type: application/json" \
-d '{
    "subject": "OOP",
    "year": "2024_25",
    "testType": "Prvi_kolokvijum",
    "group": "1"
}'
```

Slobodno istražujte API i modifikujte ga prema svojim potrebama. Ako naiđete na bilo kakve probleme ili imate sugestije, molimo vas da doprinesete projektu otvaranjem pitanja ili slanjem zahteva za izmenama (pull request).

{"message":"{\"id\":\"M312023\",\"value\":\"f266c9a9-d9fd-4c21-b602-2674cc4cddd7\"}"}
