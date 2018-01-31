package com.kinghorse.juc;

/**
 * 题目：判断打印的是 “one” 还是 “two”
 * @author ThinkPad
 *1.两个普通同步方法，两个线程，标准打印，打印的结果是： one two
 *2.在getOne()方法中新增Thread.sleep(3000),打印结果是：one two
 *3.在Number类中新增普通方法getThree()方法，打印结果是： three one two
 *4.两个普通同步方法，两个Number对象，分别调用getOne()、getTwo()。打印结果是：two one
 *5.修改getOne()为静态同步方法。一个Number对象。打印结果是：two one
 *6.修改getOne()为静态同步方法。两个Number对象，分别调用getOne()、getTwo()。打印结果是：two one
 *7.修改getOne()和getTwo()都为静态同步方法。一个Number对象。打印结果是：two one
 *8.修改getOne()和getTwo()都为静态同步方法。两个Number对象，分别调用getOne()、getTwo()。打印结果是： one two
 *
 *线程8锁的关键
 *①非静态方法的锁默认为 this，静态方法的锁为 对应的Class类的实例
 *②某一时刻内，只有一个线程持有锁，无论几个方法
 */
public class TestThread8Monitor {
	public static void main(String[] args) {
		Number number = new Number();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				number.getOne();
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				number.getTwo();
			}
		}).start();
		
		/*new Thread(new Runnable() {
			
			@Override
			public void run() {
				number.getThree();
			}
		}).start();*/
	}

}

class Number{
	
	public static synchronized void getOne(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("one");
	}
	
	public synchronized void getTwo(){
		System.out.println("two");
	}
	
	public void getThree(){
		System.out.println("three");
	}
}
