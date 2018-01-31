package com.kinghorse.juc;

import java.time.LocalDate;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟生产者和消费者
 * object.wait().防止虚假唤醒，应该放在循环中while(){}
 * @author ThinkPad
 *
 */
public class TestProducerAndConsumerLock {

	public static void main(String[] args) {
		Clerk1 clerk = new Clerk1();
		Producer1 producer = new Producer1(clerk);
		Consumer1 consumer = new Consumer1(clerk);
		
		new Thread(producer,"生产者A").start();
		new Thread(consumer, "消费者B").start();
		
	}
}

class Clerk1{
	
	private int product = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	//	进货
	public void get(){
		lock.lock();
		try{
			while (product >= 10) {
				System.out.println("产品已满！");
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				System.out.println(Thread.currentThread().getName() + ":" + ++product);
				condition.signalAll();
		}finally {
			lock.unlock();
		}
	}
	
	//	卖货
	public void sale(){
		lock.lock();
		try{
			while (product <= 0) {
				System.out.println("产品缺货！");
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				System.out.println(Thread.currentThread().getName() + ":" + --product);
				condition.signalAll();
		}finally {
			lock.unlock();
		}
		
	}
}

class Producer1 implements Runnable{
	private Clerk1 clerk;
	
	public Producer1(Clerk1 clerk){
		this.clerk = clerk;
	}
	
	public void run(){
		for(int i = 0; i < 20; i++){
			clerk.get();
		}
	}
}

class Consumer1 implements Runnable{

	private Clerk1 clerk;
	
	public Consumer1(Clerk1 clerk){
		this.clerk = clerk;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 20; i++){
			clerk.sale();		}
	}
	
}