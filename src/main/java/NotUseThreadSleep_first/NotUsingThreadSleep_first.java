package NotUseThreadSleep_first;
import java.util.ArrayList;

public class NotUsingThreadSleep_first {

    public static void main(String[] args) {

        int maxThreads = 3;

        ThreadGroup activThread = new ThreadGroup("MyThread");
        QueueThread qt = new QueueThread();
        CountGeneral cg = new CountGeneral();

        for (int numberThread = 1; numberThread <= maxThreads; numberThread++) {

            new Thread(activThread, new MyThread(cg, qt, maxThreads, activThread)).start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class CountGeneral {

    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void addCounter() {
        counter = counter + 1;
    }
}


class QueueThread {

    private ArrayList<String> nameThreads = new ArrayList<>();

    public ArrayList<String> getNameThreads() {
        return nameThreads;
    }

    public void addNameThreads(String name) {
        nameThreads.add(name);
    }
}


class MyThread implements Runnable {

    int maxThreads;
    ThreadGroup activThread;
    QueueThread qt;
    CountGeneral cg;

    static boolean isActive = true;

    MyThread(CountGeneral cg, QueueThread qt, int maxThreads, ThreadGroup activThread) {

        this.maxThreads = maxThreads;
        this.activThread = activThread;
        this.cg = cg;
        this.qt = qt;
    }

    void disable(){
        isActive=false;
    }

    MyThread(){
        isActive = true;
    }

    public void run() {

        synchronized (cg) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String nameOfThread = Thread.currentThread().getName();
            qt.addNameThreads(nameOfThread);

            while (isActive) {

                System.out.println(isActive);
                cg.addCounter();
                System.out.printf("%s current counter is: " + cg.getCounter() + "\n", nameOfThread);

                if (activThread.activeCount() == qt.getNameThreads().size() && cg.getCounter() % 5 == 0 ) {
                    cg.notifyAll();
                }

                if (cg.getCounter() == 20) {
                    isActive = false;
                    return;
                }

                if (cg.getCounter() % 5 == 0) {

                    try {
                        System.out.println(Thread.currentThread().getName() + " Спит\n");
                        cg.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}