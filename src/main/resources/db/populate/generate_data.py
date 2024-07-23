import random
import string
from datetime import datetime
from faker import Faker
from bcrypt import hashpw, gensalt
from concurrent.futures import ThreadPoolExecutor, as_completed
import csv
import os

fake = Faker()

def generate_random_id():
    random_digits = ''.join(random.choices(string.digits, k=6))
    return fake.word() + random_digits

def generate_random_birth_date():
    start_date = datetime(1950, 1, 1)
    end_date = datetime(2006, 1, 1)
    return fake.date_between(start_date=start_date, end_date=end_date)

def generate_password_hash(password):
    return hashpw(password.encode('utf-8'), gensalt()).decode('utf-8')

def generate_random_user():
    sex = fake.random_element(['M', 'F'])
    first_name = fake.first_name()
    last_name = fake.last_name()
    birth_date = generate_random_birth_date()
    biography = fake.text(max_nb_chars=50)
    city = fake.city()
    password_hash = generate_password_hash(first_name + last_name)

    return (
        generate_random_id(),
        first_name,
        last_name,
        birth_date,
        sex,
        biography,
        city,
        password_hash
    )

def generate_users_to_csv(total_users, csv_filename, mode='w'):
    users = []
    with ThreadPoolExecutor(max_workers=8) as executor:
        futures = [executor.submit(generate_random_user) for _ in range(total_users)]
        for future in as_completed(futures):
            users.append(future.result())

    with open(csv_filename, mode, newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerows(users)

total_users = 1_000_000

script_dir = os.path.dirname(os.path.abspath(__file__))
csv_filename = os.path.join(script_dir, 'users.csv')

if os.path.exists(csv_filename):
    generate_users_to_csv(total_users, csv_filename, mode='a')
else:
    generate_users_to_csv(total_users, csv_filename, mode='w')

print(f"All data has been successfully generated and saved to {csv_filename}")
