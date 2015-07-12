package com.lge.lai.common.db.dao;

import static com.lge.lai.common.db.dao.DAOUtil.close;
import static com.lge.lai.common.db.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lge.lai.common.db.dto.AsbMime;

public class AsbMimeDAO {
    private long asbId;
    private DAOFactory daoFactory;

    private static final String DB = "LGAppIF";
    private static final String ASB_TABLE = DB + ".asb";
    private static final String TABLE = DB + ".asb_mime";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE + " "
            + "(_asb_id, _mime_type) " + "VALUES "
            + "(?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM (SELECT * FROM "
            + ASB_TABLE + ") AS ASB_T INNER JOIN (SELECT * FROM "
            + TABLE + " WHERE _id = ?) AS ASB_MIME_T ON ASB_T._id = ASB_MIME_T._asb_id";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE _id = ?";

    public AsbMimeDAO(long asbId, DAOFactory daoFactory) {
        this.asbId = asbId;
        this.daoFactory = daoFactory;
    }

    public AsbMime find(long id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }

    private AsbMime find(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return map(resultSet);
            } else {
                throw new DAOException("Querying asbMime failed, no object obtained");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(resultSet, statement, connection);
        }
    }    

    public long create(AsbMime asbMime) throws DAOException {
        Object[] values = { asbId, asbMime.mimeType };

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKey = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating asbMime failed, not rows affected");
            }

            generatedKey = statement.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getLong(1);
            } else {
                throw new DAOException("Creating asbMime failed, no generated key obtained");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(generatedKey, statement, connection);
        }
    }

    public void delete(long id) throws DAOException {
        delete(SQL_DELETE_BY_ID, id);
    }

    private void delete(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting asbMime failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(null, statement, connection);
        }
    }    

    private static AsbMime map(ResultSet resultSet) throws SQLException {
        String version = resultSet.getString("_version");
        String type = resultSet.getString("_type");
        String desc = resultSet.getString("_desc");
        String packageName = resultSet.getString("_pkg_name");
        String className = resultSet.getString("_cls_name");
        String actionName = resultSet.getString("_action_name");
        String mimeType = resultSet.getString("_mime_type");
        String updatedBy = resultSet.getString("_updated_by");
        return new AsbMime(version, type, desc, packageName, className, actionName, mimeType, updatedBy);
    }
}