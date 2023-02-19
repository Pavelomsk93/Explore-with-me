# Сервис для обмена информацией о событиях

**Программа позволяет делиться информацией о событиях (концерты/выставки/походы), получать информацию о событиях: подборки, поиск, оставлять комментарии.**

**Используемые стек: Java 17, Spring Boot, Spring JPA, Maven, PostgreSQL, Docker**

**Программа имеет два сервера: ewm-sercice и stats-server.**

Сервис ewm-sercice включает в себя основную логику приложения.

Сервис stats-server хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.
Взаимодействие сервисов реализовано с помощью Сostume Client - наследуемый от BaseClient.
Запросы в БД производятся с использованием Spring JPA.

**Необходимо запускать docker-compose.yml на следующих портах:**
- ewm-sercice:    ports - 8080:8080
- ewm-db:         ports - 6541:5432
- stats-service:  ports - 9090:9090
- stats-db:       ports - 6542:5432


Пример Endpoint-a поиска события (программа написана на Java):

```java

    @PutMapping(path = "/{eventId}")
    public EventFullDto putEvent(
            @PathVariable Long eventId,
            @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("URL: /admin/events/{eventId}. PutMapping/Редактирование события " + eventId + "/putEvent");
        return eventService.putEvent(eventId, adminUpdateEventRequest);
    }

```
