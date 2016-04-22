package com.bjym.mobiledata.taskdispatcher;


import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.bjym.mobiledata.Time.OnTime;
import com.bjym.mobiledata.redis.impl.LogicJedisPools;
import com.bjym.mobiledata.redis.impl.RedisImpl;
import com.bjym.mobiledata.redis.interfaces.RedisInterface;
import com.bjym.mobiledata.redis.utils.RedisConfigKeyConstants;
import com.bjym.mobiledata.utils.LoggerUtil;
import com.bjym.mobiledata.utils.PropertiesUtil;

public class AcquireTask implements Runnable {
	static{
		new OnTime("DTTaskDisp002", 60);
	}
	public static Integer allcount=0;
	//public static Integer success;
	//public static Integer fail;
	//public static Integer waitdisp;
	private RedisInterface redisInterface = new RedisImpl(LogicJedisPools
			.getInstance());
	private ThreadPoolExecutor threadPool;

	public AcquireTask(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}

	@Override
	public void run() {
		try {
			int taskNum = PropertiesUtil.get("AcquireTaskNumber", 10);

			List<byte[]> taskList = redisInterface.getByteFromList(
					RedisConfigKeyConstants.logic_redis_listname_submit,
					taskNum);

			allcount+=taskList.size();
			if (taskList == null) {
//				LoggerUtil.info("û�л�ȡ������..");
				return;
			}
			
			LoggerUtil.info("��"
					+ RedisConfigKeyConstants.logic_redis_listname_submit
					+ "�����л�ȡ" + taskList.size() + "������");

			for (int i = 0; i < taskList.size(); i++) {
				byte[] task = taskList.get(i);

				// ���￼�� �Ƿ��ô�����ֵ�� future ??
				threadPool.execute(new ThreadPoolTask(task));
			}
		} catch (Exception e) {
			LoggerUtil.error("AcquireTask.run()�쳣:" + e.getMessage());
		}
	}
}
