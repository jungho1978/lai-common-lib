package com.lge.lai.common.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lge.lai.common.db.dto.Provider;

public class ProviderDAO extends BaseDAO implements DaoCallback {
    static Logger LOGGER = LogManager.getLogger(ProviderDAO.class.getName());

    private static final String DB = "LGAppIF";
    private static final String TABLE = DB + ".provider";
    
    public enum Where {
        PACKAGE_ONLY,
        PACKAGE_AND_VERSION,
    };

    private static final String SQL_FIND_BY_ID = "SELECT * FROM " + TABLE + " WHERE _id = ?";
    
    private static final String SQL_UPDATE_MANIFEST_PATH = "UPDATE " + TABLE + " SET _manifest_path = ? WHERE _id = ?";

    private static final String SQL_INSERT = "INSERT INTO "
            + TABLE
            + " "
            + "(_version, _type, _desc, _pkg_name, _cls_name, _table_name, _read_permission, _write_permission, _authorities, _primary, _updated_by, _ctime) "
            + "VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    public final static Map<Where, String> QUERY_TABLE = new HashMap<>();
    static {
        QUERY_TABLE.put(Where.PACKAGE_ONLY, "SELECT * FROM " + TABLE + " WHERE _pkg_name = ?");
        QUERY_TABLE.put(Where.PACKAGE_AND_VERSION, "SELECT * FROM " + TABLE + " WHERE _pkg_name = ? AND _version = ?");
    }

    private static final String SQL_DELETE = "DELETE FROM " + TABLE + " WHERE _id = ?";

    public ProviderDAO(DAOFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public Object find(long id) throws DAOException {
        return find(this, SQL_FIND_BY_ID, id).get(0);
    }

    @Override
    public List<Object> list() throws DAOException {
        throw new DAOException("This API is not available in ProviderDAO");
    }
    
    public List<Object> list(Where where, Object... values) throws DAOException {
        String sql = QUERY_TABLE.get(where);
        if (sql == null) {
            LOGGER.error("Unknown where: " +  where);
            return null;
        }
        return find(this, sql, values);
    }

    @Override
    public long create(Object obj) throws DAOException {
        if (obj instanceof Provider) {
            Provider provider = (Provider)obj;
            Object[] values = { provider.versionName, provider.type, provider.desc,
                    provider.packageName, provider.className, provider.tableName,
                    provider.readPermission, provider.writePermission, provider.authorities,
                    provider.primaryKey, provider.updatedBy, DAOUtil.toSqlTimestamp() };
            return create(SQL_INSERT, values);
        } else {
            LOGGER.error("instance is invalid: " + obj.getClass().getName());
            throw new DAOException("instance is invalid: " + obj.getClass().getName());
        }
    }
    
    public int update(long id, String manifestPath) throws DAOException {
        Object[] values = { manifestPath, id };
        return update(SQL_UPDATE_MANIFEST_PATH, values);
    }

    @Override
    public void delete(long id) throws DAOException {
        delete(SQL_DELETE, id);
    }

    @Override
    public Object convertToDTO(ResultSet rs) throws SQLException {
        long id = rs.getLong("_id");
        String version = rs.getString("_version");
        String type = rs.getString("_type");
        String desc = rs.getString("_desc");
        String packageName = rs.getString("_pkg_name");
        String className = rs.getString("_cls_name");
        String tableName = rs.getString("_table_name");
        String readPermission = rs.getString("_read_permission");
        String writePermission = rs.getString("_write_permission");
        String authorities = rs.getString("_authorities");
        String primaryKey = rs.getString("_primary");
        String updatedBy = rs.getString("_updated_by");
        return new Provider(version, type, desc, packageName, className, tableName, readPermission,
                writePermission, authorities, primaryKey, updatedBy, id);
    }
}
