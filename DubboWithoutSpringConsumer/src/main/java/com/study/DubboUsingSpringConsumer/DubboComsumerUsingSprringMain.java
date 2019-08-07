package com.study.DubboUsingSpringConsumer;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.study.DubboWithoutSpring.DemoService;

public class DubboComsumerUsingSprringMain {
	
	public static void main(String[] args) throws IOException {


		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-dubbo-consumer.xml"});
        context.start();
        
        DemoService xxxService = (DemoService) context.getBean("demoService");
		xxxService.sayHello("Lin Jian DubboComsumerUsingSprringMain");
		System.out.println("OK");
        
        System.in.read(); // 按任意键退出

	}

}
