# Filmorate

В схеме представленной ниже реализованная следующим образом.
У нас есть два основых объекта:
- user
- film

1) Каждого user мы заносим в таблицу:
- user_id INT (Primary key)
- email VARCHAR(255)
- login VARCHAR (255)
- name VARCHAR(255)
- birthday DATE

2) У каждого user есть friend, который и есть user под определенным id, потому мы реализуем новую таблицу в 
которой будет user_id и его друг friend_id, так же что бы было понятно дружат ли они или нет, добавим поле status.
- user_id INT
- friend_id
- status

3) Каждый film мы заносим в таблицу:
- film_id INT (Primary key)
- name VARCHAR(255)
- description VARCHAR(255)
- release_date DATE
- duration BIGINT
- rate INT
- genre VARCHAR(255)
- age_rating VARCHAR(255)

4) У каждого фильма есть film_like, которые показывают кому понравился этот фильм. Для этого мы реализуем таблицу с 
пользователями которым понравился тот или иной фильм:
- user_id INT
- film_id INT


![Ссылка схемы БД.](src/main/resources/static/Схема Filmorate.png)