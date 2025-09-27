import json
import time
from threading import Thread
from time import sleep

import requests

def send_req():
    time_r_start = time.time()

    header = {
        "Content-Type": "application/json"

    }
    body = {
        "message": "ping"
    }

    r = requests.post("http://localhost:8080/stub/ping", json=body).json()

    answer = json.dumps(r, indent=4, ensure_ascii=False)
    time_r_end = time.time()
    print(f"{answer} - {time_r_end - time_r_start}")


for i in range(2):
    th = Thread(target=send_req())
    sleep(1)
    th.start()

