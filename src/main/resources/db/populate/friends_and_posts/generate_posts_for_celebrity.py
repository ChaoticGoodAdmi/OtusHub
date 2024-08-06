import csv
import random
from faker import Faker
from datetime import datetime, timedelta
import os
import psycopg2

# Создание объекта Faker
fake = Faker()

# Функция для подключения к базе данных и получения списка user_id
def get_user_ids():
    connection = psycopg2.connect(
        dbname="ИМЯ БД",
        user="ИМЯ ПОЛЬЗОВАТЕЛЯ",  # Замените на ваше имя пользователя
        password="ПАРОЛЬ",  # Замените на ваш пароль
        host="localhost",
        port="5433"
    )
    cursor = connection.cursor()
    cursor.execute("SELECT id FROM userdb.users;")
    user_ids = [row[0] for row in cursor.fetchall()]
    cursor.close()
    connection.close()
    return user_ids

# Функция для генерации случайного текста
def generate_content():
    return ' '.join(fake.words(random.randint(1, 100)))

# Функция для генерации постов для одного пользователя
def generate_posts_for_user(user_id, num_posts):
    posts = []
    for _ in range(num_posts):
        content = generate_content()
        created_at = fake.date_time_between(start_date='-2y', end_date='now')
        updated_at = created_at + timedelta(days=random.randint(0, 365))
        posts.append([user_id, content, created_at, updated_at])
    return posts

# Генерация данных и запись в CSV файл
def generate_csv_for_one_user(filename, num_posts):
    # Получение списка user_id и выбор случайного пользователя
    user_ids = get_user_ids()
    random_user_id = random.choice(user_ids)

    # Генерация постов для выбранного пользователя
    posts = generate_posts_for_user(random_user_id, num_posts)

    # Определение текущей директории и путь к файлу
    current_directory = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(current_directory, filename)

    # Запись постов в CSV файл
    with open(file_path, 'w', newline='', encoding='utf-8') as csvfile:
        csvwriter = csv.writer(csvfile, delimiter=',')
        csvwriter.writerow(['user_id', 'content', 'created_at', 'updated_at'])
        csvwriter.writerows(posts)

# Запуск генерации данных
generate_csv_for_one_user('single_user_posts.csv', 1000)
