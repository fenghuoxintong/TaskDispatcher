package com.bjym.mobiledata.taskdispatcher;

import com.bjym.mobiledata.bean.LogSend;
import com.bjym.mobiledata.bean.PackageRelation;
import com.bjym.mobiledata.bean.PhoneAreaISP;
import com.bjym.mobiledata.bean.UserReg;
import com.bjym.mobiledata.busbean.SubmitTask;
import com.bjym.mobiledata.db.LogSendDaoImp;
import com.bjym.mobiledata.db.PackageRelationDaoImp;
import com.bjym.mobiledata.db.PhoneDaoimp;
import com.bjym.mobiledata.db.RedListDaoImp;
import com.bjym.mobiledata.db.utils.DBConstants;
import com.bjym.mobiledata.redis.impl.GatewayJedisPools;
import com.bjym.mobiledata.redis.impl.RedisImpl;
import com.bjym.mobiledata.redis.interfaces.RedisInterface;
import com.bjym.mobiledata.redis.utils.RedisConfigKeyConstants;
import com.bjym.mobiledata.utils.LoggerUtil;
import com.bjym.mobiledata.utils.SerializeUtils;
import com.bjym.mobiledata.utils.ToolUtil;

public class ThreadPoolTask implements Runnable {
	public static Integer fail=0;
	public static Integer success=0;
	private PackageRelationDaoImp packageRelationDaoImp = new PackageRelationDaoImp();
	private LogSendDaoImp logSendDaoImp = new LogSendDaoImp();
	private PhoneDaoimp phoneDaoimp = new PhoneDaoimp();
	private RedListDaoImp redListDaoImp = new RedListDaoImp();

	private RedisInterface gatewayRedisInterface = new RedisImpl(
			GatewayJedisPools.getInstance());

	private byte[] task;

	public ThreadPoolTask(byte[] task) {
		this.task = task;
	}

	@Override
	public void run() {
		try {
			Object obj = SerializeUtils.unserialize(task);
			if (obj instanceof SubmitTask) {
				SubmitTask submitTask = (SubmitTask) obj;
				dispatcherTask(submitTask);
			}
		} catch (Exception e) {
			fail++;
			LoggerUtil.error("ThreadPoolTask.run异常:" + e.getMessage());
		}
	}

	private void dispatcherTask(SubmitTask submitTask) {
		LogSend logSend = null;
		UserReg userReg = null;

		int userId = 0;
		String mobile = null;
		int userType = 0;

		String maskPackageCode = null;
		String maskcodePrice = null;
		String maskcodePackageSize = null;

		try {
			logSend = submitTask.getLogSend();
			logSend.setIspID(-1);
			userReg = submitTask.getUserReg();

			// 根据号码判断省份
			mobile = logSend.getDesMobile();
			userId = userReg.getUSER_ID();
			userType = userReg.getUsertype();

			if (!ToolUtil.isMobileNO(mobile)) {
				fail++;
				LoggerUtil.info("号码错误");
				logSend.setEcode(DBConstants.ecode_mobileerror_error);
				logSend.setEcodeDes(DBConstants.ecodedesc_mobileerror_error);
				logSendDaoImp.addLogsend(logSend,
						DBConstants.logSend_Type_Error);
				return;
			}

			 //红名单逻辑
			if (userType == 1) {
				if (redListDaoImp.find(mobile)) {
					fail++;
					LoggerUtil.info("号码为红名单");
					logSend.setEcode(DBConstants.ecode_redlist_error);
					logSend.setEcodeDes(DBConstants.ecodedesc_redlist_error);
					logSendDaoImp.addLogsend(logSend,
							DBConstants.logSend_Type_Error);
					return;
				}
			}
			

			// 伪码 金额_流量大小_用户类型
			maskPackageCode = logSend.getMaskpackage();

			if (!isTrueMaskcode(maskPackageCode)) {
				fail++;
				LoggerUtil.error("提交流量包代码格式错误.");
				logSend.setEcode(DBConstants.ecode_codeerror_error);
				logSend.setEcodeDes(DBConstants.ecodedesc_codeerror_error);
				logSendDaoImp.addLogsend(logSend,
						DBConstants.logSend_Type_Error);
				return;
			}
			String[] arr = maskPackageCode.split("\\#");
			maskcodePrice = arr[0];
			maskcodePackageSize = arr[1];
		} catch (Exception e) {
			fail++;
			LoggerUtil.error("提交流量包代码格式错误.");
			logSend.setEcode(DBConstants.ecode_codeerror_error);
			logSend.setEcodeDes(DBConstants.ecodedesc_codeerror_error);
			logSendDaoImp.addLogsend(logSend, DBConstants.logSend_Type_Error);
			return;
		}

		try {
			PhoneAreaISP phonAreaISP = phoneDaoimp.find(mobile);
			if (phonAreaISP != null) {
				logSend.setProvice(phonAreaISP.getProvince());
				logSend.setCity(phonAreaISP.getCity());
				logSend.setIspID(phonAreaISP.getIspCode());
			} else {
				fail++;
				LoggerUtil.error(mobile + "号段表中没有匹配");
				logSend.setProvice("未知");
				logSend.setCity("未知");
				logSend.setEcode(DBConstants.ecode_areaunknown_unknown);
				logSend.setEcodeDes(DBConstants.ecodedesc_areaunknown_unknown);
				logSendDaoImp.addLogsend(logSend,
						DBConstants.logSend_Type_unknown);
				return;
			}

			// 这儿userPackage 是伪码
			// 判断伪码是否存在 () 根据路由选择pcode
			// 1. 伪码中的 金额和流量大小
			// 2. toC toB
			// 3. 支持和地区
			// 4. 权值选择 和 利润最大
			// 5. 得到产品后判断产品 库存

			// 省份转省份编码
			String province = logSend.getProvice();
			if (!"未知".equals(province)) {
				province = ToolUtil.provinceToPcode(province);
			}

			// 路由
			PackageRelation packageRelation = packageRelationDaoImp
					.findPcodeBySmart(String.valueOf(userId), userType, logSend
							.getIspID(), maskcodePackageSize, Integer
							.valueOf(maskcodePrice), province);

			if (packageRelation == null) {
				fail++;
				LoggerUtil.error("没有合适的网关匹配");
				logSend.setEcode(DBConstants.ecode_gateway_submitfail);
				logSend.setEcodeDes(DBConstants.ecodedesc_gateway_submitfail);
				logSendDaoImp.addLogsend(logSend,
						DBConstants.logSend_Type_Submitfail);
				return;
			}

			// 预付费用户 比较 即(将)消费金额 与 预存余额
			if (userReg.getStatus() == 0) {
				if (userReg.getTotalBalance() < packageRelation
						.getAssignprice()) {
					fail++;
					LoggerUtil.error("预存金额不足");
					logSend.setEcode(DBConstants.ecode_balance_submitfail);
					logSend
							.setEcodeDes(DBConstants.ecodedesc_balance_submitfail);
					logSendDaoImp.addLogsend(logSend,
							DBConstants.logSend_Type_Submitfail);
					return;
				}
			}

			logSend.setCostprice(packageRelation.getDprice());
			logSend.setPrice(packageRelation.getAssignprice());
			logSend.setMaskpackage(maskPackageCode);
			logSend.setUserPackage(packageRelation.getRealcode());
			logSend.setVcode(packageRelation.getVcode());
			logSend.setProductID(packageRelation.getPcode());
			logSend.setIspID(packageRelation.getIspid());

			String vcode = packageRelation.getVcode();

			String listname = RedisConfigKeyConstants.gateway_redis_listname;

			listname = listname.replaceAll("\\*", vcode);
			submitTask.setLogSend(logSend);

			int count = 0;
			// 提交网关队列， 进行2次重试
			while (count < 2) {
				if (dispatcherTaskToGateway(listname, submitTask)) {
					success++;
					LoggerUtil.info(logSend.getSendID() + "," + mobile + "派发至"
							+ listname + "网关队列");
					return;
				}
				count++;
				sleep(1);
			}

			logSend.setEcode(DBConstants.ecode_redis_submitfail);
			logSend.setEcodeDes(DBConstants.ecodedesc_redis_submitfail);
			// 重试失败 存入
			logSendDaoImp.addLogsend(logSend,
					DBConstants.logSend_Type_Submitfail);
		} catch (Exception e) {
			fail++;
			LoggerUtil.error("dispatcherTask异常:" + e.getMessage());
			logSend.setEcode(DBConstants.ecode_redis_submitfail);
			logSend.setEcodeDes(DBConstants.ecodedesc_redis_submitfail);
			logSendDaoImp.addLogsend(logSend,
					DBConstants.logSend_Type_Submitfail);
			return;
		}
	}

	private void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean dispatcherTaskToGateway(String listname,
			SubmitTask submitTask) {
		byte[] newTask = SerializeUtils.serialize(submitTask);
		if (gatewayRedisInterface.addByteToList(listname, newTask) == 1) {
			return true;
		}
		return false;
	}

	private boolean isTrueMaskcode(String maskcode) {
		if (maskcode == null || "".equals(maskcode)) {
			return false;
		}

		String[] arr = maskcode.split("\\#");
		if (arr.length != 2) {
			return false;
		}

		String a1 = arr[0];
		String a2 = arr[1];
		if ("".equals(a1) || "".equals(a2)) {
			return false;
		}

		return true;
	}
}
