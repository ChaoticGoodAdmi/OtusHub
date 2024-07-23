import csv
from psycopg2 import connect, sql
from psycopg2.extras import execute_values
from psycopg2 import pool
import os

# Database connection settings
DB_NAME = ''
DB_USER = ''
DB_PASSWORD = ''
DB_HOST = ''
DB_PORT = ''

connection_pool = pool.SimpleConnectionPool(1, 20, dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD, host=DB_HOST, port=DB_PORT)

def read_users_from_csv(csv_filename):
    users = []
    with open(csv_filename, newline='') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            users.append(tuple(row))
    return users

def insert_users(users):
    conn = connection_pool.getconn()
    try:
        cursor = conn.cursor()
        insert_query = sql.SQL("""
            INSERT INTO userdb.users (id, first_name, second_name, birth_date, sex, biography, city, password_hash)
            VALUES %s
        """)
        execute_values(cursor, insert_query, users)
        conn.commit()
        cursor.close()
    finally:
        connection_pool.putconn(conn)

script_dir = os.path.dirname(os.path.abspath(__file__))
csv_filename = os.path.join(script_dir, 'users.csv')
users = read_users_from_csv(csv_filename)
insert_users(users)

print("All data has been successfully inserted into the database.")

connection_pool.closeall()
