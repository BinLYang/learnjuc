package com.kinghorse.juc;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 1.ReadWriteLock:读写锁
 * 写写、读写 --需要互斥
 * 读读--不需要互斥
 * @author ThinkPad
 *
 */
public class TestReadWriteLock {
	
	public static void main(String[] args) {
		ReadWriteLockDemo demo = new ReadWriteLockDemo();
		
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					demo.set((int)(Math.random()*10));
				}
			},"写数据").start();
		}
		
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					demo.get();
				}
			},"读").start();
		}
	}

	
}

class ReadWriteLockDemo{
	private int num = 0;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	//	读
	public void get(){

		lock.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + ":" + num);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	//	写
	public void set(int num){
		
		lock.writeLock().lock();
		
		try {
			System.out.println(Thread.currentThread().getName() + ":" + num);
			this.num = num;
		} finally {
			lock.writeLock().unlock();
		}
		
	}
}