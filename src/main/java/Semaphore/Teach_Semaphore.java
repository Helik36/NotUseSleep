package Semaphore;
import java.util.concurrent.Semaphore;

// https://metanit.com/java/tutorial/8.6.php

/// Смысл Semaphore в том, Что мы заранее объявлеяем, какое количество потоков имеет доступ к методу
/// Т.е мы можем указать, что к методу одновременно может иметь только 1 или 10 поток
/// При этом нужно - 1) Объявлять об этом и какое количество
/// 2) Самому указать, в какой момент работы кода доступ свободен от потока

/// Semaphore(int permits)
/// int permits — начальное и максимальное значение счетчика.
/// То есть то, сколько потоков одновременно могут иметь доступ к общему ресурсу;
///
/// Semaphore(int permits, boolean fair)
/// boolean fair — для установления порядка, в котором потоки будут получать доступ.
/// Если fair = true, доступ предоставляется ожидающим потокам в том порядке, в котором они его запрашивали.
/// Если же он равен false, порядок будет определять планировщик потоков.

public class Teach_Semaphore {

    public static void main(String[] args) {

        // Параметр permits указывает на количество допустимых разрешений для доступа к ресурсу.
        Semaphore sem = new Semaphore(1); // 1 разрешение
        CommonResource res = new CommonResource();

        new Thread(new CountThread(res, sem, "CountThread 1")).start();
        new Thread(new CountThread(res, sem, "CountThread 2")).start();
        new Thread(new CountThread(res, sem, "CountThread 3")).start();
    }
}
class CommonResource{

    int x=0;
}

class CountThread implements Runnable{

    CommonResource res;
    Semaphore sem;
    String name;

    CountThread(CommonResource res, Semaphore sem, String name){
        this.res=res;
        this.sem=sem;
        this.name=name;
    }

    public void run(){

        try {
            System.out.println(name + " ожидает разрешение");

            // Получаем разрешение
            sem.acquire(); // Для получения разрешения у семафора надо вызвать метод acquire(),

            res.x=1;
            for (int i = 1; i < 5; i++){

                System.out.println(this.name + ": " + res.x);
                res.x++;
                Thread.sleep(100);
            }
        }
        catch(InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(name + " освобождает разрешение");

        // После окончания работы с ресурсом полученное ранее разрешение надо освободить с помощью метода release()
        sem.release();
    }
}
