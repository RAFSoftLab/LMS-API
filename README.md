Uvod

Ovo je open source API razvijen u Java Spring-u, pružajući endpoint-ove za upravljanje informacijama o studentima. API je kontejnerizovan pomoću Dockera, a baza podataka (PostgreSQL) takođe je kontejnerizovana. Celokupno postavljanje može se lako implementirati korišćenjem Docker Compose alata.

Pretpostavke

Proverite da li imate instalirane sledeće softvere:

Docker (verzija >= 20.0)\
Docker Compose (verzija >= 2.21.0)\
Java Development Kit (JDK) 17 ili novije

Početak rada

1. Klonirajte repozitorijum:

```
git clone https://github.com/your/repo.git
cd your-repo
```

2. Izgradite Docker kontejnere i pokrenite ih:

```
./gradlew bootJar   
docker-compose build --no-cache
docker-compose up --force-recreate
```

Docker compose ce automatski preuzeti i pokrenuti najnoviju verziju oficijalnog postgres kontejnera

Sačekajte da se kontejneri pokrenu. Kada su spremni, API bi trebalo da bude dostupan na http://localhost:8091.

API Endpoint-ovi

Dobavi Studente:\
URL: http://localhost:8091/api/v1/students \
Metoda: GET

Kreiraj Studenta:\
URL: http://localhost:8091/api/v1/students \
Metoda: POST \
Zahtevno telo:

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

Dobavi Studenta po ID-u: \
URL: http://localhost:8091/api/v1/students/{id} \
Metoda: GET

Obriši Studenta po ID-u: \
URL: http://localhost:8091/api/v1/students/{id} \
Metoda: DELETE

Studentov zadatak je kloniran: \
URL: http://localhost:8091/api/v1/students/{id}/task_cloned \
Metoda: POST \
Zahtevno telo:

```
{
    "taskGroup": "21",
    "classroom": "RAF10"
}
```

Studentov zadatak je predat:

URL: http://localhost:8091/api/v1/students/{id}/task_cloned
Metoda: POST
Zahtevno telo:

```
{
    "taskGroup": "21",
    "classroom": "RAF10"
}
```

Otpremi Studente:\
URL: http://localhost:8091/api/v1/students/upload \
Metoda: POST \
Zahtevno telo: CSV Fajl

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
