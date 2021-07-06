package com.example.bookstore_backend.utils.sessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {

    public static void setSession(JSONObject data) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession();
            for (Object thisKey : data.keySet()) {
                String key = (String) thisKey;
                Object val = data.get(key);
                session.setAttribute(key, val);
            }
        }
    }

    public static boolean removeSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        return true;
    }

    public static Boolean checkAuthority() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer userIdentity = (Integer) session.getAttribute("userIdentity");
                return userIdentity != null && userIdentity >= 0;
            }
        }
        return false;
    }

    public static JSONObject getAuthority() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            HttpSession session = request.getSession(false);
            if (session != null) {
                JSONObject authorityObject = new JSONObject();
                authorityObject.put("userId", (Integer) session.getAttribute("userId"));
                authorityObject.put("username", (String) session.getAttribute("username"));
                authorityObject.put("userIdentity", (Integer) session.getAttribute("userIdentity"));
                return authorityObject;
            }
        }
        return null;
    }
}
