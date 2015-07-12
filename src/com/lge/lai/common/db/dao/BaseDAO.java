package com.lge.lai.common.db.dao;

import static com.lge.lai.common.db.dao.DAOUtil.close;
import static com.lge.lai.common.db.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDAO {
	protected DAOFactory daoFactory;
	
	public BaseDAO(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public Object find(DaoCallback callback, String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return callback.covertToDAO(resultSet);
            } else {
                throw new DAOException("Querying failed, no object obtained");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(resultSet, statement, connection);
        }
    }
	
	public long create(String sql, Object[] values) {
		Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, true, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Creating failed, no rows affected");
            }
            
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new DAOException("Creating failed, no generated key obtained");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(generatedKeys, statement, connection);
        }
	}
	
	public void delete(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(null, statement, connection);
        }
    }
	
	public abstract Object find(long id) throws DAOException;
	public abstract long create(Object obj) throws DAOException;
	public abstract void delete(long id) throws DAOException;
}