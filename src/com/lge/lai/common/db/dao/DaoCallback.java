package com.lge.lai.common.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DaoCallback {
	public Object covertToDAO(ResultSet rs) throws SQLException;
}
