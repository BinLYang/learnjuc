package com.kinghorse.juc;

/**
 * 模拟生产者和消费者
 * object.wait().防止虚假唤醒，应该放在循环中while(){}
 * @author ThinkPad
 *
 */
public class TestProducerAndConsumer {

	public static void main(String[] args) {
		Clerk clerk = new Clerk();
		Producer producer = new Producer(clerk);
		Consumer consumer = new Consumer(clerk);
		
		new Thread(producer,"生产者A").start();
		new Thread(consumer, "消费者B").start();
		
	}
}

class Clerk{
	
	private int product = 0;
	
	//	进货
	public synchronized void get(){
		while (product >= 10) {
			System.out.println("产品已满！");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			System.out.println(Thread.currentThread().getName() + ":" + ++product);
			this.notifyAll();
	}
	
	//	卖货
	public synchronized void sale(){
		while (product <= 0) {
			System.out.println("产品缺货！");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			System.out.println(Thread.currentThread().getName() + ":" + --product);
			this.notifyAll();
	}
}

class Producer implements Runnable{
	private Clerk clerk;
	
	public Producer(Clerk clerk){
		this.clerk = clerk;
	}
	
	public void run(){
		for(int i = 0; i < 20; i++){
			clerk.get();
		}
	}
}

class Consumer implements Runnable{

	private Clerk clerk;
	
	public Consumer(Clerk clerk){
		this.clerk = clerk;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < 20; i++){
			clerk.sale();		}
	}
	
}