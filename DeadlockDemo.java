import java.util.concurrent.Semaphore;

class Account {
    int balance;
    Semaphore lock = new Semaphore(1);

    Account(int balance) {
        this.balance = balance;
    }
}

public class DeadlockDemo {

    static Account account1 = new Account(1000);
    static Account account2 = new Account(1000);

    // Transfer function with critical section
    static void transfer(Account from, Account to, int amount) {
        try {
            System.out.println(Thread.currentThread().getName()
                    + " waiting for FROM account lock");
            from.lock.acquire();

            // small delay to increase deadlock chance
            Thread.sleep(100);

            System.out.println(Thread.currentThread().getName()
                    + " waiting for TO account lock");
            to.lock.acquire();

            // Critical Section
            from.balance -= amount;
            to.balance += amount;

            System.out.println(Thread.currentThread().getName()
                    + " transfer completed");

            to.lock.release();
            from.lock.release();

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
