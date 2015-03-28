package com.thinkgem.jeesite.common.uuid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;



public class JdbcSequenceIdProvider implements SequenceIdProvider {

	private static final String TABLE_NAME = "sys_sequences";
    private final DataSource dataSource;
 
    public JdbcSequenceIdProvider(DataSource dataSource) {
        this.dataSource = dataSource;
 
        confirmTableExists();
    }
 
    @Override
    public SequenceId create(String name) {
        return new SequenceId(this, name, 1);
    }
 
    @Override
    public SequenceId create(String name, long begin) {
        return new SequenceId(this, name, begin);
    }
 
    private void confirmTableExists() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
 
            ResultSet rs = conn.getMetaData().getTables(null, null, TABLE_NAME, null);
            boolean found = rs.next();
            rs.close();
            
            if (!found) {
                Statement stmt = conn.createStatement();
                String sql = "create table " + TABLE_NAME + " (sequence_name varchar(50) not null, next_value long not null, primary key(sequence_name))";
                stmt.execute(sql);
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn);
        }
    }
 
    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
 
    @Override
    public long load(String name) {
        long value = SequenceId.NOT_FOUND;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            String sql = "select next_value from " + TABLE_NAME + " where sequence_name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                value = rs.getLong(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn);
        }
        return value;
    }
 
    @Override
    public void store(String name, long value) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            String sql = "update " + TABLE_NAME + " set next_value=? where sequence_name=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, value);
            ps.setString(2, name);
            int updated = ps.executeUpdate();
            ps.close();
 
            if (updated == 0) {
                sql = "insert into " + TABLE_NAME + " (sequence_name, next_value) values (?,?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setLong(2, value);
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn);
        }
    }

}
