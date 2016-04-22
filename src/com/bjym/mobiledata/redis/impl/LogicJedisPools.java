package com.bjym.mobiledata.redis.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.bjym.mobiledata.redis.utils.RedisConfigKeyConstants;
import com.bjym.mobiledata.redis.utils.RedisConfigUtil;
import com.bjym.mobiledata.utils.LoggerUtil;

public class LogicJedisPools implements JedisPools {
	private static JedisPools instance;
	private JedisPool logic_jedispool;

	public static synchronized JedisPools getInstance() {
		if (instance == null) {
			instance = new LogicJedisPools();
		}
		return instance;
	}

	private LogicJedisPools() {
		load();
	}

	@Override
	public void load() {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(RedisConfigUtil.getLogic(
					RedisConfigKeyConstants.logic_redis_max_active, 100));
			config.setMaxIdle(RedisConfigUtil.getLogic(
					RedisConfigKeyConstants.logic_redis_max_idle, 50));
			config.setMaxWait(RedisConfigUtil.getLogic(
					RedisConfigKeyConstants.logic_redis_max_wait, 1000));

			config.setTestOnBorrow(true);

			logic_jedispool = new JedisPool(config, RedisConfigUtil.getLogic(
					RedisConfigKeyConstants.logic_redis_ip, "127.0.0.1"),
					RedisConfigUtil.getLogic(
							RedisConfigKeyConstants.logic_redis_port, 6379),
					RedisConfigUtil.getLogic(
							RedisConfigKeyConstants.logic_redis_read_time_out,
							10000));

		} catch (Exception e) {
			LoggerUtil.error("初始化redis 连接池异常:" + e.getMessage());
		}
	}

	@Override
	public Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = (Jedis) logic_jedispool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread
						.sleep(RedisConfigUtil
								.getLogic(
										RedisConfigKeyConstants.logic_redis_exception_sleep_time,
										1000));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			load();
		}
		if (jedis != null) {
			return jedis;
		}
		LoggerUtil.error("任务redis,取到空链接");
		return null;
	}

	@Override
	public void close(Jedis jedis) {
		logic_jedispool.returnResource(jedis);
	}

	@Override
	public void destroy() {
		logic_jedispool.destroy();
	}
}
