package com.study.DubboWithoutSpring;

import java.io.IOException;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;


/**
 * 不使用Spring容器
 * @author 01376083
 *
 */
public class SpringMainWithoutSpringTest {

	public static void main(String[] args) throws IOException {


		// 服务实现
		DemoService xxxService = new DemoServiceImpl();
		 
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");
		application.setOwner("linjian");
		 
		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("zookeeper://127.0.0.1:2181");
		//registry.setAddress("zookeeper://10.202.34.200:2181?backup=10.202.34.201:2181,10.202.34.202:2181");
		
		/*registry.setUsername("aaa");
		registry.setPassword("bbb");*/
		 
		// 服务提供者协议配置
		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setName("dubbo");
		protocol.setPort(20880);
		protocol.setThreads(200);
		 
		// 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
		 
		// 服务提供者暴露服务配置
		ServiceConfig<DemoService> service = new ServiceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
		service.setApplication(application);
		service.setRegistry(registry); // 多个注册中心可以用setRegistries()
		service.setProtocol(protocol); // 多个协议可以用setProtocols()
		service.setInterface(DemoService.class);
		service.setRef(xxxService);
		service.setVersion("1.0.0");
		
		/*
		ReferenceConfigCache cache = ReferenceConfigCache.getCache(); 
		*/
		 
		// 暴露及注册服务
		service.export();
		
		
		System.out.println("Dubbo start OK!");
		
		
		System.in.read(); // 按任意键退出

	}

}
