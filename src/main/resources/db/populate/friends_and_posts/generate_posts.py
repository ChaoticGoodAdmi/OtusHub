import csv
import random
from faker import Faker
from datetime import datetime, timedelta
import os
import psycopg2
from concurrent.futures import ThreadPoolExecutor

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
def generate_user_posts(user_id, max_posts):
    posts = []
    num_posts = random.randint(0, max_posts)
    for _ in range(num_posts):
        content = generate_content()
        created_at = fake.date_time_between(start_date='-2y', end_date='now')
        updated_at = created_at + timedelta(days=random.randint(0, 365))
        posts.append([user_id, content, created_at, updated_at])
    return posts

# Генерация данных и запись в CSV файл с использованием многопоточности
def generate_csv(filename, num_rows, user_ids, max_posts_per_user=10):
    # Определение текущей директории и путь к файлу
    current_directory = os.path.dirname(os.path.abspath(__file__))
    file_path = os.path.join(current_directory, filename)

    # Пул потоков
    with ThreadPoolExecutor() as executor:
        all_posts = []
        futures = [executor.submit(generate_user_posts, random.choice(user_ids), max_posts_per_user) for _ in range(num_rows)]
        for future in futures:
            all_posts.extend(future.result())

    # Запись в CSV файл
    with open(file_path, 'w', newline='', encoding='utf-8') as csvfile:
        csvwriter = csv.writer(csvfile, delimiter=',')
        csvwriter.writerow(['user_id', 'content', 'created_at', 'updated_at'])
        csvwriter.writerows(all_posts[:num_rows])

# Запуск генерации данных
user_ids = get_user_ids()
generate_csv('posts_data.csv', 100000, user_ids)
