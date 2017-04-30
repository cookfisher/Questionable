/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.questionable.controllers;

import com.questionable.models.User;
import com.questionable.utility.UserDB;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author viseshprasad
 */
public class UserController extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "";
        HttpSession session = request.getSession();
        // get current action
        String action = request.getParameter("action");
        User user = (User) session.getAttribute("theUser");
        if (action == null) {
            url = "/home.jsp";    // the "main" page
        } else if (action.equalsIgnoreCase("start-login")) {
            url = "/login.jsp";
        } else if (action.equalsIgnoreCase("start-signup")) {
            url = "/signup.jsp";
        } else if (action.equalsIgnoreCase("start-contact")) {
            url = "/contact.jsp";
        } else if (action.equalsIgnoreCase("login")) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            
            
            User loginUser = UserDB.getUser(email);
            if (loginUser != null) {
                String pass = loginUser.getPassword();

                if (pass.equals(password)) {
                    User userBean = new User(loginUser.getId(),
                            loginUser.getUser_name(),
                            loginUser.getPassword(),
                            loginUser.getEmail(),
                            loginUser.getType(),
                            loginUser.getReg_date());
                    session.setAttribute("theUser", userBean);

                    url = "/home.jsp";
                } else {
                    request.setAttribute("msg", "Invalid password");
                    url = "/login.jsp";
                }

            } else {
                request.setAttribute("msg", "Not a valid user");
                url = "/login.jsp";
            }
        } else if (action.equalsIgnoreCase("create")) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String type = "user";
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm_password");
            if (username != null && email != null && password != null && confirmPassword != null && password.equals(confirmPassword)) {
                User userBean = new User();
                userBean.setUser_name(username);
                userBean.setEmail(email);
                userBean.setPassword(password);
                userBean.setType("user");

                Date currDate = new Date();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String reg_date = df.format(currDate);

                userBean.setReg_date(reg_date);

                UserDB.addUser(userBean);
                session.setAttribute("theUser", userBean);
                url = "/home.jsp";
            } else {
                request.setAttribute("msg", "Cannot create the account");
                url = "/signup.jsp";
            }
        } else if (action.equalsIgnoreCase("about")) {
                url = "/about.jsp";
        } else if (action.equalsIgnoreCase("logout")) {
            if (user != null) {
                session.invalidate();
                url = "/home.jsp";
            } else {
                url = "/home.jsp";
            }
        } else if (action.equalsIgnoreCase("contact")) {
            
                request.setAttribute("contacted", "true");
                url = "/home.jsp";
        } 
        // forward request and response objects to specified URL
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
