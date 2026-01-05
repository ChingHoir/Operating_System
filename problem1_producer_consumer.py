import threading
import time
import random

# ----------------------------
# Shared buffer
# ----------------------------
BUFFER_SIZE = 100
buffer = []

# ----------------------------
# Semaphores
# ----------------------------
empty = threading.Semaphore(BUFFER_SIZE)  # empty slots
full = threading.Semaphore(0)              # filled slots
mutex = threading.Semaphore(1)             # mutual exclusion


# ----------------------------
# Producer function
# ----------------------------
def producer(pid):
    while True:
        # produce a pair
        p1 = f"P{pid}-1"
        p2 = f"P{pid}-2"

        # wait for two empty slots
        empty.acquire()
        empty.acquire()

        # enter critical section
        mutex.acquire()

        buffer.append(p1)
        buffer.append(p2)
        print(f"Producer {pid} produced {p1}, {p2}")

        # leave critical section
        mutex.release()

        # signal two full slots
        full.release()
        full.release()

        time.sleep(random.uniform(0.5, 1.5))


# ----------------------------
# Consumer function
# ----------------------------
def consumer():
    while True:
        # wait for two full slots
        full.acquire()
        full.acquire()

        # enter critical section
        mutex.acquire()

        p1 = buffer.pop(0)
        p2 = buffer.pop(0)
        print(f"Consumer consumed {p1}, {p2}")

        # leave critical section
        mutex.release()

        # signal two empty slots
        empty.release()
        empty.release()

        time.sleep(random.uniform(1, 2))


# ----------------------------
# Create threads
# ----------------------------
producers = []
for i in range(3):  # 3 producers
    t = threading.Thread(target=producer, args=(i,))
    producers.append(t)
    t.start()

consumer_thread = threading.Thread(target=consumer)
consumer_thread.start()
