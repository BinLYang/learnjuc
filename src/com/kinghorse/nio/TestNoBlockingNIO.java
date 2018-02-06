package com.kinghorse.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDate;
import java.util.Iterator;

import org.junit.Test;

/**
 * 非阻塞时NIO
 * 
 * @author ThinkPad
 *
 */
public class TestNoBlockingNIO {

	@Test
	public void client() throws IOException{
		//1.获取通道
		SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		
		//2.切换非阻塞模式
		sChannel.configureBlocking(false);
		
		//3.分配缓冲区
		ByteBuffer buf = ByteBuffer.allocate(1024);
		
		//4.发送数据
		buf.put(LocalDate.now().toString().getBytes());
		buf.flip();
		sChannel.write(buf);
		buf.clear();
		
		//5.关闭通道
		sChannel.close();
	}
	
	@Test
	public void server() throws IOException{
		//1.获取通道
		ServerSocketChannel ssChannel = ServerSocketChannel.open();
		
		//2.切换非阻塞模式
		ssChannel.configureBlocking(false);
		
		//3.绑定连接
		ssChannel.bind(new InetSocketAddress(9898));
		
		//4.获取选择器
		Selector selector = Selector.open();
		
		//5.将通道注册到选择器上
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		//6.轮询式的获取选择器上已经“准备就绪”的事件
		while(selector.select() > 0){
			//7.获取当前选择器上所有注册的选择键（已经就绪的监听事件）
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while (it.hasNext()) {
				//8.获取准备就绪的事件
				SelectionKey sk = (SelectionKey) it.next();
				
				//9.判断是什么事件
				if (sk.isAcceptable()) {
					//10.获取客户端连接
					SocketChannel sChannel = ssChannel.accept();
					
					//11.切换为非阻塞模式
					sChannel.configureBlocking(false);
					
					//12.将该通道注册到选择器上
					sChannel.register(selector, SelectionKey.OP_READ);
				}else if (sk.isReadable()) {
					//13.获取当前选择器上 读就绪 状态的通道
					SocketChannel sChannel = (SocketChannel) sk.channel();
					
					//14.读取数据
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					
					int len = 0;
					if ((len = sChannel.read(buffer)) > 0) {
						buffer.flip();
						System.out.println(new String(buffer.array(),0,len));
						buffer.clear();
					}
					
				}
				//15.取消选择键
				it.remove();
			}
		}
	}
}
