# Fulköpings Uthyrning AB

## För att köra projektet lokalt
1. Starta en PostgreSQL-container (`docker-compose.yml` finns i roten).  
Databasen som är angiven i `application.properties` är `jdbc:postgresql://localhost:5432/fulhyr`,  
så skapa antingen den i en SQL-klient, eller byt adress till någon existerande tom databas i containern
2. Bygg och kör projektet. Tabeller genereras automatiskt av Hibernate
3. Alla endpoints finns nu på port `5500`


## Att använda API:et
Dokumentation via OpenAPI och Swagger UI genereras automatiskt och finns på:
- Swagger UI: http://localhost:5500/swagger-ui.html
- OpenAPI-dokumentet (JSON): http://localhost:5500/v3/api-docs  

Se dokumentationen för endpoints och förväntad JSON.  
DTO:er används för requests mot samtliga entiteter (mot `Rental` används DTO även för response)

### HATEOAS-länkar
Länkar till *self* och *collection* finns i alla svar med status `200` / `201`

I svaren från `/api/rentals` finns länkar till kopplade `Person` och `Product`,  
samt länk till återlämning ifall uthyrningen är aktiv.  

I svaren från `/api/persons` finns länkar till personens aktiva `Rental` (om det finns några)

### Radering och ändring av kopplade data
En `Person` eller `Product` kan inte raderas om den har koppling till någon `Rental` (oavsett om den är aktiv eller inte)

`Rental` har ingen endpoint alls för `DELETE`

`PUT` `/api/products/{id}` nekas om produkten för tillfället är uthyrd,  
men är tillgänglig igen så fort den är återlämnad


## VG-krav…
Jag är medveten om att jag har implementerat några men inte alla VG-krav.  
De jag har med är:
- ingen dubbeluthyrning
- historik / ingen radering av kopplade data
