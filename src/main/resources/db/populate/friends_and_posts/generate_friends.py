import csv
import random
from faker import Faker
from datetime import datetime
import os
import psycopg2

# Создание объекта Faker
fake = Faker()

# Функция для подключения к базе данных и получения списка user_id из таблицы posts
def get_user_ids_from_posts():
    connection = psycopg2.connect(
        dbname="ИМЯ БД",
        user="ИМЯ ПОЛЬЗОВАТЕЛЯ",  # Замените на ваше имя пользователя
        password="ПАРОЛЬ",  # Замените на ваш пароль
        host="localhost",
        port="5433"
    )
    cursor = connection.cursor()
    cursor.execute("SELECT DISTINCT user_id FROM userdb.posts;")
    user_ids = [row[0] for row in cursor.fetchall()]
    cursor.close()
    connection.close()
    return user_ids

# Функция для генерации списка друзей
def generate_friends(user_ids, num_friends):
    friends_list = []
    for _ in range(num_friends):
        user_id = random.choice(user_ids)
        num_friend_ids = random.randint(10, 100)
        friend_ids = random.sample(user_ids, num_friend_ids)

        for friend_id in friend_ids:
            if user_id != friend_id:
                added_at = fake.date_time_between(start_date='-2y', end_date='now')
                friends_list.append([user_id, friend_id, added_at])
    return friends_list

# Генерация данных и запись в CSV файл
def generate_friends_csv(filename, num_entries):
    # Получение списка user_id из таблицы posts
    user_ids = get_user_ids_from_posts()

    # Генерация списка друзей
    friends_data = generate_friends(user_ids, num_entries)

    # Определение текущей директории и путь к файлу
    current_directory = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(current_directory, filename)

    # Запись данных в CSV файл
    with open(file_path, 'w', newline='', encoding='utf-8') as csvfile:
        csvwriter = csv.writer(csvfile, delimiter=',')
        csvwriter.writerow(['user_id', 'friend_id', 'added_at'])
        csvwriter.writerows(friends_data)

# Запуск генерации данных
generate_friends_csv('friends_data.csv', 10000)
