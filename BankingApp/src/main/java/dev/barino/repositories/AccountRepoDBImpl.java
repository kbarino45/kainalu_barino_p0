package dev.barino.repositories;

import dev.barino.models.Account;
import dev.barino.util.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepoDBImpl implements AccountRepo {

    Connection connection = JDBCConnection.getConnection();

    @Override
    public Account addAccount(Account a) {
        //Values: id, pw, first, last, balance
        String sql = "INSERT INTO accounts VALUES (default,?,?,?,?) RETURNING *";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,a.getPw());
            ps.setString(2,a.getFirstName());
            ps.setString(3,a.getLastName());
            ps.setDouble(4,a.getBalance());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return buildAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } //end try

        return null;

    }

    @Override
    public Account getAccount(int id) {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return buildAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } //end try

        return null;

    }

    @Override
    public Account updateAccount(Account change) {

        String sql = "UPDATE accounts SET pw=?, first_name=?, last_name=?, balance=? WHERE account_id=? RETURNING *";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,change.getPw());
            ps.setString(2,change.getFirstName());
            ps.setString(3,change.getLastName());
            ps.setDouble(4,change.getBalance());
            ps.setInt(5, change.getAccountID());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return buildAccount(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteAccount(int id) {
        String sql = "DELETE FROM accounts WHERE account_id=?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Helper Method
    private Account buildAccount(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setAccountID(rs.getInt("account_id"));
        a.setPw(rs.getString("pw"));
        a.setFirstName(rs.getString("first_name"));
        a.setLastName(rs.getString("last_name"));
        a.setBalance(rs.getDouble("balance"));
        return a;
    }

}
