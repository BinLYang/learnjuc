package com.kinghorse.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TestForkJoinPool {

	public static void main(String[] args) {

		ForkJoinPool pool = new ForkJoinPool();
		
		ForkJoinTask<Long> task = new ForkJoinSumCalculate(0, 100000000L);
		
		Long sum = pool.invoke(task);
		
		System.out.println(sum);
	}

}

class ForkJoinSumCalculate extends RecursiveTask<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long start;
	private long end;
	private static final long THURSHOLD=10000L;//临界值
	
	
	public ForkJoinSumCalculate(long start, long end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {

		long length = end - start;
		if (length <= THURSHOLD) {
			long sum = 0L;
			for (long i = start; i <= end; i++) {
				sum += i;
			}
			return sum;
		}else{
			long middle = (start + end)/2;
			ForkJoinSumCalculate left = new ForkJoinSumCalculate(start, middle);
			left.fork();
			
			ForkJoinSumCalculate right = new ForkJoinSumCalculate(middle + 1, end);
			right.fork();
			
			return left.join() + right.join();
		}
	}
	
}