package org.example;

// # 1
class JThread extends Thread {

    JThread(String name){
        super(name);
    }

    public void run(){

        System.out.printf("%s started... \n", Thread.currentThread().getName());
        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }
        System.out.printf("%s fiished... \n", Thread.currentThread().getName());
    }
}

// # 2
class CommonResource{

    int x=0;
}
// # 2
class CountThread implements Runnable {

    CommonResource res;

    CountThread(CommonResource res) {
        this.res = res;
    }

    public void run() {

        synchronized (res) { // Этот оператор предваряет блок кода или метод, который подлежит синхронизации
            // При применении оператора synchronized к методу пока этот метод не завершит выполнение, монопольный доступ имеет только один поток - первый, который начал его выполнение.
            res.x = 1;
            for (int i = 1; i < 5; i++) {
                System.out.printf("%s %d \n", Thread.currentThread().getName(), res.x);
                res.x++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {

//        Thread t = Thread.currentThread(); // получаем главный поток
//        System.out.println(t.getName()); // main
//        System.out.println(t); // main
//
//        // Создаём свой класс на основе Thread
//        System.out.println("Main thread started...");
//        new JThread("JThread").start();
//        System.out.println("Main thread finished...\n\n");
//
//        // Запуск нескольких потоков
//        // # 1
//        System.out.println("Main thread started...");
//        for(int i=1; i < 6; i++)
//            new JThread("JThread " + i).start();
//        System.out.println("Main thread finished...");
//
//        try {
//            Thread.sleep(5000);
//        }
//        catch(InterruptedException e){
//            System.out.println("Thread has been interrupted");
//        }

        // # 2
        CommonResource commonResource= new CommonResource();
        for (int i = 1; i < 6; i++) {

            Thread d = new Thread(new CountThread(commonResource));
            d.setName("Thread " + i);
            d.start();
        }
    }
}