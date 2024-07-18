import pandas as pd
import random
from faker import Faker
import os

fake = Faker()

def generate_unique_id(existing_ids):
    while True:
        new_id = fake.word() + str(random.randint(1000000, 9999999))
        if new_id not in existing_ids:
            return new_id

def replace_duplicate_ids():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    csv_filename = os.path.join(script_dir, 'users.csv')
    df = pd.read_csv(csv_filename)

    unique_ids = set(df.iloc[:, 0])
    id_replacements = {}

    for idx, id_value in enumerate(df.iloc[:, 0]):
        if id_value in id_replacements:
            df.iloc[idx, 0] = id_replacements[id_value]
        elif id_value in unique_ids:
            unique_ids.remove(id_value)
        else:
            new_id = generate_unique_id(unique_ids)
            id_replacements[id_value] = new_id
            df.iloc[idx, 0] = new_id
            unique_ids.add(new_id)

    df.to_csv('users.csv', index=False)
    print("Duplicate IDs replaced and file saved.")

replace_duplicate_ids()