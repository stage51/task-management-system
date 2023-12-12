Документация по запуску
=
### Требования к окружению
* Java 17+
* Maven
* Docker
### Установка через терминал
1. Склонируйте репозиторий:
```bash
git clone https://github.com/stage51/task-management-system
```
2. Перейдите в каталог проекта:
```bash
cd task-management-system
```
3. Создайте образ Docker:
```bash
docker build -t task-manager .
```
4. После успешной сборки, запустите контейнеры через Docker Compose. Spring-приложение может запуститься немного позже, чем MySQL:
```bash
docker-compose up 
```
5. Теперь приложение доступно по адресу ``http://localhost:9090``
* Если Spring-приложение отключилось, выполните:
```bash
docker run task-manager
```
* Или если контейнер с MySQL отключился, выполните:
```bash
docker run mysqldb
```
Работа с приложением
==
### Регистрация и авторизация
* Для того, чтобы зарегистрироваться, необходимо сделать POST-запрос по адресу ``/api/registration``.
```JSON
{
    "username" : "string",
    "email" : "string",
    "password" : "string"
    "confirmPassword" : "string"
}
```
* Обратите внимание, что ``email`` должен быть уникальным и по форме ``email@site.com``, ``password`` и ``confirmPassword`` должны быть одинаковы.
* После этого необходимо сделать POST-запрос на ``/api/auth`` для авторизации.
```JSON
{
    "email" : "string",
    "password" : "string"
}
```
* В качестве ответа мы получаем Bearer Token, который необходим для работы с API.
```JSON
{
    "token" : "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sInN1YiI6ImV2Z2VueUB2b3ZhLnJ1IiwiaWF0IjoxNzAyMjQyNjI3LCJleHAiOjE3MDIyNDMyMjd9.P3wDxb8xBZf0Dr2h7trcnmHmtB9Pin7bDnGFr3CcTh0"
}
```
* В Postman его можно нужно вставить сюда
> <img width="759" alt="1" src="https://github.com/stage51/task-management-system/assets/57895435/c604690f-5a5e-4e84-bb2e-6c27fd3b5c68">

### Пример работы с API.
* Создаем нового пользователя.
> <img width="808" alt="step1" src="https://github.com/stage51/task-management-system/assets/57895435/139f87b6-b7fd-4f4c-921b-b3a94654682e">
* Авторизуемся.
> <img width="808" alt="step2" src="https://github.com/stage51/task-management-system/assets/57895435/b37bdab0-b6fc-45ee-baf2-d01f65cb1ff2">
* Вставляем токен и переходим ко всем пользователям. Рекомендуется вместо возврата всех пользователей обращаться к страницам ``/api/users/page/{page}``.
> <img width="808" alt="step3" src="https://github.com/stage51/task-management-system/assets/57895435/0ff680e2-7a0f-4ae0-ad84-93d6e9549033">
* Создаем новое задание.
> <img width="808" alt="step5" src="https://github.com/stage51/task-management-system/assets/57895435/f80763a2-1dc1-4280-a18c-7e460545ab21">
* Переходим на нулевую страницу с задачами. Нумерация начинается с 0. Все страницы располагают по 20 элементов. Задачи отсортированы по приоритету.
> <img width="808" alt="step6" src="https://github.com/stage51/task-management-system/assets/57895435/c4bd31a7-3b56-4c0e-bbd2-805ada8aa180">
* Перейдем к комментариям.
> <img width="808" alt="step7" src="https://github.com/stage51/task-management-system/assets/57895435/a66e28fa-2c85-4990-8d92-369c271770ba">
* Создаем комментарий к нашей задаче.
> <img width="808" alt="step8" src="https://github.com/stage51/task-management-system/assets/57895435/eafab630-883a-442e-a4ff-db6e5223fae6">
### Swagger UI
* Вы можете посмотреть документацию для эндпоинтов по адресу ``/swagger-ui/index.html``.
> <img width="1439" alt="2" src="https://github.com/stage51/task-management-system/assets/57895435/0c4a2e19-15ad-465c-9164-6d854c722946">

