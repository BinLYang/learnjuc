package com.kingbin.testjava8;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.StreamHandler;
import org.junit.Test;

public class TestLambda {

	List<User> lists = Arrays.asList(new User("张三", 20, 5000.00),
			new User("李四", 21, 6000.00),
			new User("王五", 25, 8000.00),
			new User("赵六", 28, 7777.77));
	
	@Test
	public void test(){
		lists.stream()
			 .filter((user) -> user.getSalary() >= 7000)
			 .forEach(System.out::println);
	}
	
	public String stringHandler(String string,Function<String, String> mf){
		return mf.apply(string);
	}
	
	@Test
	public void test1(){
		String string = stringHandler("  hh  hh  ", (str) -> str.trim());
		System.out.println(string);
	}
	
	Integer[] num = {1,2,3,4,5};
	@Test
	public void test2(){
		Arrays.stream(num).map((x) -> x * x).forEach(System.out::println);
	}
	
	@Test
	public void test3(){
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt);
		
		Instant instant = Instant.now();
		System.out.println(instant);
		
		OffsetDateTime odt = instant.atOffset(ZoneOffset.ofHours(8));
		System.out.println(odt);
	}
}
