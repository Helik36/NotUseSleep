package Synchr;



/// Смысл synchronized (он же монитор) в том, что если ты запустишь сколько угодно потоков, к методу где объявлен  synchronized
/// имеет только 1 поток. И так в порядке очереди

class CommonResource{

    int x=0;
}

class CountThread implements Runnable {

    CommonResource res;

    CountThread(CommonResource res) {
        this.res = res;
    }

    public void run() {

        //логика, которая одновременно доступна только для одного потока
        synchronized (res) {
            // При применении оператора synchronized к методу, пока этот метод не завершит выполнение,
            //  доступ имеет ТОЛЬКО один поток - первый, который начал его выполнение.

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

public class Teach_Synchr {

    public static void main(String[] args) {

        CommonResource commonResource= new CommonResource();
        for (int i = 1; i < 6; i++) {

            Thread d = new Thread(new CountThread(commonResource));
            d.setName("Thread " + i);
            d.start();
        }
    }
}


// # 2


