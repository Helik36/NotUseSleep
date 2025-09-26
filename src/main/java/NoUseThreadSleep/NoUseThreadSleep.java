package NoUseThreadSleep;
import java.util.concurrent.Semaphore;


/*
Работа скрипта заключается в том, что потоки запускаются, и далее по очереди прибавляют сумму, к общему счётчику
При достижении максимального значения maxLimitCount, происходит изменение статуса для цикла while в классе MyThread и завершение скрипта
В процессе прибавлений суммы к счётчику, паралельно происходит проверка - достиг ли счётчик максимального значения. Если
нет, то поток будет ожидать вызова когда это произойдёт

Если счётчик не достиг этого значения, поток уходит в ожидание (строки 45-50). Поток ждём, пока не возобновиться его раббота вызовом метода notifyAll.
Таким образом, независимо от времени, ожидание будет до того момента, пока counter не дойдёт до значения maxLimitCount

CountGeneral - Общий счётчик, куда будут складываться числа со всех тредов до опредённого лимита
ActiveStatus - Состояние активности для цикла while в MyThread
MyThread - Основной класс, где происходит вся работа

Потоки
 */

public class NoUseThreadSleep {

    public static void main(String[] args) {
        

        // Количество потоков
        int maxThreads = 5;

        //Максимальное значение для глобального счётчика
        int maxLimitCount = 123;

        Semaphore sem = new Semaphore(1, true);

        ActiveStatus activeStatus = new ActiveStatus();
        CountGeneral countGeneral = new CountGeneral();

        for (int numberThread = 1; numberThread <= maxThreads; numberThread++) {

            MyThread myThread = new MyThread(sem, countGeneral, activeStatus);

            new Thread(myThread, "Thread-" + numberThread).start();
        }

        synchronized(countGeneral) {

            // Проверяем, не достиг ли счётчик максимального значения
            // Тут происходит ожидание, пока лимит не будет достигнуть
            while(countGeneral.getCounter() < maxLimitCount) {

                try {
                    countGeneral.wait();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            //Если лимит был достигнут, меняем состояние на false, для выхода из цикла в MyThread
            activeStatus.disable();
        }
    }
}

// Глобальный счётчик
class CountGeneral {

    private int counter = 0;

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized void addCounter() {
        counter = counter + 1;
        notifyAll();
    }

    public synchronized void echo() {
        notifyAll();
    }
}

// Состояние работы
class ActiveStatus {

    private boolean isActive = true;

    public synchronized boolean isActive() {
        return isActive;
    }

    public synchronized void disable() {
        isActive = false;
    }
}


class MyThread implements Runnable {

    // Переменная сколько раз будет сложений в цикле for
    int maxAddCount = 5;
    Semaphore sem;

    private final ActiveStatus activeStatus;
    private final CountGeneral countGeneral;

    MyThread(Semaphore sem, CountGeneral countGeneral, ActiveStatus activeStatus) {
        this.sem = sem;
        this.countGeneral = countGeneral;
        this.activeStatus = activeStatus;
    }

    public void run() {

        String nameOfThread = Thread.currentThread().getName();

        try {

            while (activeStatus.isActive()) {

                sem.acquire();

                if (activeStatus.isActive()) {

                    for (int i=0; i < maxAddCount; i++){
                        countGeneral.addCounter();
                        System.out.printf("%s текущее значение: %d\n", nameOfThread, countGeneral.getCounter());
                    }
                    System.out.println("\n");

                } else {
                    sem.release();

                }
                sem.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(nameOfThread + " выкл");
    }
}
