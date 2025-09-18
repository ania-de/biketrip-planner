BikeTrip Planner

Aplikacja webowa do planowania tras rowerowych: tworzenie i edycja tras, dodawanie punktów na mapie, oceny i recenzje, kalkulator kalorii oraz integracje z pogodą (OpenWeatherMap) i generowaniem tras (OpenRouteService).
Projekt zawiera REST API (JSON) oraz UI oparte o Spring MVC + Thymeleaf.

Funkcjonalności
-Trasy: tworzenie, lista, filtrowanie, edycja, usuwanie.
-Punkty trasy: dodawanie punktów (lat/lon), rysowanie trasy na mapie (Leaflet).
-Recenzje: dodawanie/edycja/usuwanie, średnia ocena trasy.
-Pogoda: podgląd bieżącej pogody dla miasta trasy.
-Kalkulator kalorii: szacowanie spalonych kcal (MET).
-Generator tras: automatyczne pętle wokół miasta (OpenRouteService).

Zastosowana technologia
Java 17, Spring Boot 3, Spring MVC/REST, Spring Data JPA (Hibernate), MySQL, Thymeleaf, Leaflet, Lombok, Maven.

Dwa wejścia: REST API (JSON) oraz UI (Thymeleaf).

Wymagania wstępne
-Java 17+
-MySQL

Zmienne środowiskowe (klucze do API):
-OWM_API_KEY – OpenWeatherMap
-ORS_API_KEY – OpenRouteService

Uwaga: jeśli OWM_API_KEY nie jest ustawiony, WeatherService przerwie start.

Konfiguracja i uruchomienie
Baza danych – domyślne ustawienia (application.properties):

spring.datasource.url=jdbc:mysql://localhost:3306/bikeTrip?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=coderslab
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true


Uruchomienie:

klucze:
export OWM_API_KEY=OWM_API_KEY=f6baea88b8b67a6d51ec53e3bce51af3
export ORS_API_KEY=ORS_API_KEY=eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6IjE3YjFjNTU1ZmRiMDRlMjE4MzVmOTI0YzcwNjFiZmY0IiwiaCI6Im11cm11cjY0In0\=

start:
./mvnw spring-boot:run


Aplikacja: http://localhost:8080
UI: /ui/auth/login, /ui/routes
REST: np. /routes, /reviews, /categories, /api/weather

Przykład (REST – utworzenie trasy):

curl -X POST "http://localhost:8080/routes" -H "Content-Type: application/json" -d '{
"name":"Trasa 28",
"city":"Opole",
"difficulty":"MEDIUM",
"distance":23.5,
"duration":60,
"categoryId":null,
"points":[{"name":"Start","latitude":54.5189,"longitude":18.5305,"orderIndex":0}]
}'


ENG VERSION:

BikeTrip Planner

Web app for planning bike routes: create/edit routes, add points on the map, leave ratings & reviews, estimate calories, and fetch current weather (OpenWeatherMap). It can also generate round-trip routes via OpenRouteService.
The project exposes both a REST API (JSON) and a server-rendered UI (Spring MVC + Thymeleaf).

Features
-Routes: create, list, filter (city, difficulty, distance, duration), edit, delete
-Route points: add points (lat/lon) and draw the route on a Leaflet map
-Reviews: add/edit/delete and compute average rating
-Weather: show current weather for the route’s city (OpenWeatherMap)
-Calories: simple MET-based calorie estimation
-Route generator: automatic round-trip generation around a city (OpenRouteService)

Tech Stack
Java 17, Spring Boot 3, Spring MVC/REST, Spring Data JPA (Hibernate), MySQL, Thymeleaf, Leaflet, Lombok, Maven.

Two entry points: REST API (JSON) and UI (Thymeleaf).

Requirements
-Java 17+
-MySQL

Environment variables (API keys)

OWM_API_KEY – OpenWeatherMap
ORS_API_KEY – OpenRouteService

Note: If OWM_API_KEY is missing, WeatherService will fail the application startup (by design).
Security: Do not commit API keys to Git; use environment variables or a secrets manager.

Configuration & Run
Database (default settings in application.properties)
spring.datasource.url=jdbc:mysql://localhost:3306/bikeTrip?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=coderslab
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

Start the app
KEYS:
export OWM_API_KEY=OWM_API_KEY=f6baea88b8b67a6d51ec53e3bce51af3
export ORS_API_KEY=ORS_API_KEY=eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6IjE3YjFjNTU1ZmRiMDRlMjE4MzVmOTI0YzcwNjFiZmY0IiwiaCI6Im11cm11cjY0In0\=

START:
./mvnw spring-boot:run


App: http://localhost:8080

UI: /ui/auth/login, /ui/routes

REST: e.g. /routes, /reviews, /categories, /api/weather

Example (REST — create a route)
curl -X POST "http://localhost:8080/routes" \
-H "Content-Type: application/json" \
-d '{
"name":"City Loop",
"city":"Gdynia",
"difficulty":"MEDIUM",
"distance":23.5,
"duration":60,
"categoryId":null,
"points":[{"name":"Start","latitude":54.5189,"longitude":18.5305,"orderIndex":0}]
}'