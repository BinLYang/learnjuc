package com.kinghorse.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 要求:开启3个线程 ,其ID为：A、B、C ，每个线程将自己的ID在屏幕上打印10遍，要求输出的结果必须按顺序显示：ABCABCABC....
 * @author ThinkPad
 *
 */
public class TestABCAlternate {

	public static void main(String[] args) {
		AlternateDemo demo = new AlternateDemo();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					demo.loopA(i);
				}
			}
		},"A").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					demo.loopB(i);
				}
			}
		},"B").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					demo.loopC(i);
				}
			}
		},"C").start();
	}
}

class AlternateDemo{
	private int num = 1;//	当前正在执行线程标记
	
	private Lock lock = new ReentrantLock();
	private Condition condition1 = lock.newCondition();
	private Condition condition2 = lock.newCondition();
	private Condition condition3 = lock.newCondition();
	
	/**
	 * 
	 * @param totalLoop 循环第几轮
	 */
	public void loopA(int totalLoop) {
		lock.lock();
		
		try {
			//	1.判断
			if (num != 1) {
				try {
					condition1.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//	2.打印
			//for (int i = 0; i <= 5; i++) {
				System.out.println(Thread.currentThread().getName() + "\t"  + "\t" + totalLoop);
			//}
			//	3.唤醒
			num = 2;
			condition2.signal();
			
		} finally {
			lock.unlock();
		}
	}
	
	public void loopB(int totalLoop) {
		lock.lock();
		
		try {
			//	1.判断
			if (num != 2) {
				try {
					condition2.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//	2.打印
			//for (int i = 0; i <= 5; i++) {
				System.out.println(Thread.currentThread().getName() + "\t"  + "\t" + totalLoop);
			//}
			//	3.唤醒
			num = 3;
			condition3.signal();
			
		} finally {
			lock.unlock();
		}
	}
	
	public void loopC(int totalLoop) {
		lock.lock();
		
		try {
			//	1.判断
			if (num != 3) {
				try {
					condition3.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//	2.打印
			//for (int i = 0; i <= 5; i++) {
				System.out.println(Thread.currentThread().getName() + "\t"  + "\t" + totalLoop);
			//}
			//	3.唤醒
			num = 1;
			condition1.signal();
			
		} finally {
			lock.unlock();
		}
	}
	
}