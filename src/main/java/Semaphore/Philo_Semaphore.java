package Semaphore;
import java.util.concurrent.Semaphore;

// https://metanit.com/java/tutorial/8.6.php

/// Смысл Semaphore в том, Что мы заранее объявлеяем, какое количество потоков имеет доступ к методу
/// Т.е мы можем указать, что к методу одновременно может иметь только 1 или 10 поток
/// При этом нужно - 1) Объявлять об этом и какое количество
/// 2) Самому указать, в какой момент работы кода доступ свободен от потока


public class Philo_Semaphore {

    public static void main(String[] args) {

        Semaphore sem = new Semaphore(2); // 2 разрешение

        for(int i=1;i<6;i++)
            new Philosopher(sem,i).start();
    }
}

// класс философа
class Philosopher extends Thread {

    Semaphore sem; // семафор. ограничивающий число философов

    // кол-во приемов пищи
    int num = 0;

    // условный номер философа
    int id;

    // в качестве параметров конструктора передаем идентификатор философа и семафор
    Philosopher(Semaphore sem, int id) {
        this.sem=sem;
        this.id=id;
    }

    public void run()
    {
        try
        {
            while(num<1)
            {
                //Запрашиваем у семафора разрешение на выполнение
                sem.acquire();
                System.out.println ("Философ " + id+" садится за стол");
                // философ ест
                sleep(5000);
                num++;

                System.out.println ("Философ " + id+" выходит из-за стола");


                // После окончания работы с ресурсом полученное ранее разрешение надо освободить с помощью метода release()
                sem.release();

                // философ гуляет
                sleep(5000);
            }
        }
        catch(InterruptedException e)
        {
            System.out.println ("у философа " + id + " проблемы со здоровьем");
        }
    }
}
