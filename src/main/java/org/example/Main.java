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


public class Main {
    public static void main(String[] args) {

        Thread t = Thread.currentThread(); // получаем главный поток
        System.out.println(t.getName()); // main
        System.out.println(t); // main

        // Создаём свой класс на основе Thread
        System.out.println("Main thread started...");
        new JThread("JThread").start();
        System.out.println("Main thread finished...\n\n");

        // Запуск нескольких потоков
        // # 1
        System.out.println("Main thread started...");
        for(int i=1; i < 6; i++)
            new JThread("JThread " + i).start();
        System.out.println("Main thread finished...");

        try {
            Thread.sleep(5000);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }


    }
}