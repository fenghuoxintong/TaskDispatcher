package com.bjym.mobiledata.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bjym.mobiledata.bean.MonitorBean;
import com.bjym.mobiledata.redis.impl.LogicJedisPools;
import com.bjym.mobiledata.redis.impl.RedisImpl;
import com.bjym.mobiledata.redis.interfaces.RedisInterface;
import com.bjym.mobiledata.taskdispatcher.AcquireTask;
import com.bjym.mobiledata.taskdispatcher.ThreadPoolTask;
import com.bjym.mobiledata.utils.LoggerUtil;
import com.bjym.mobiledata.utils.SerializeUtils;

public class OnTime {
	private RedisInterface redisInterface = new RedisImpl(LogicJedisPools
			.getInstance());
	private String nodecode;
	
	public OnTime(String nodecode,int second) {
		
		this.nodecode=nodecode;
		Timer timer = new Timer();
		timer.schedule(new RefreshAccessTokenTask(), 0, second*1000);
	}
	
	private class RefreshAccessTokenTask extends TimerTask {
		@Override
		public void run() {
			
			List<byte[]> taskList2 = new ArrayList<byte[]>();
			Integer allcount=AcquireTask.allcount;
			Integer success=ThreadPoolTask.success;
			Integer fail=ThreadPoolTask.fail;
			Integer wait=(int) redisInterface.getListLen("list:task:submit2");
			LoggerUtil.info("总数："+allcount+"成功： "+success+"失败："+fail+"代发数"+wait);
			MonitorBean monitor=new MonitorBean("DTTaskDisp002",allcount,success,fail,wait, 0, 0, 0, 0);
			taskList2.add(SerializeUtils.serialize(monitor));
			try{
				redisInterface.addByteToList("monitorbean", taskList2);
			}catch (Exception e) {
				// TODO: handle exception
				LoggerUtil.info("添加队列异常");
			}
			
			//清零
			AcquireTask.allcount=0;
			ThreadPoolTask.success=0;
			ThreadPoolTask.fail=0;
			//ThreadLocalSend.setIsSend(true);
			
		}
	}
}
