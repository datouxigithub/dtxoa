/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtx.rbac.controller.impl;

import dtx.db.ControllerFactory;
import dtx.rbac.bean.Node;
import dtx.rbac.bean.User;
import dtx.rbac.controller.RBACController;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author datouxi
 */
public class SessionRBACController {
    
    public static final String RBACNAME="rbac";
    public static final String NODEID="node_id";
    private static final String DEFAULTURL="/index";
    private static final String LOGINURL="/login";
    
    public static boolean login(String account,String password,HttpServletRequest request){
        HttpSession session=request.getSession(true);
        RBACController rc=ControllerFactory.getRBACController();
        boolean result=rc.authenticate(account, password);
        if(result){
            rc.updateLoginInfo(new Timestamp(System.currentTimeMillis()), request.getRemoteAddr());
            session.setAttribute(RBACNAME, rc);
        }
        return result;
    }
    
    public static User getLoginInfo(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        return session==null ? null:(session.getAttribute(RBACNAME)==null ? null:((RBACController)session.getAttribute(RBACNAME)).getLoginInfo());
    }
    
    public static boolean isLogin(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null)return false;
        RBACController rc=(RBACController) session.getAttribute(RBACNAME);
        return rc!=null ? rc.isLogin():false;
    }
    
    public static Map getRoleChilds(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null)return null;
        RBACController rbac=(RBACController) session.getAttribute(RBACNAME);
        if(rbac==null)return null;
        if(ControllerFactory.getUserController().isAdmin(rbac.getLoginInfo()))
            return ControllerFactory.getRoleController().getAllChilds(DefaultRoleControllerImpl.ROOTROLEID);
        else
            return rbac.getRoleChilds();
    }
    
    public static boolean accessDecision(Node node,HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null)return false;
        RBACController rc=(RBACController) session.getAttribute(RBACNAME);
        return rc!=null ? rc.accessDecision(node):false;
    }
    
    public static boolean accessDecision(String nodeId,HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null)return false;
        RBACController rc=(RBACController) session.getAttribute(RBACNAME);
        return rc!=null ? rc.accessDecision(nodeId):false;
    }
    
    public static void loginOut(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null)return;
        RBACController rc=(RBACController) session.getAttribute(RBACNAME);
        rc.loginOut();
        session.invalidate();
    }
    
    public static Map getAccessList(HttpServletRequest request){
        HttpSession session=request.getSession(false);
        if(session==null)return null;
        RBACController rc=(RBACController) session.getAttribute(RBACNAME);
        return rc!=null ? rc.getAccessList():null;
    }
    
    public static String getDefaultPage(HttpServletRequest request){
        return getPageAddress(DEFAULTURL, request);
    }
    
    public static String getPageAddress(String url,HttpServletRequest request){
        return request.getContextPath()+url;
    }
    
    public static String getLoginPage(HttpServletRequest request){
        return getPageAddress(LOGINURL, request);
    }
    
    public static void jump(HttpServletRequest request,HttpServletResponse response){
        try {
            request.getRequestDispatcher("/jump").forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(SessionRBACController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessionRBACController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
