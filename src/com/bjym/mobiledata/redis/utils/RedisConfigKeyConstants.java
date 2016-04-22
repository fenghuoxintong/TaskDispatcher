package com.bjym.mobiledata.redis.utils;

public class RedisConfigKeyConstants {
	public static final String logic_redis_max_active = "logic_redis_max_active";
	public static final String logic_redis_max_idle = "logic_redis_max_idle";
	public static final String logic_redis_max_wait = "logic_redis_max_wait";
	public static final String logic_redis_ip = "logic_redis_ip";
	public static final String logic_redis_port = "logic_redis_port";
	public static final String logic_redis_read_time_out = "logic_redis_read_time_out";
	public static final String logic_redis_exception_sleep_time = "logic_redis_exception_sleep_time";

	public static final String gateway_redis_max_active = "gateway_redis_max_active";
	public static final String gateway_redis_max_idle = "gateway_redis_max_idle";
	public static final String gateway_redis_max_wait = "gateway_redis_max_wait";
	public static final String gateway_redis_ip = "gateway_redis_ip";
	public static final String gateway_redis_port = "gateway_redis_port";
	public static final String gateway_redis_read_time_out = "gateway_redis_read_time_out";
	public static final String gateway_redis_exception_sleep_time = "gateway_redis_exception_sleep_time";

	public static final String db_redis_max_active = "db_redis_max_active";
	public static final String db_redis_max_idle = "db_redis_max_idle";
	public static final String db_redis_max_wait = "db_redis_max_wait";
	public static final String db_redis_ip = "db_redis_ip";
	public static final String db_redis_port = "db_redis_port";
	public static final String db_redis_read_time_out = "db_redis_read_time_out";
	public static final String db_redis_exception_sleep_time = "db_redis_exception_sleep_time";
	
	//任务提交队列
	public static final String logic_redis_listname_submit = "list:task:submit";
	//任务提交失败队列
	public static final String logic_redis_listname_failure = "list:task:submitfail";
	
	//网关队列
	public static final String gateway_redis_listname = "list:gateway:*";
	
	//logsend日志队列
	public static final String dblog_redis_listname = "list:dblog:logsend";

	//状态报告队列 更新logsend表
	public static final String report_redis_listname = "list:dblog:report";

	//状态报告队列 需要同步用户
	public static final String synreport_redis_listname = "list:dblog:synreport";

}
