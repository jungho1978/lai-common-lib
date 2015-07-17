package com.lge.lai.common.db.dao;

import static com.lge.lai.common.db.dao.DAOUtil.close;
import static com.lge.lai.common.db.dao.DAOUtil.prepareStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public abstract class BaseDAO {
    static Logger LOGGER = LogManager.getLogger(BaseDAO.class.getName());
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
            LOGGER.info(statement.toString());

            List<Object> objects = Lists.newArrayList();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                objects.add(callback.convertToDTO(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DAOException(e);
        } finally {
            close(resultSet, statement, connection);
        }
    }

    protected long create(String sql, Object[] values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, true, values);
            LOGGER.info(statement.toString());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating failed, no rows affected");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new DAOException("Updating failed, no generated key obtained");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                LOGGER.error("Duplicated entry exist: e" + e);
                return -1;
            } else {
                LOGGER.error("Unknown SQL exception: " + e);
                throw new DAOException(e);
            }
        } finally {
            close(generatedKeys, statement, connection);
        }
    }
    
    protected int update(String sql, Object[] values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);
            LOGGER.info(statement.toString());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Updating failed, no rows affected");
            }
            return affectedRows;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DAOException(e);
        } finally {
            close(null, statement, connection);
        }
    }

    protected void delete(String sql, Object... values) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = prepareStatement(connection, sql, false, values);
            LOGGER.info(statement.toString());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Deleting failed, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
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