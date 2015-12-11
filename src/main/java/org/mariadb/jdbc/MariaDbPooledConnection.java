package org.mariadb.jdbc;

import javax.sql.*;

import org.threadly.concurrent.event.ListenerHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MariaDbPooledConnection implements PooledConnection {

    MariaDbConnection connection;
    ListenerHelper<ConnectionEventListener> connectionEventListeners;
    ListenerHelper<StatementEventListener> statementEventListeners;

    /**
     * Constructor.
     * @param connection connection to retrieve connection options
     */
    public MariaDbPooledConnection(MariaDbConnection connection) {
        this.connection = connection;
        connection.pooledConnection = this;
        statementEventListeners = ListenerHelper.build(StatementEventListener.class);
        connectionEventListeners = ListenerHelper.build(ConnectionEventListener.class);
    }

    /**
     * Creates and returns a <code>Connection</code> object that is a handle
     * for the physical connection that
     * this <code>PooledConnection</code> object represents.
     * The connection pool manager calls this method when an application has
     * called the method <code>DataSource.getConnection</code> and there are
     * no <code>PooledConnection</code> objects available. See the
     * {@link javax.sql.PooledConnection interface description} for more information.
     *
     * @return a <code>Connection</code> object that is a handle to
     * this <code>PooledConnection</code> object
     * @throws java.sql.SQLException if a database access error occurs
     *                               if the JDBC driver does not support
     *                               this method
     * @since 1.4
     */
    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    /**
     * Closes the physical connection that this <code>PooledConnection</code>
     * object represents.  An application never calls this method directly;
     * it is called by the connection pool module, or manager.
     * <br>
     * See the {@link javax.sql.PooledConnection interface description} for more
     * information.
     *
     * @throws java.sql.SQLException if a database access error occurs
     *                               if the JDBC driver does not support
     *                               this method
     * @since 1.4
     */
    @Override
    public void close() throws SQLException {
        connection.pooledConnection = null;
        connection.close();
    }

    /**
     * Registers the given event failover so that it will be notified
     * when an event occurs on this <code>PooledConnection</code> object.
     *
     * @param listener a component, usually the connection pool manager,
     *                 that has implemented the
     *                 <code>ConnectionEventListener</code> interface and wants to be
     *                 notified when the connection is closed or has an error
     * @see #removeConnectionEventListener
     */
    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        connectionEventListeners.addListener(listener);
    }

    /**
     * Removes the given event failover from the list of components that
     * will be notified when an event occurs on this
     * <code>PooledConnection</code> object.
     *
     * @param listener a component, usually the connection pool manager,
     *                 that has implemented the
     *                 <code>ConnectionEventListener</code> interface and
     *                 been registered with this <code>PooledConnection</code> object as
     *                 a failover
     * @see #addConnectionEventListener
     */
    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        connectionEventListeners.removeListener(listener);
    }

    /**
     * Registers a <code>StatementEventListener</code> with this <code>PooledConnection</code> object.  Components that
     * wish to be notified when  <code>PreparedStatement</code>s created by the
     * connection are closed or are detected to be invalid may use this method
     * to register a <code>StatementEventListener</code> with this <code>PooledConnection</code> object.
     * <br>
     *
     * @param listener an component which implements the <code>StatementEventListener</code>
     *                 interface that is to be registered with this <code>PooledConnection</code> object
     *                 <br>
     * @since 1.6
     */
    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        statementEventListeners.addListener(listener);
    }

    /**
     * Removes the specified <code>StatementEventListener</code> from the list of
     * components that will be notified when the driver detects that a
     * <code>PreparedStatement</code> has been closed or is invalid.
     * <br>
     *
     * @param listener the component which implements the
     *                 <code>StatementEventListener</code> interface that was previously
     *                 registered with this <code>PooledConnection</code> object
     *                 <br>
     * @since 1.6
     */
    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        statementEventListeners.removeListener(listener);
    }

    /**
     * Fire statement close event to listeners.
     * @param st statement
     */
    public void fireStatementClosed(Statement st) {
        if (st instanceof PreparedStatement) {
            StatementEvent event = new StatementEvent(this, (PreparedStatement) st);
            statementEventListeners.call().statementClosed(event);
        }
    }

    /**
     * Fire statement error to listeners.
     * @param st statement
     * @param ex exception
     */
    public void fireStatementErrorOccured(Statement st, SQLException ex) {
        if (st instanceof PreparedStatement) {
            StatementEvent event = new StatementEvent(this, (PreparedStatement) st, ex);
            statementEventListeners.call().statementErrorOccurred(event);
        }
    }

    /**
     * Fire Connection close to listening listeners.
     */
    public void fireConnectionClosed() {
        ConnectionEvent event = new ConnectionEvent(this);
        connectionEventListeners.call().connectionClosed(event);
    }

    /**
     * Fire connection error to listening listerners.
     * @param ex exception
     */
    public void fireConnectionErrorOccured(SQLException ex) {
        ConnectionEvent event = new ConnectionEvent(this, ex);
        connectionEventListeners.call().connectionErrorOccurred(event);
    }
}
