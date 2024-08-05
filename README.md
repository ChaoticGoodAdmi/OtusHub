# API Спецификация и Инструкция по Запуску Приложения OtusHub

## Информация о приложении

**Название приложения:** OtusHub  
**Порт сервера:** 4242

## Описание API

### Общая информация

- **Версия API:** 1.1.0
- **Заголовок:** OTUS Highload Architect

### [Коллекция Postman-запросов](https://github.com/ChaoticGoodAdmi/OtusHub/blob/master/src/main/resources/otushub.postman_collection.json)

### Альтернатива: cUrl запросы

#### Регистрация пользователя (валидация данных и сохранение пользователя в БД)

*Необходимо поменять данные в теле запроса на нужные*
**Требования:**

1. все поля не пустые
2. birthDate: в формате "YYYY-MM-DD" должна быть в прошлом
3. sex: "MALE" или "FEMALE"
4. biography: не длиннее 200 символов`

```bash
curl -X POST http://localhost:4242/user/register \
    -H "Content-Type: application/json" \
    -d '{
          "firstName": "Kirill",
          "secondName": "Ushakov",
          "birthDate": "1990-05-03",
          "sex": "MALE",
          "biography": "A software developer with over 4 years of experience in backend development.",
          "city": "Vladimir",
          "password": "securepassword123"
        }'
```

#### Аутентификация пользователя по userId и password

*Необходимо поменять данные в теле запроса на нужные*
**Требования:**

1. userId: подставить UUID, сгенерированный при выполнении метода user/register
2. password: тот же пароль, что использовался в теле запроса в user/register

```bash
curl -X POST http://localhost:4242/login \
    -H "Content-Type: application/json" \
    -d '{
          "userId": "ea2915",
          "password": "securepassword123"
        }'
```

#### Получение анкетных данных пользователя по UserId

*Необходимо поменять данные в URL и в заголовке Authentication на нужные*
**Требования:**

1. userId: подставить UUID, сгенерированный при выполнении метода user/register в URL запроса
2. Headers: в заголовке запроса Authentication вставить токен, полученный при выполнении сервиса /login после "Bearer<
   пробел>"

```bash
curl -X GET http://localhost:4242/user/get/ea2915 \
    -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYTI5MTUiLCJpYXQiOjE3MjAwMDIyMzQsImV4cCI6MTcyMDA4ODYzNH0.Q456enA8phHL7VhMSr7u4ec12xTMcK1Fi1_6eSpzCK8XCc3WjFHfzFTs_q2_qxjZ3ighJF1_19pjLes9Mx1TbQ"
```

#### Health Check

```bash
curl -X GET http://localhost:4242/actuator/health
```

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

1. Клонировать проект из репозитория командой:

```
git clone https://github.com/ChaoticGoodAdmi/OtusHub.git
```

2. Перед запуском приложения необходимо установить следующие переменные окружения:

- `SPRING_DATASOURCE_URL`: URL базы данных
- `SPRING_DATASOURCE_USERNAME`: Имя пользователя базы данных
- `SPRING_DATASOURCE_PASSWORD`: Пароль пользователя базы данных
- `JWT_SECRET`: Секретный ключ для подписи JWT токенов
- `SERVICE_URL`: Базовый URL для внешних сервисов

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
```

3. Запустить контейнер с базой Postgres

```
docker run --name otushub-postgres -e POSTGRES_PASSWORD=postgres -p 5433:5432 -d postgres
```

4. В директории с проектом выполнить команду:

```
docker build -t otushub-backend .
```

5. Затем запустить контейнер

```
docker run -d -p 4242:4242 --name otushub-backend otushub-backend
```

## Инструкция по заполнению таблицы случайными данными

1. Установить Python 3.12
2. Выполнить команду

```
pip install -r src/main/resources/db/populate/requirements.txt
```

3. Выполнить команду

```
python src/main/resources/db/populate/generate_data.py
```

В результате сгенерируется файл users.csv (пример сгенерированного файла с 1 000 000 значений приложен).

4. Выполнить команду на случай сгенерированных неуникальных id (при генерации в коде приложения есть проверка):

```
python src/main/resources/db/populate/check_non_unique_ids.py.py
```

5. В файле `src/main/resources/db/populate/insert_data.py` добавить значения для локальной БД:

```
  DB_NAME = 'название БД'
  DB_USER = 'имя пользователя'
  DB_PASSWORD = 'пароль'
  DB_HOST = 'адрес БД'
  DB_PORT = 'порт БД'
```

Пример:

```
  DB_NAME = 'otushub'
  DB_USER = 'admin'
  DB_PASSWORD = 'password'
  DB_HOST = 'localhost'
  DB_PORT = '5432'
```

4. Выполнить команду

```
python src/main/resources/db/populate/insert_data.py
```

5. Пароли сгенерированных пользователей соответствуют имени и фамилии в формате "ИмяФамилия". Пример: "AmandaHale".

6. В скриптах из папки src/main/resources/db/populate/friends_and_posts заменить необходимые параметры Выполнить команды

```python
python src/main/resources/db/populate/friends_and_posts/generate_posts.py
python src/main/resources/db/populate/friends_and_posts/generate_posts_for_celebrity.py
python src/main/resources/db/populate/friends_and_posts/generate_friends.py
python src/main/resources/db/populate/friends_and_posts/generate_friends_for_celebrity.py
```

7. Импортировать файлы в БД в соответствующие таблицы:

- **posts_data.csv**
- **single_user_posts.csv**
- **friends_for_single_user.csv**
- **friends_data.csv** 

