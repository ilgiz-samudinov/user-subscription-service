# Микросервис управления пользователями и подписками

Микросервис, разработанный на **Spring Boot 3**, предоставляет RESTful API для управления пользователями и их подписками на цифровые сервисы, такие как YouTube Premium, VK Музыка, Яндекс.Плюс, Netflix и другие стриминговые платформы. Для хранения данных используется база данных PostgreSQL.

---

## О проекте

Проект реализован с использованием **гексагональной архитектуры** (архитектуры портов и адаптеров), что обеспечивает модульность, тестируемость и гибкость кода.

### Основные преимущества:
- **Изоляция бизнес-логики**: Домен не зависит от внешних технологий (БД, веб-серверов).
- **Тестируемость**: Логика тестируется независимо от инфраструктуры.
- **Гибкость**: Легко добавлять новые адаптеры (например, для других БД или API).
- **Чистота кода**: Четкое разделение ответственности между слоями.

---



## Функциональные возможности

### Управление пользователями
- **Создание пользователя**: Регистрация с указанием личных данных.
- **Получение данных**: Информация о пользователе по его ID.
- **Обновление данных**: Изменение информации о пользователе.
- **Удаление пользователя**: Удаление из системы.

### Управление подписками
- **Добавление подписки**: Назначение пользователю подписки на сервис.
- **Список подписок**: Просмотр всех подписок пользователя.
- **Удаление подписки**: Удаление конкретной подписки.
- **Топ-3 сервиса**: Отображение самых популярных сервисов по числу уникальных подписок.

**Поддерживаемые сервисы**: YouTube Premium, VK Музыка, Яндекс.Плюс, Netflix и др.

---

## Технологический стек
- **Spring Boot 3** — основа приложения.
- **Spring Data JPA** — взаимодействие с базой данных.
- **PostgreSQL** — реляционная база данных.
- **Maven** — управление зависимостями.
- **Docker** — контейнеризация.

---

## Структура проекта

```
example/com/usersubscriptionservice/
├── UserSubscriptionServiceApplication.java
├── adapter/
│   ├── in/
│   │   └── web/
│   │       ├── SubscriptionController.java
│   │       ├── UserController.java
│   │       └── dto/
│   │           ├── SubscriptionRequest.java
│   │           ├── SubscriptionResponse.java
│   │           ├── TopSubscriptionDto.java
│   │           ├── UserRequest.java
│   │           └── UserResponse.java
│   └── out/
│       ├── entity/
│       │   ├── SubscriptionEntity.java
│       │   └── UserEntity.java
│       ├── mapper/
│       │   ├── SubscriptionMapper.java
│       │   └── UserMapper.java
│       ├── persistence/
│       │   ├── SubscriptionPersistenceAdapter.java
│       │   └── UserPersistenceAdapter.java
│       └── repository/
│           ├── JpaSubscriptionRepository.java
│           └── JpaUserRepository.java
├── application/
│   ├── exception/
│   │   ├── ApplicationException.java
│   │   ├── ErrorResponse.java
│   │   ├── GlobalExceptionHandler.java
│   │   ├── NotFoundException.java
│   │   └── ValidationException.java
│   ├── port/
│   │   ├── in/
│   │   │   ├── SubscriptionUseCase.java
│   │   │   └── UserUseCase.java
│   │   └── out/
│   │       ├── SubscriptionRepositoryPort.java
│   │       ├── TopSubscriptionProjection.java
│   │       └── UserRepositoryPort.java
│   └── service/
│       ├── SubscriptionService.java
│       └── UserService.java
├── domain/
│   └── model/
│       ├── Subscription.java
│       ├── User.java
│       └── valueobject/
│           ├── Currency.java
│           ├── Status.java
│           ├── SubscriptionService.java
│           └── SubscriptionType.java
```



## Схема базы данных

### Таблица `users`
| Поле         | Тип          | Ограничения          | Описание               |
|--------------|--------------|----------------------|------------------------|
| `id`         | BIGINT       | Primary Key, Auto-increment | Уникальный ID пользователя |
| `email`      | VARCHAR(255) | Unique, Not Null    | Email пользователя     |
| `password`   | VARCHAR(255) | Not Null            | Пароль пользователя    |
| `firstName`  | VARCHAR(255) | Not Null            | Имя пользователя       |
| `lastName`   | VARCHAR(255) | Not Null            | Фамилия пользователя   |
| `phone`      | VARCHAR(20)  | Not Null            | Телефон пользователя   |

### Таблица `subscriptions`
| Поле                   | Тип              | Ограничения                                                | Описание                                                |
|-----------------------|------------------|-----------------------------------------------------------|--------------------------------------------------------|
| `id`                  | BIGINT          | Primary Key, Auto-increment, Not Null                     | Уникальный ID подписки                                 |
| `user_id`             | BIGINT          | Foreign Key → `users(id)`, Not Null                       | Ссылка на пользователя                                 |
| `subscription_service`| VARCHAR (ENUM)  | Not Null                                                  | Название сервиса (enum `SubscriptionService`)          |
| `subscription_type`   | VARCHAR (ENUM)  | Not Null                                                  | Тип подписки (enum `SubscriptionType`)                 |
| `start_date`          | DATE            | Not Null                                                  | Дата начала подписки                                   |
| `end_date`            | DATE            | Not Null                                                  | Дата окончания подписки                                |
| `status`              | VARCHAR (ENUM)  | Not Null                                                  | Статус подписки (enum `Status`)                        |
| `auto_renew`          | BOOLEAN         |                                                           | Флаг автообновления                                    |
| `price`               | DECIMAL(10,2)   | Not Null                                                  | Стоимость подписки                                     |
| `currency`            | VARCHAR (ENUM)  | Not Null                                                  | Валюта платежа (enum `Currency`)                       |
| `created_at`          | TIMESTAMP       | Not Null, Default CURRENT_TIMESTAMP, Updatable=false      | Время создания записи                                  |
| `updated_at`          | TIMESTAMP       | Not Null, Default CURRENT_TIMESTAMP                       | Время последнего обновления                            |

---

## API эндпоинты

### Пользователи
| Метод   | Эндпоинт           | Описание                     |
|---------|--------------------|------------------------------|
| `POST`  | `/users`           | Создать пользователя         |
| `GET`   | `/users/{id}`      | Получить пользователя по ID  |
| `PUT`   | `/users/{id}`      | Обновить данные пользователя |
| `DELETE`| `/users/{id}`      | Удалить пользователя         |

### Подписки
| Метод   | Эндпоинт                          | Описание                        |
|---------|-----------------------------------|---------------------------------|
| `POST`  | `/users/{userId}/subscriptions`   | Добавить подписку              |
| `GET`   | `/users/{userId}/subscriptions`   | Получить подписки пользователя |
| `DELETE`| `/users/{userId}/subscriptions/{subscriptionId}` | Удалить подписку |
| `GET`   | `/subscriptions/top`              | Топ-3 популярных сервиса       |

### Дополнительные возможности
| Метод   | Эндпоинт                          | Описание                        |
|---------|-----------------------------------|---------------------------------|
| `GET`   | `/api/subscriptions`              | Список всех подписок           |
| `PATCH` | `/api/users/{userId}/subscriptions/{id}/auto-renew?autoRenew={true|false}` | Изменить автообновление |
| `PATCH` | `/api/users/{userId}/subscriptions/{id}/cancel` | Отменить подписку         |
| `PATCH` | `/api/users/{userId}/subscriptions/{id}/pause`  | Приостановить подписку    |

---



---

## Установка и запуск

### Требования
- **Java 17+**
- **Maven**
- **Docker**

### Инструкции
1. **Клонирование репозитория**:
   ```bash
   git clone https://github.com/ilgiz-samudinov/user-subscription-service.git
   cd user-subscription-service
   ```

2. **Запуск с Docker Compose**:
   ```bash
   docker-compose up --build
   ```


## Примеры запросов

### Создание пользователя
**Запрос**: `POST /users`  
**Тело запроса**:
```json
{
    "email": "ilgiz@gmail.com",
    "password": "ilgiz",
    "firstName": "Ilgiz",
    "lastName": "Samudinov",
    "phone": "+996706540198"
}
```
**Ответ**: JSON с данными пользователя и присвоенным `id`.

### Добавление подписки
**Запрос**: `POST /users/{userId}/subscriptions`  
**Тело запроса**:
```json
{
    "subscriptionService": "YANDEX",
    "subscriptionType": "PLUS",
    "autoRenew": true
}
```
**Примечание**: `userId` передается в URL.  
**Ответ**: JSON с данными подписки и присвоенным `id`.

---


## Возможные улучшения
- Добавить аутентификацию и авторизацию (Spring Security, JWT).
- Реализовать пагинацию для списков подписок.
- Использовать предопределенные значения для `subscriptionService`.
- Добавить юнит- и интеграционные тесты (JUnit, Testcontainers).