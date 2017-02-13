/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtxoa.servlet;

import dtx.rbac.controller.impl.SessionRBACController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author datouxi
 */
public abstract class CURDServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session=request.getSession(false);
        if(session==null||session.getAttribute(SessionRBACController.RBACNAME)==null){
            request.setAttribute(JumpServlet.MESSAGE, "没有权限进行相应操作，请先登录!");
            request.setAttribute(JumpServlet.PAGETO, SessionRBACController.getLoginPage(request));
            SessionRBACController.jump(request, response);
            return;
        }
        handleRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected abstract void handleRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException;
    
}
