package Exchanger;
import java.util.concurrent.Exchanger;


// https://metanit.com/java/tutorial/8.7.php

/// Класс Exchanger предназначен для обмена данными между потоками.
/// Он является типизированным и типизируется типом данных, которыми потоки должны обмениваться.
///
/// Обмен данными производится с помощью единственного метода этого класса exchange():
/// V exchange(V x) throws InterruptedException
/// V exchange(V x, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException
///
/// Параметр x представляет буфер данных для обмена.
/// Вторая форма метода также определяет параметр timeout - время ожидания и unit - тип временных единиц, применяемых для параметра timeout.
///
///

public class Teach_Exchanger {
    public static void main(String[] args) {

        Exchanger<String> ex = new Exchanger<String>();
        new Thread(new PutThread(ex)).start();
        new Thread(new GetThread(ex)).start();
    }
}

class PutThread implements Runnable{

    Exchanger<String> exchanger;
    String message;

    PutThread(Exchanger<String> ex){

        this.exchanger=ex;
        message = "Hello Java!";
    }
    public void run(){

        try{
            message=exchanger.exchange(message);
            System.out.println("PutThread has received: " + message);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }
}
class GetThread implements Runnable{

    Exchanger<String> exchanger;
    String message;

    GetThread(Exchanger<String> ex){

        this.exchanger=ex;
        message = "Hello World!";
    }
    public void run(){

        try{
            message=exchanger.exchange(message);
            System.out.println("GetThread has received: " + message);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }
}
