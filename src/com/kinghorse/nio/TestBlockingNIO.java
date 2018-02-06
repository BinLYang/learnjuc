package com.kinghorse.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * 阻塞式NIO
 * 使用NIO完成网络通信的三个核心：
 * 	1.通道（Channel）：负责连接
 * 	
 * 
 * 
 * 	2.缓冲区(Buffer):负责数据的存取
 * 	3.选择器(Selector):是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况
 * 
 * 上
 * @author ThinkPad
 *
 */
public class TestBlockingNIO {
	
	//	客户端
	@Test
	public void cilent() throws IOException{
		//1.获取通道
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		
		FileChannel fChannel = FileChannel.open(Paths.get(""), StandardOpenOption.READ);
		
		//2.分配指定大小的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		//3.读取本地文件，并发送到服务端
		while (fChannel.read(buf) != -1) {
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		
		//4.关闭通道
		fChannel.close();
		sChannel.close();
	}
	
	//服务器端
	@Test
	public void server() throws IOException{
		//1.获取通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		
		FileChannel outChannel = FileChannel.open(Paths.get(""), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		//2.绑定连接
		ssChannel.bind(new InetSocketAddress(9898));
		
		//3.获取客户端的连接
		SocketChannel sChannel = ssChannel.accept();
		
		//4.分配指定大小的缓冲区
		ByteBuffer dst = ByteBuffer.allocate(1024);
		
		//5.接受客户端数据，并保存到本地
		while (sChannel.read(dst) != -1) {
			dst.flip();
			outChannel.write(dst);
			dst.clear();
		}
		
		//6.关闭通道
		sChannel.close();
		outChannel.close();
		ssChannel.close();
		
	}

}
