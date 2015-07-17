package com.lge.lai.common.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.lge.lai.common.db.dto.ASBUri;

public class ASBUriDAO extends BaseDAO implements DaoCallback {
    private long asbId;

    private static final String DB = "LGAppIF";
    private static final String ASB_TABLE = DB + ".asb";
    private static final String TABLE = DB + ".asb_uri";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE + " "
            + "(_asb_id, _uri, _uri_desc) " + "VALUES " + "(?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM (SELECT * FROM " + ASB_TABLE
            + ") AS ASB_T INNER JOIN (SELECT * FROM " + TABLE
            + " WHERE _id = ?) AS ASB_URI_T ON ASB_T._id = ASB_URI_T._asb_id";

    private static final String SQL_LIST_BY_FK = "SELECT * FROM (SELECT * FROM "
            + ASB_TABLE + ") AS ASB_T INNER JOIN (SELECT * FROM " + TABLE
            + " WHERE _asb_id = ?) AS ASB_URI_T ON ASB_T._id = ASB_URI_T._asb_id";
    
    private static final String SQL_LIST_BY_FK_AND_URI = "SELECT * FROM (SELECT * FROM "
            + ASB_TABLE + ") AS ASB_T INNER JOIN (SELECT * FROM " + TABLE
            + " WHERE _asb_id = ? AND _uri = ?) AS ASB_URI_T ON ASB_T._id = ASB_URI_T._asb_id";    

    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE _id = ?";

    public ASBUriDAO(long asbId, DAOFactory daoFactory) {
        super(daoFactory);
        this.asbId = asbId;
    }

    @Override
    public Object find(long id) throws DAOException {
        return find(this, SQL_FIND_BY_ID, id).get(0);
    }

    @Override
    public List<Object> list() throws DAOException {
        return find(this, SQL_LIST_BY_FK, asbId);
    }
    
    public List<Object> list(String uri) throws DAOException {
        return find(this, SQL_LIST_BY_FK_AND_URI, asbId, uri);
    }

    @Override
    public long create(Object obj) throws DAOException {
        if (obj instanceof ASBUri) {
            ASBUri asbUri = (ASBUri)obj;
            Object[] values = { asbId, asbUri.uri, asbUri.uriDesc };

            return create(SQL_INSERT, values);
        } else {
            throw new DAOException("instance is not valid: " + obj.getClass().getName());
        }
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
        String uri = rs.getString("_uri");
        String uriDesc = rs.getString("_uri_desc");
        String updatedBy = rs.getString("_updated_by");
        return new ASBUri(version, type, desc, packageName, className, actionName, uri, uriDesc,
                updatedBy, id);
    }
}
