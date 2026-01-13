import java.util.concurrent.Semaphore;

class Account {
    int id;
    int balance;
    Semaphore lock = new Semaphore(1);

    Account(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }
}

public class NoDeadlockDemo {

    static Account account1 = new Account(1, 1000);
    static Account account2 = new Account(2, 1000);

    static void transfer(Account a, Account b, int amount) {
        Account firstLock, secondLock;

        // Lock ordering by account ID
        if (a.id < b.id) {
            firstLock = a;
            secondLock = b;
        } else {
            firstLock = b;
            secondLock = a;
        }

        try {
            System.out.println(Thread.currentThread().getName()
                    + " locking account " + firstLock.id);
            firstLock.lock.acquire();

            Thread.sleep(100);

            System.out.println(Thread.currentThread().getName()
                    + " locking account " + secondLock.id);
            secondLock.lock.acquire();

            // Critical Section
            a.balance -= amount;
            b.balance += amount;

            System.out.println(Thread.currentThread().getName()
                    + " transfer completed");

            secondLock.lock.release();
            firstLock.lock.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            transfer(account1, account2, 100);
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            transfer(account2, account1, 200);
        }, "Thread-2");

        t1.start();
        t2.start();
    }
}
