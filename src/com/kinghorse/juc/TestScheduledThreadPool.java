package com.kinghorse.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestScheduledThreadPool {

	public static void main(String[] args) throws Exception {
		
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
		
		Future<Integer> future = pool.schedule(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				int sum = 0;
				for (int i = 0; i <= 100; i++) {
					sum += i;
				}
				System.out.println(sum);
				return sum;
			}
		}, 2, TimeUnit.SECONDS);
		
		pool.shutdown();
		
		System.out.println(future.get());
	}

}
