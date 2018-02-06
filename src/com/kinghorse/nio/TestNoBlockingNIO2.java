package com.kinghorse.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * 非阻塞式DataGramChannel
 * UDP协议
 * 
 * @author ThinkPad
 *
 */
public class TestNoBlockingNIO2 {

	@Test
	public void client() throws IOException{
		DatagramChannel dChannel = DatagramChannel.open();
		
		dChannel.configureBlocking(false);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		Scanner scanner = new Scanner(System.in);
		
		while(scanner.hasNext()){
			String src = scanner.next();
			buffer.put((LocalDateTime.now().toString() + ":" + src).getBytes());
			buffer.flip();
			dChannel.send(buffer, new InetSocketAddress("127.0.0.1", 9898));
			buffer.clear();
		}
		
		dChannel.close();
	}
	
	@Test
	public void server() throws IOException{
		DatagramChannel dChannel = DatagramChannel.open();
		
		dChannel.configureBlocking(false);
		
		dChannel.bind(new InetSocketAddress(9898));
		
		Selector selector = Selector.open();
		
		dChannel.register(selector, SelectionKey.OP_READ);
		
		while (selector.select() > 0) {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			
			while (it.hasNext()) {
				SelectionKey sk = (SelectionKey) it.next();
				
				if (sk.isReadable()) {
					ByteBuffer buf = ByteBuffer.allocate(1024);
					
					dChannel.receive(buf);
					buf.flip();
					System.out.println(new String(buf.array(), 0, buf.limit()));
					buf.clear();
				}
				
			}
			it.remove();
		}
	}
}
 