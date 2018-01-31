package com.kinghorse.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用于解决多线程安全问题的方式：
 * 	synchronized:隐式锁
 * 	1.同步代码块
 * 	2.同步方法
 * 
 * jdk1.5以后
 * 	3.同步锁Lock
 * 	注意：lock()上锁，unlock()释放锁
 * @author ThinkPad
 *
 */
public class TestLock {

	public static void main(String[] args) {
		Ticket ticket = new Ticket(); 
		new Thread(ticket,"1号窗口").start();
		new Thread(ticket,"2号窗口").start();
		new Thread(ticket,"3号窗口").start();
	}
}

class Ticket implements Runnable{

	private int tick = 100;
	private Lock lock = new ReentrantLock();
	@Override
	public void run() {
		while (true) {
			lock.lock();
			
			try {
				Thread.sleep(200);
				if (tick > 0) {
					System.out.println(Thread.currentThread().getName() + "完成售票，余票数为：" + --tick);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				lock.unlock();
			}
		}
	}
	
}
