package com.lge.lai.common.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lge.lai.common.db.dto.ASB;

public class ASBDAO extends BaseDAO implements DaoCallback {
    private static final String DB = "LGAppIF";
    private static final String TABLE = DB + ".asb";

    public enum Where {
        PACKAGE_ONLY,
        PACKAGE_AND_VERSION,
        PACKAGE_AND_ACTION
    };

    private static final String SQL_FIND_BY_ID = "SELECT * FROM " + TABLE + " WHERE _id = ?";
    
    private static final String SQL_UPDATE_MANIFEST_PATH = "UPDATE " + TABLE + " SET _manifest_path = ? WHERE _id = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE + " "
            + "(_version, _type, _desc, _pkg_name, _cls_name, _action_name, _updated_by, _ctime) "
            + "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE _id = ?";
    
    public final static Map<Where, String> QUERY_TABLE = new HashMap<>();
    static {
        QUERY_TABLE.put(Where.PACKAGE_ONLY, "SELECT * FROM " + TABLE + " WHERE _pkg_name = ?");
        QUERY_TABLE.put(Where.PACKAGE_AND_VERSION, "SELECT * FROM " + TABLE + " WHERE _pkg_name = ? AND _version = ?");
        QUERY_TABLE.put(Where.PACKAGE_AND_ACTION, "SELECT * FROM " + TABLE + " WHERE _pkg_name = ? AND _action_name = ?");
    }

    public ASBDAO(DAOFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public Object find(long id) throws DAOException {
        return find(this, SQL_FIND_BY_ID, id).get(0);
    }

    @Override
    public List<Object> list() throws DAOException {
        throw new DAOException("This API is not availabe in AsbDAO");
    }

    public List<Object> list(Where where, Object... values) {
        String query = QUERY_TABLE.get(where);
        if (query == null) {
            return null;
        }
        return find(this, query, values);
    }

    @Override
    public long create(Object obj) throws DAOException {
        if (obj instanceof ASB) {
            ASB asb = (ASB)obj;
            Object[] values = { asb.versionName, asb.type, asb.desc, asb.packageName,
                    asb.className, asb.actionName, asb.updatedBy, DAOUtil.toSqlTimestamp() };
            return create(SQL_INSERT, values);
        } else {
            throw new DAOException("instance is invalid: " + obj.getClass().getName());
        }
    }
    
    public int update(long id, String manifestPath) throws DAOException {
        Object[] values = { manifestPath, id };
        return update(SQL_UPDATE_MANIFEST_PATH, values);
    }

    @Override
    public void delete(long id) throws DAOException {
        delete(SQL_DELETE_BY_ID, id);
    }

    @Override
    public Object convertToDTO(ResultSet rs) throws SQLException {
        long id = rs.getLong("_id");
        String version = rs.getString("_version");
        String type = rs.getString("_type");
        String desc = rs.getString("_desc");
        String packageName = rs.getString("_pkg_name");
        String className = rs.getString("_cls_name");
        String actionName = rs.getString("_action_name");
        String updatedBy = rs.getString("_updated_by");
        return new ASB(version, type, desc, packageName, className, actionName, updatedBy, id);
    }
}
