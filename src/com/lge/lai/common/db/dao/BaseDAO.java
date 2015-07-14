package com.lge.lai.common.db.dao;

import static com.lge.lai.common.db.dao.DAOUtil.close;
import static com.lge.lai.common.db.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Lists;

public abstract class BaseDAO {
    protected DAOFactory daoFactory;

    public BaseDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    protected List<Object> find(DaoCallback callback, String sql, Object... values)
            throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);

            List<Object> objects = Lists.newArrayList();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                objects.add(callback.convertToDTO(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            close(resultSet, statement, connection);
        }
    }

    protected long create(String sql, Object[] values) {
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

    protected void delete(String sql, Object... values) throws DAOException {
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

    public abstract List<Object> list() throws DAOException;

    public abstract long create(Object obj) throws DAOException;

    public abstract void delete(long id) throws DAOException;
}