package com.bjym.mobiledata.db.utils;

public class DBConstants {
	public static final String logSend_Type_Submitfail = "submitfail";
	public static final String logSend_Type_Error = "error";
	public static final String logSend_Type_unknown = "unknown";

	// ////////////////////logsend_unknown///////////////////////////////////
	public static final String ecode_areaunknown_unknown = "1";
	public static final String ecodedesc_areaunknown_unknown = "δ֪����";

	// ////////////////////logsend_submitfail///////////////////////////////////
	public static final String ecode_senddataex_submitfail = "2";
	public static final String ecodedesc_senddataex_submitfail = "���������쳣";

	public static final String ecode_receivedataex_submitfail = "3";
	public static final String ecodedesc_receivedataex_submitfail = "���������쳣";

	public static final String ecode_gateway_submitfail = "4";
	public static final String ecodedesc_gateway_submitfail = "��ƥ������";

	public static final String ecode_balance_submitfail = "5";
	public static final String ecodedesc_balance_submitfail = "Ԥ�治��";

	public static final String ecode_redis_submitfail = "6";
	public static final String ecodedesc_redis_submitfail = "����ʧ��";
	
	// ////////////////////logsend_error///////////////////////////////////
	public static final String ecode_mobileerror_error = "8";
	public static final String ecodedesc_mobileerror_error = "�������";

	public static final String ecode_codeerror_error = "9";
	public static final String ecodedesc_codeerror_error = "�������";

	public static final String ecode_redlist_error = "10";
	public static final String ecodedesc_redlist_error = "������";
}
