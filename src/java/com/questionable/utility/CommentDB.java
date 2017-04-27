/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.questionable.utility;

import com.questionable.models.Category;
import com.questionable.models.Comment;
import com.questionable.models.Post;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author viseshprasad
 */
public class CommentDB {
    
        public static int addComment(Comment comment) throws IOException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query
                = "INSERT INTO comment (id, content, user_id, post_id, created_date, modified_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, comment.getId());
            ps.setString(2, comment.getContent());
            ps.setInt(3, comment.getUser().getId());
            ps.setInt(4, comment.getPost().getId());
            ps.setString(5, comment.getCreated_date());
            ps.setString(6, comment.getModified_date());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
        }
    }

    public static ArrayList<Comment> getComments() throws IOException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM comment";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            ArrayList<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setContent(rs.getString("content"));
                comment.setUser(UserDB.getUser(rs.getInt("user_id")));
                comment.setPost(PostDB.getPost(rs.getInt("post_id")));
                comment.setCreated_date(rs.getString("created_date"));
                comment.setModified_date(rs.getString("modified_date"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

        public static ArrayList<Comment> getCommentsByPost(int post_id) throws IOException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM comment "
                + "WHERE post_id = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, post_id);
            rs = ps.executeQuery();
            ArrayList<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setContent(rs.getString("content"));
                comment.setUser(UserDB.getUser(rs.getInt("user_id")));
                comment.setPost(PostDB.getPost(rs.getInt("post_id")));
                comment.setCreated_date(rs.getString("created_date"));
                comment.setModified_date(rs.getString("modified_date"));
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

    }


}
