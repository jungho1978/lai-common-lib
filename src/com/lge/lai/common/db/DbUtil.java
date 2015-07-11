package com.lge.lai.common.db;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class DbUtil {
	public static Timestamp getCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		return new Timestamp(now.getTime());
	}
}
