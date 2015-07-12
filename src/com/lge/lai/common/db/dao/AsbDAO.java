package com.lge.lai.common.db.dao;

import static com.lge.lai.common.db.dao.DAOUtil.close;
import static com.lge.lai.common.db.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lge.lai.common.db.dto.Asb;

public class AsbDAO {
    private DAOFactory daoFactory;

    private static final String DB = "LGAppIF";
    private static final String TABLE = DB + ".asb";
    
    private static final String SQL_FIND_BY_ID = "SELECT * FROM " + TABLE + " WHERE _id = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE + " "
            + "(_version, _type, _desc, _pkg_name, _cls_name, _action_name, _updated_by, _ctime) " + "VALUES "
            + "(?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE _id = ?";

    public AsbDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public Asb find(long id) throws DAOException {
        return find(SQL_FIND_BY_ID, id);
    }
    
    private Asb find(String sql, Object... values) throws DAOException {
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
                throw new DAOException("Querying asb failed, no object obtained");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(resultSet, statement, connection);
        }
    }
    
    public long create(Asb asb) throws DAOException {
        Object[] values = { asb.version, asb.type, asb.desc, asb.packageName, asb.className, asb.actionName, asb.updatedBy, DAOUtil.toSqlTimestamp() };

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, SQL_INSERT, true, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating asb failed, no rows affected");
            }
            
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new DAOException("Creating asb failed, no generated key obtained");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(generatedKeys, statement, connection);
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
                throw new DAOException("Deleting asb failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(null, statement, connection);
        }
    }
    
    private static Asb map(ResultSet resultSet) throws SQLException {
        String version = resultSet.getString("_version");
        String type = resultSet.getString("_type");
        String desc = resultSet.getString("_desc");
        String packageName = resultSet.getString("_pkg_name");
        String className = resultSet.getString("_cls_name");
        String actionName = resultSet.getString("_action_name");
        String updatedBy = resultSet.getString("_updated_by");
        return new Asb(version, type, desc, packageName, className, actionName, updatedBy);
    }
}
