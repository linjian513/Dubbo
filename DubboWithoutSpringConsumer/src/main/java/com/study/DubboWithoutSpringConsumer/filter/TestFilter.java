package com.study.DubboWithoutSpringConsumer.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.common.Constants;

@Activate(order = 1000, group = {Constants.PROVIDER, Constants.CONSUMER})
public class TestFilter implements Filter{

	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		RpcContext context = RpcContext.getContext();
		if (context.isConsumerSide()) {
			TraceUtil.putTraceInto(context);
		} else if (context.isProviderSide()) {
			TraceUtil.getTraceFrom(context);
		}
		return invoker.invoke(invocation);

	}

}
