# java-filmorate

В этом спринте вы начнёте с малого, но очень важного: 
создадите каркас Spring Boot приложения Filmorate (от англ. film — «фильм» и rate — «оценивать»). 
В дальнейшем сервис будет обогащаться новым функционалом и с каждым спринтом становиться лучше благодаря вашим знаниям о Java. 
Скорее вперёд!

В проекте реализованы: 

<details>
    <summary><h3> Спринт 9:</h3></summary>
Выполнено проектирование согласно Техническому заданию:
1. Определены модели данных приложения: Film, User;
2. Организовано предварительной хранение данных;
3. Созданы REST-контроллеры: FilmController, UserController;
3. Задана валидация данных;
4. Выполнено логирование данных;
5. Валидация проверяется тестами Unit5.
</details>

<details>
    <summary><h3> Спринт 10:</h3></summary>
1. Переработана архитектура проекта:
* созданы интерфейсы FilmStorage и UserStorage; 
* созданы классы InMemoryFilmStorage и InMemoryUserStorage (@Component); 
* созданы классы UserService и FilmService (@Service);
2. API доведен до соответствия REST;
3. Настроен ExceptionHandler для централизованной обработки ошибок
</details>

* Спринт 11. Часть 1:
1. Переработана архитектура проекта: добавлены дополнительные поля в классы Film (жанр и рейтинг) и User (список дружбы).
 
2. Спроектировна схема БД:
   ![ER-диаграмма](/images/FILMORATE_DB.png)
* Связь между User (пользователи) и User_friends (таблица сопоставления друзей) "many to many";
* Связь между User и Film  "many to many" через табличку likes;
* Связь между Film и Genres  "many to many" через табличку Film_Genres; 
* Связь между Rating и Film "one to many" т.к. rating_id уникален и может быть присвоен множеству фильмов;
* Для таблиц friends, likes и film_genre_line использованы составные Primary Key из двух id.

3. ### Примеры запросов

<details>
    <summary><h3>Работа с фильмами:</h3></summary>

* Запрос фильма по id:

```SQL
SELECT f.film_name,
       f.film_description,
       f.film_releaseDate,
       f.film_duration,
       r.rating_name,
       g.genre_name
FROM Films f
JOIN Rating r ON f.rating_id = r.rating_id
JOIN Film_Genres fg ON f.film_id = fg.film_id
JOIN Genres g ON fg.genre_id = g.genre_id
WHERE f.film_id = ?
```   

* Запрос всех фильмов:

```SQL
SELECT f.film_name,
       f.film_description,
       f.film_releaseDate,
       f.film_duration,
       r.rating_name,
       g.genre_name
FROM Films f
JOIN Rating r ON f.rating_id = r.rating_id
JOIN Film_Genres fg ON f.film_id = fg.film_id
JOIN Genres g ON fg.genre_id = g.genre_id;
```

* Запрос топ-N фильмов по количеству лайков:
```SQL
SELECT f.film_name,
       COUNT(l.film_id) AS likes_count
FROM Films f
JOIN Likes l ON f.film_id = l.film_id
GROUP BY f.film_name
ORDER BY likes_count DESC
LIMIT N;
```
</details>

<details>
    <summary><h3>Работа с пользователями:</h3></summary>

* Запрос пользователя по id:

```SQL
SELECT *
FROM Users
WHERE user_id = ?;
```   

* Запрос всех пользователей:

```SQL
SELECT *
FROM Users;
``` 

</details>

<details>
    <summary><h3>Работа с жанрами:</h3></summary>

* Запрос жанра по id:

```SQL
SELECT *
FROM Genres
WHERE genre_id = ?;;
``` 

* Запрос всех жанров:

```SQL
SELECT *
FROM Genres;
```   
</details>

<details>
    <summary><h3>Работа с рейтингами MPA:</h3></summary>

* Запрос рейтинга по id:

```SQL
SELECT *
FROM Rating
WHERE rating_id = ?;
``` 

* Запрос всех рейтингов MPA:

```SQL
SELECT *
FROM Rating;
```   
</details>


