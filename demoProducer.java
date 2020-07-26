package com.job;

/**
 *测试多线程+异常
 *电脑的生产和销售
 * 流水线生产指定台电脑后休眠
 * 商城销售网库存后启动流水线
 * 统计商城销售数量
 * @author 123
 * @create
 */
class Sell implements Runnable{
    public Clerk clerk;
    public Sell(Clerk clerk){
        this.clerk=clerk;
    }

    @Override
    public void run() {
        while (true) {
            clerk.SellProduct();
        }
    }

    }
class Clerk {
    //库存
    private int repertory;
    //销售数量
    private int count;
    //生产
    public synchronized void Producer(){
        notify();
        if (repertory <5) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + "生产第" + ((repertory++)+1)+ "台电脑");
        } else {
            System.out.println("目前库存"+repertory+"台"+"\n"+"流水线休眠");
            System.out.println();
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();


            }
        }
    }
    //售卖
    public synchronized void SellProduct(){
        if (repertory > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("从京东商城卖出第" +((count++)+1) + "台电脑");
            repertory--;
        } else {
            try {
                throw new IllegalSellException("目前库存数量不足: 重启流水线");
            } catch (IllegalSellException e) {
                e.printStackTrace();
                System.out.println();
                notify();
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
class Producer implements Runnable {
    private Clerk clerk;
    public Producer(Clerk clerk){
        this.clerk=clerk;
    }

    @Override
    public void run() {
        while (true){
            clerk.Producer();
        }
    }
}
class IllegalSellException extends Exception{
    public IllegalSellException(){}
    public IllegalSellException(String massage){
        super(massage);
    }
}

public class demoProducer {
    public static void main(String[] args) {
        Clerk clerk=new Clerk();

        Producer a=new Producer(clerk);
        Thread a1=new Thread(a,"一号流水线");
        a1.setPriority(10);
        a1.start();

        Sell b=new Sell(clerk);
        Thread b2=new Thread(b,"京东商城");
        b2.start();


    }
}

