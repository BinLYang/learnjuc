package com.kinghorse.nio;

import java.awt.im.InputContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.management.StandardEmitterMBean;

import org.junit.Test;

/**
 * 一、通道：用于连接源节点和目标节点
 * 
 * 二、通道的主要实现类
 * 	|--FileChannel
 * TCP
 * 	|--SocketChannel
 * 	|--ServerSocketChannel
 * UDP
 * 	|--DatagramChannel
 * 
 * 三、获取通道
 * 	1.Java针对支持通道的类提供了getChannel()方法
 * 		本地IO
 * 			FileInoutStream/FileOutPutStream
 * 			RandomAccessFile
 * 		网络IO
 * 			Socket
 * 			ServerSocket
 * 			DatagramSocket
 * 
 * 2.在JDK1.7中的NIO.2针对各通道提供了静态方法open()
 * 3.在JDK1.7中的NIO.2的Files工具类的newByteChannel()
 * 
 * 四、通道之间的数据传输
 * 	transferFrom()
 *  transferTo()
 * 
 * 五、分散（Scatter）与聚集（Gather）
 * 
 * 分散读取（Scattering Reads）:将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）:将多个缓冲区中的数据聚集到通道中
 * 
 * @author ThinkPad
 *
 */
public class TestChannel {
	
	//	分散聚集
	@Test
	public void test3() throws IOException{
		RandomAccessFile raf1 = new RandomAccessFile("C:\\Users\\ThinkPad\\Desktop\\新建文本文档.txt", "rw");
		RandomAccessFile raf2 = new RandomAccessFile("C:\\Users\\ThinkPad\\Desktop\\新建文本文档-copy.txt", "rw");
		//	获取通道
		FileChannel channel = raf1.getChannel();
		FileChannel channel2 = raf2.getChannel();
		
		//	分配多个缓冲区
		ByteBuffer bf1 = ByteBuffer.allocate(100);
		ByteBuffer bf2 = ByteBuffer.allocate(100);
		
		//分散读取
		ByteBuffer[] bfs = {bf1,bf2};
		while(channel.read(bfs) != -1){
			
			
			for (ByteBuffer buffer : bfs) {
				buffer.flip();
				channel2.write(buffer);
				System.out.println(new String(bfs[0].array(),0,bfs[0].limit()));
				System.out.println("-----------------------------------------");
				System.out.println(new String(bfs[1].array(),0,bfs[1].limit()));
				System.out.println("*****************************************");
				
				buffer.clear();
			}
			
		}
		channel.close();
		channel2.close();
	}
	
	//通道之间数据传输（直接缓冲区）
	@Test
	public void test2() throws IOException{
		FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\ThinkPad\\Desktop\\ERP.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\ThinkPad\\Desktop\\ERP-copy.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ
				,StandardOpenOption.CREATE);
		inChannel.transferTo(0, inChannel.size(), outChannel);
		
		inChannel.close();
		outChannel.close();
	}

	//	使用 <直接缓存区> 完成文件的复制
	@Test
	public void test1() throws IOException{
		FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\ThinkPad\\Desktop\\ERP.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\ThinkPad\\Desktop\\ERP-copy.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ
				,StandardOpenOption.CREATE);
		
		//内存映射文件
		MappedByteBuffer inBuffer = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outBuffer = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		//直接对缓冲区进行数据读写的操作
		byte[] dst = new byte[inBuffer.limit()];
		inBuffer.get(dst);
		outBuffer.put(dst);
		
		inChannel.close();
		outChannel.close();
	}
	
	
	
	//	利用通道完成文件的复制(非直接缓冲区)
	@Test
	public void test() throws Exception{
		FileInputStream fis = new FileInputStream("C:\\Users\\ThinkPad\\Desktop\\ERP.jpg");
		FileOutputStream fos = new FileOutputStream("C:\\Users\\ThinkPad\\Desktop\\ERP-copy.jpg");
		
		//	获取通道
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		
		//	开启缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		//	将通道数据写入缓存区
		while(inChannel.read(buffer) != -1){
			buffer.flip();//	切换为读取数据模式
			outChannel.write(buffer);
			buffer.clear();//	清空缓存区数据
		}
		
		outChannel.close();
		inChannel.close();
		fos.close();
		fis.close();
	}
}
