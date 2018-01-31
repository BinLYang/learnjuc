package com.kinghorse.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.PooledConnection;

/**
 * 一、线程池：提供一个线程队列，队列中保存着所有等待状态的线程，避免了创建与销毁的额外开销，提高了响应速度
 * 二、线程池的体系结构
 * 		java.util.concurrent.Executor:负责线程使用和调度的根接口
 * 			|--ExecutorService 子接口，线程池的主要接口
 * 				|--ThreadPoolExeutor 线程池的实现类
 * 				|--ScheduledExecutorService 子接口，负责线程的调度
 * 					|--ScheduledThreadPoolExecutor :继承了ThreadPoolExeutor 实现了ScheduledExecutorService
 * 三、工具类：Executors
 * 	ExecutorService newFixedThreadPool():创建固定大小的线程池
 * 	ExecutorService	newCachedThreadPool():缓存线程池，线程池数量不固定，可更具需求自动更改数量
 * 	ExecutorService	newSingleThreadExecutor():创建单个线程
 * 
 * 	ScheduledExecutorService newScheduledThreadPool():创建固定大小的线程，可以延迟或定时任务
 * 
 * @author ThinkPad
 *
 */
public class TestThreadPool {

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(5);
		
		ThreadPoolDemo demo = new ThreadPoolDemo();
		
		pool.submit(demo);
	}
	
	
	
}

class ThreadPoolDemo implements Runnable{

	private int i = 0;
	@Override
	public void run() {
		for (int i = 0; i <= 100; i++) {
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
		
	}
	
}
