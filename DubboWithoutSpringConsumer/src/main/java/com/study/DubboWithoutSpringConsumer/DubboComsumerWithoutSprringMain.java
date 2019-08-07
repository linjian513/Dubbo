package com.study.DubboWithoutSpringConsumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.study.DubboWithoutSpring.DemoService;

public class DubboComsumerWithoutSprringMain {
	
	public static void main(String[] args){
		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx1");
		application.setOwner("linjian");
		 
		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("zookeeper://127.0.0.1:2181");
		/*registry.setUsername("aaa");
		registry.setPassword("bbb");*/
		 
		// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
		 
		// 引用远程服务
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");
		
		
		reference.setFilter("test");
		 
		// 和本地bean一样使用xxxService
		DemoService xxxService = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
		xxxService.sayHello("Lin Jian LJ");
		
		
		
		ReferenceConfigCache cache = ReferenceConfigCache.getCache(); 
		DemoService xxxService2 = cache.get(reference);
		if(xxxService2 != null){
			System.out.println("OK");
		}else{
			System.out.println("null");
		}
	}

}
