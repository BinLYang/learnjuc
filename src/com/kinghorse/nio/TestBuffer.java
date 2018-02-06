package com.kinghorse.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import javax.swing.text.Position;

import org.junit.Test;

/**
 * 一、缓冲区(Buffer)在Java NIO中负责数据的存储。缓冲区就相当于数组，用于存储不同数据类型的数据
 * 根据数据不同类型（Boolean除外），提供了以下类型的缓冲区
 * ByteBuffer
 * ShortBuffer
 * CharBuffer
 * IntBuffer
 * LongBuffer
 * DoubleBuffer
 * FolatBuffer
 * 
 * 上述缓冲区的管理方式几乎一致，通过 allocate() 获取缓冲区
 * 
 * 二、缓冲区存取数据的两个核心方法
 * 	put():存入数据
 * 	get():取出数据
 * 
 * 
 * 四、缓冲区的四个核心属性
 * capacity: 容量	
 * limit:	限制的可操作数据
 * position: 操作数据的位置
 * 
 * position<=limit<=capacity	
 * 
 * mark:标记，记录当前position的位置，可以通过reset()恢复到mark位置
 * 
 * 五、直接缓冲区和非直接缓冲区
 * 非直接缓冲区：通过allocate()方法分配缓冲区，将缓冲区建立自JVM的内存中
 * 直接缓冲区：通过allocateDirect()方法分配直接缓冲区，将缓冲区建立在物理内存中，可以提高效率
 * @author ThinkPad
 *
 */
public class TestBuffer {


	@Test
	public void test1(){
		String str = "abcdef";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		buffer.put(str.getBytes());
		buffer.flip();
		
		byte[] dst = new byte[buffer.limit()];
		buffer.get(dst, 0, 2);
		//做标记
		buffer.mark();
		System.out.println(buffer.position());
		buffer.get(dst, 2, 2);
		System.out.println(buffer.position());
		
		buffer.reset();
		System.out.println(buffer.position());
		
		//查看剩余未取出数据
		if (buffer.hasRemaining()) {
			System.out.println(buffer.remaining());
		}
	}
	
	
	@Test
	public void test(){
		
		String str = "abcde";
		//1.分配指定大小的缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		
		System.out.println("------------------");
		//2.利用put()把数据放到缓冲区
			
		byteBuffer.put(str.getBytes());
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		
		System.out.println("------------------");
		//3.切换读取数据模式
		
		byteBuffer.flip();
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		
		System.out.println("------------------");
		//4.利用get()读取数据
		byte [] dst = new byte[byteBuffer.limit()];
		byteBuffer.get(dst);
		System.out.println(new String(dst, 0, dst.length));
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		
		System.out.println("------------------");
		//5.rewind():可重复读
		byteBuffer.rewind();
		dst = new byte[byteBuffer.limit()];
		byteBuffer.get(dst);
		System.out.println(new String(dst, 0, dst.length));
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		
		System.out.println("------------------");
		//6.清空缓冲区，但是缓冲区内的数据依然存在，处于“被遗忘”状态
		byteBuffer.clear();
		System.out.println(byteBuffer.capacity());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.position());
		
		System.out.println("------------------");
		
	}
}
