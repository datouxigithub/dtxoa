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

/**
 *
 * @author datouxi
 */
public class JumpServlet extends HttpServlet {
    
    public static final String PAGEFROM = "page-where-from";
    public static final String PAGETO = "page-where-to";
    public static final String MESSAGE = "message-for-show";
    public static final String MESSAGESHOWTIME = "waiting-time";
    private static final int WAITINGTIME=5;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String jumpStr="<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"><title>跳转</title><script language=\"JavaScript\" type=\"text/javascript\">function delayURL(url) {var delay =document.getElementById('time').innerHTML;if(delay > 0) {delay--;document.getElementById('time').innerHTML = delay; } else {window.top.location.href = url; }t = setTimeout(\"delayURL('\" + url + \"')\", 1000);}</script><p>%s</p><span id='time' style='background: #00BFFF'>%d</span>秒钟后自动跳转，如果不跳转，请点击下面的链接<a href='%s'>跳转地址</a><script type='text/javascript'>delayURL('%s');</script>";
        int time=WAITINGTIME;
        if(request.getAttribute(MESSAGESHOWTIME)!=null)
            time=(int)request.getAttribute(MESSAGESHOWTIME);
        String toUrl=request.getAttribute(PAGETO)!=null ? (String)request.getAttribute(PAGETO):SessionRBACController.getDefaultPage(request);
        jumpStr=String.format(jumpStr, request.getAttribute(MESSAGE),time,toUrl,toUrl);
        response.getWriter().write(jumpStr);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
