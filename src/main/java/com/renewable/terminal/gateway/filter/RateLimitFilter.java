package com.renewable.terminal.gateway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.renewable.terminal.gateway.exception.RateLimitException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/** 限流拦截器
 * @Description： 使用令牌桶机制
 * @Author: jarry
 */
public class RateLimitFilter extends ZuulFilter {

	// Guava已经包含了令牌桶实现了。这里的500表示每秒放入桶内的令牌数
	private static final RateLimiter RATE_LIMITER = RateLimiter.create(500);

	@Override
	public String filterType() {
		return PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// 限流过滤应当在所有过滤器前面，所以要比小的预设值还要小1
		return SERVLET_DETECTION_FILTER_ORDER - 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		// 这里可以如鉴权过滤器那样，操作response。也可以抛出一个自定义的异常
		if (!RATE_LIMITER.tryAcquire()){	// 默认每次获取一个令牌
			throw new RateLimitException();
		}
		return null;
	}
}
