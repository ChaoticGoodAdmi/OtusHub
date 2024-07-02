# API Спецификация и Инструкция по Запуску Приложения OtusHub

## Информация о приложении

**Название приложения:** OtusHub  
**Порт сервера:** 4242

## Описание API

### Общая информация

- **Версия API:** 1.2.0
- **Заголовок:** OTUS Highload Architect

### Методы API

#### POST /login

- **Описание:** Упрощенный процесс аутентификации путем передачи идентификатора пользователя и получения токена для
  дальнейшего прохождения авторизации.
- **Тело запроса:** JSON объект с полями:
  - `userId` (строка): Идентификатор пользователя
  - `password` (строка): Пароль пользователя
- **Ответы:**
  - `200`: Успешная аутентификация. Возвращает объект с полем `token` (строка).
  - `400`: Невалидные данные.
  - `404`: Пользователь не найден.
  - `500`: Ошибка сервера.
  - `503`: Сервис недоступен.

#### POST /user/register

- **Описание:** Регистрация нового пользователя.
- **Тело запроса:** JSON объект с полями:
  - `first_name` (строка): Имя
  - `second_name` (строка): Фамилия
  - `birthdate` (строка): Дата рождения в формате `YYYY-MM-DD`
  - `biography` (строка): Хобби, интересы и т.п.
  - `city` (строка): Город
  - `password` (строка): Пароль пользователя
- **Ответы:**
  - `200`: Успешная регистрация. Возвращает объект с полем `user_id` (строка).
  - `400`: Невалидные данные.
  - `500`: Ошибка сервера.
  - `503`: Сервис недоступен.

#### GET /user/get/{id}

- **Описание:** Получение анкеты пользователя.
- **Параметры пути:**
  - `id` (строка): Идентификатор пользователя
- **Ответы:**
  - `200`: Успешное получение анкеты пользователя. Возвращает объект `User`.
  - `400`: Невалидные данные.
  - `404`: Анкета не найдена.
  - `500`: Ошибка сервера.
  - `503`: Сервис недоступен.

### Компоненты

#### Ответы

- `400`: Невалидные данные ввода.
- `401`: Неавторизованный доступ.
- `5xx`: Ошибка сервера. Возвращает объект с полями:
  - `message` (строка): Описание ошибки.
  - `request_id` (строка): Идентификатор запроса.
  - `code` (целое число): Код ошибки.

#### Схемы

- **BirthDate:** Дата рождения (строка, формат `date`).
- **UserId:** Идентификатор пользователя (строка).
- **User:** Объект пользователя с полями:
  - `id` (UserId): Идентификатор пользователя.
  - `first_name` (строка): Имя.
  - `second_name` (строка): Фамилия.
  - `birthdate` (BirthDate): Дата рождения.
  - `biography` (строка): Интересы.
  - `city` (строка): Город.

#### Схемы безопасности

- **bearerAuth:** Авторизация по токену (схема `bearer`).

## Инструкция по запуску

### Необходимые переменные окружения

Перед запуском приложения необходимо установить следующие переменные окружения:

- `SPRING_DATASOURCE_URL`: URL базы данных
- `SPRING_DATASOURCE_USERNAME`: Имя пользователя базы данных
- `SPRING_DATASOURCE_PASSWORD`: Пароль пользователя базы данных
- `JWT_SECRET`: Секретный ключ для подписи JWT токенов
- `SERVICE_URL`: Базовый URL для внешних сервисов

### Файл конфигурации

Пример файла конфигурации `application.properties`:

```properties
spring.application.name=OtusHub
server.port=4242
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.generate-ddl=false
spring.jpa.hibernate.ddl-auto=none
jwt.secret=${JWT_SECRET}
jwt.expirationMs=86400000
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
retry.after.seconds=3600
service.ping.url=${SERVICE_URL}/ping
healthcheck.cpuThreshold=0.7
healthcheck.memoryThreshold=0.8
management.endpoints.web.exposure.include=health,info,metrics,env,configprops
management.endpoint.health.show-details=always
