package com.kingbin.testjava8;


public class Testdxc {

	public static void main(String[] args) {
		ThreadDemo threadDemo = new ThreadDemo();
		Thread thread = new Thread(threadDemo);
		thread.start();
		
		while(true){
			
			if (threadDemo.isFlag()) {
				System.out.println("**********");
				break;
			}/*else{
				System.out.println("没取到最新的flag");
			}*/
		}
	}

}

class ThreadDemo implements Runnable{
	
	private volatile boolean flag = false;
	@Override
	public void run() {

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		flag = true;
		System.out.println("flag=" + flag);
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
