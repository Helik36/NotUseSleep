package tests;

import java.util.ArrayList;

public class test {

    public static void main(String[] args) {

        int maxThreads = 3;

        ThreadGroup activThread = new ThreadGroup("MyThread");
        dataThread dt = new dataThread();
        CountGeneral cg = new CountGeneral();

        for (int numberThread = 1; numberThread <= maxThreads; numberThread++) {

            MyThread myThread = new MyThread(cg, dt, activThread);
            new Thread(activThread, myThread, "Thread-" + numberThread).start();
        }

        try {
            Thread.sleep(10000);
            dt.disable();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class CountGeneral {

    private int counter = 0;

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized void addCounter() {
        counter = counter + 1;
    }
}


class dataThread {

    private final ArrayList<String> nameThreads = new ArrayList<>();
    private boolean isActive = true;

    public synchronized void addNameThreads(String name) {
        nameThreads.add(name);
    }

    public synchronized ArrayList<String> getNameThreads() {
        return new ArrayList<>(nameThreads);
    }

    public synchronized void clearNameThreads(){
        nameThreads.clear();
    }

    public synchronized boolean isActive() {
        return isActive;
    }

    public synchronized void disable() {
        isActive = false;
    }
}


class MyThread implements Runnable {

    private final dataThread dt;
    private final CountGeneral cg;
    private final ThreadGroup activThread;

    MyThread(CountGeneral cg, dataThread dt, ThreadGroup activThread) {
        this.cg = cg;
        this.dt = dt;
        this.activThread = activThread;
    }

    public void run() {

        String nameOfThread = Thread.currentThread().getName();

        synchronized (cg) {
            while (dt.isActive()) {

                if (!dt.getNameThreads().contains(nameOfThread)){
                    dt.addNameThreads(nameOfThread);
                }

                cg.addCounter();
                System.out.printf("%s текущее значение: %d\n", nameOfThread, cg.getCounter());

                if (activThread.activeCount() == dt.getNameThreads().size() && cg.getCounter() % 5 == 0 ) {
                    cg.notifyAll();
                    dt.clearNameThreads();
                }

                if (cg.getCounter() % 5 == 0 && dt.isActive()) {
                    try {
                        System.out.println(nameOfThread + " Спит\n");
                        cg.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

            if (!dt.isActive()){
                cg.notifyAll();
            }
        }

        System.out.println(nameOfThread + " завершился.");
    }
}
