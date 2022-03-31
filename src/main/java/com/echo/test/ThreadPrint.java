package com.echo.test;

public class ThreadPrint {
    public static Integer index = 0;
    public static String str = "helloworl";
    public static class MyThread1 implements Runnable{
        public void run() {
            // 两个 while 一定要牢记
            while(true){
                while ((index % 3) != 0) {
                    Thread.yield();
                }
                System.out.println("MyThread1" + str.charAt(index));
                index ++;
            }
        }
    }
    public static class MyThread2 implements Runnable{
        public void run(){
            while(true){
                while((index % 3) != 1){
                    Thread.yield();
                }
                System.out.println("MyThread2" + str.charAt(index));
                index ++;
            }
        }
    }
    public static class MyThread3 implements Runnable{
        public void run(){
            while(true){
                while((index % 3) != 2){
                    Thread.yield();
                }
                System.out.println("MyThread3" + str.charAt(index));
                if(index == 8){
                    index = 0;
                    System.out.println("___________");
                }else {
                    index ++;
                }
            }
        }
    }
    public static void main(String[] args){
        MyThread1 thread1 = new MyThread1();
        MyThread2 thread2 = new MyThread2();
        MyThread3 thread3 = new MyThread3();
        new Thread(thread1).start();
        new Thread(thread2).start();
        new Thread(thread3).start();
    }
}