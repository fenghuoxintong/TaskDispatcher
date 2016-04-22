package com.bjym.mobiledata.redis.impl;

import redis.clients.jedis.Jedis;

public interface JedisPools {
	public void load();

	public Jedis getJedis();

	public void close(Jedis jedis);

	public void destroy();
}
