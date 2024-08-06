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

# Функция для генерации списка друзей для заданного пользователя
def generate_friends_for_user(user_id, friend_ids, num_friends):
    friends_list = []
    # Если количество доступных friend_ids меньше num_friends, выбираем всех доступных пользователей
    if len(friend_ids) < num_friends:
        num_friends = len(friend_ids)
    selected_friend_ids = random.sample(friend_ids, num_friends)
    for friend_id in selected_friend_ids:
        if user_id != friend_id:
            added_at = fake.date_time_between(start_date='-2y', end_date='now')
            friends_list.append([user_id, friend_id, added_at])
    return friends_list

# Генерация данных и запись в CSV файл
def generate_friends_csv_for_user(filename, user_id, num_friends):
    # Получение списка user_id из таблицы posts
    user_ids = get_user_ids_from_posts()
    # Убедиться, что пользователь с данным user_id существует
    if user_id not in user_ids:
        print(f"User {user_id} not found in the list of users.")
        return

    # Генерация списка друзей
    friends_data = generate_friends_for_user(user_id, user_ids, num_friends)

    # Определение текущей директории и путь к файлу
    current_directory = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(current_directory, filename)

    # Запись данных в CSV файл
    with open(file_path, 'w', newline='', encoding='utf-8') as csvfile:
        csvwriter = csv.writer(csvfile, delimiter=',')
        csvwriter.writerow(['user_id', 'friend_id', 'added_at'])
        csvwriter.writerows(friends_data)

# Запуск генерации данных
generate_friends_csv_for_user('friends_for_single_user.csv', 'ИМЯ ПОЛЬЗОВАТЕЛЯ ИЗ ФАЙЛА single_user_posts.csv', 100000)
