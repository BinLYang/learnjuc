package com.kinghorse.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的方式三：实现Callable接口
 * 相较于Runnable接口，方法有返回值，并且可以抛出异常
 * 执行Callable方式，需要FutureTask实现类的支持，用于接收运算结果。FutureTask是Future接口的实现类
 * @author ThinkPad
 *
 */
public class TestCallable {

	public static void main(String[] args) {
		ThreadDemo demo = new ThreadDemo();
		
		//	执行Callable方式，需要FutureTask实现类的支持，用于接收运算结果。
		FutureTask<Integer> result = new FutureTask<>(demo);
		
		new Thread(result).start();
		
		//	接受运算结果
		try {
			Integer sum = result.get();	//	FutureTask 可用于 闭锁(CountDownLatch)
			System.out.println("求和：" + sum);
			System.out.println("-------------------------------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}

class ThreadDemo implements Callable<Integer>{

	@Override
	public Integer call() throws Exception {

		int sum = 0;
		for(int i = 0; i <= 100; i++){
			sum += i;
		}
		return sum;
	}
	
}
