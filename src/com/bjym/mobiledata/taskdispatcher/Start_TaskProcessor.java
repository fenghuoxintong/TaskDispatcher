package com.bjym.mobiledata.taskdispatcher;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.bjym.mobiledata.utils.LoggerUtil;
import com.bjym.mobiledata.utils.PropertiesUtil;

public class Start_TaskProcessor {
	
	public static void main(String[] args) {
		
		// 获取配置信息
		// ScheduledSerice
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		System.out.println("开始时间："+sdf.format(new Date()));
		int threadNum = PropertiesUtil.get("ScheduledSericeThreadNum", 1);
		int initialDelaytime = PropertiesUtil.get(
				"ScheduledSericeInitialDelaytime", 2);
		int delaytime = PropertiesUtil.get("ScheduledSericeDelaytime", 1);

		// ThreadPoolExecutor
		int corepoolSize = PropertiesUtil.get("ThreadPoolExecutorCorepoolSize",
				4);
		int maxpoolSize = PropertiesUtil
				.get("ThreadPoolExecutorMaxpoolSize", 5);
		int keepAliveTime = PropertiesUtil.get(
				"ThreadPoolExecutorKeepAliveTime", 10);
		int workQueueSize = PropertiesUtil.get(
				"ThreadPoolExecutorWorkQueueSize", 1);

		// LoggerUtil.info("threadNum:" + threadNum + ", initialDelaytime:"
		// + initialDelaytime + ",delaytime:" + delaytime);

		// 处理同步客户任务的线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corepoolSize,
				maxpoolSize, keepAliveTime, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(workQueueSize),
				new ThreadFactory() {
					final AtomicInteger threadNumber = new AtomicInteger(1);

					public Thread newThread(Runnable r) {
						Thread t = new Thread(Thread.currentThread()
								.getThreadGroup(), r, "ThreadPoolExecutor中线程"
								+ (threadNumber.getAndIncrement()));
						t
								.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
									public void uncaughtException(Thread t,
											Throwable e) {
										LoggerUtil
												.error("ThreadPoolExecutor异常:"
														+ e.getMessage()
														+ "\r\n");
									}
								});
						return t;
					}
				}, new ThreadPoolExecutor.CallerRunsPolicy());

		// 从服务器获取任务的 计划任务线程
		ScheduledExecutorService scheduledService = Executors
				.newScheduledThreadPool(threadNum);

		// 执行计划
		scheduledService.scheduleWithFixedDelay(new AcquireTask(threadPool),
				initialDelaytime, delaytime, TimeUnit.MILLISECONDS);
	}
}