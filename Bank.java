import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {

    private static int balance = 0;
    private static Lock lock = new ReentrantLock(); // 👈 LOCK HERE

    public void deposit() {
        balance += 100;
    }

    public void withdraw() {
        balance -= 100;
    }

    public int getValue() {
        return balance;
    }

    public void run() {
        lock.lock();           // 🔒 acquire lock
        try {
            deposit();
            System.out.println(
                "Value for Thread after deposit " +
                Thread.currentThread().getName() +
                " " + getValue()
            );

            withdraw();
            System.out.println(
                "Value for Thread after withdraw " +
                Thread.currentThread().getName() +
                " " + getValue()
            );
        } 
        finally {
            lock.unlock();     // 🔓 release lock (VERY IMPORTANT)
        }
    }
}   
