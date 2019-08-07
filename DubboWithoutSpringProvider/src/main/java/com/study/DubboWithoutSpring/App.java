package com.study.DubboWithoutSpring;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		
		// 服务实现
		DemoService demoService = new DemoServiceImpl();
		 
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		
		
		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("10.202.41.99:2181");
		registry.setUsername("aaa");
		registry.setPassword("bbb");
		 
		// 服务提供者协议配置
		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setName("dubbo");
		protocol.setPort(12345);
		protocol.setThreads(200);
		 
		// 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
		
		// 服务提供者暴露服务配置
		ServiceConfig<DemoService> service = new ServiceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(application);
		service.setRegistry(registry); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocol); // 多个协议可以用setProtocols()
		service.setInterface(DemoService.class);
		service.setRef(demoService);
		service.setVersion("1.0.0");
		 
		// 暴露及注册服务
		service.export();
		
	}
}
