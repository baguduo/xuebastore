package com.touchfuture.takeout.common;


import com.sun.prism.shader.Solid_TextureRGB_AlphaTest_Loader;
import com.touchfuture.takeout.bean.Admin;
import com.touchfuture.takeout.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ActionUtil {

	public static final String SESSION_USER = "TK_USER";
	public static final String SESSION_USER_LOGOUT = "TK_USER_LOGOUT";// 用户登出
	public static final String SESSION_USER_ACTIVATION_KEY = "TK_USER_ACTIVATION_KEY";
	public static final String SESSION_USER_RESETPASSWORD_KEY = "TK_USER_RESETPASSWORD_KEY";

	public static User getCurrentUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(SESSION_USER);
		return user;
	}

	public static void setCurrentUser(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER, user);
		session.setAttribute(SESSION_USER_LOGOUT, false);
	}
	public static void removeCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_USER);
		session.setAttribute(SESSION_USER_LOGOUT, true);
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//			String name = cookie.getName();
//			if (name.equals(UserAction.COOKIE_USER_SID)
//					|| name.equals(UserAction.COOKIE_USER_PASSWORD)) {
//				cookie.setValue(null);
//				cookie.setMaxAge(0);// 立刻删除Cookie
//				cookie.setPath("/");
//				response.addCookie(cookie);
//			}
//		}
	}
	public static Admin getCurrentAdmin(HttpServletRequest request) {
		Admin admin = (Admin) request.getSession().getAttribute(SESSION_USER);
		return admin;
	}

	public static void setCurrentAdmin(HttpServletRequest request, Admin admin) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER, admin);
		session.setAttribute(SESSION_USER_LOGOUT, false);
	}

	public static void removeCurrentAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_USER);
		session.setAttribute(SESSION_USER_LOGOUT, true);
//		Cookie[] cookies = request.getCookies();
//		for (Cookie cookie : cookies) {
//			String name = cookie.getName();
//			if (name.equals(UserAction.COOKIE_USER_SID)
//					|| name.equals(UserAction.COOKIE_USER_PASSWORD)) {
//				cookie.setValue(null);
//				cookie.setMaxAge(0);// 立刻删除Cookie
//				cookie.setPath("/");
//				response.addCookie(cookie);
//			}
//		}
	}

	public static boolean getLogout(HttpServletRequest request) {
		Object r = request.getSession().getAttribute(SESSION_USER_LOGOUT);
        return r != null && (Boolean) r;
	}

	public static void setActivationKey(HttpServletRequest request, String key) {
		request.getSession().setAttribute(SESSION_USER_ACTIVATION_KEY, key);
	}

	public static String getActivationKey(HttpServletRequest request) {
		String key = (String) request.getSession().getAttribute(
				SESSION_USER_ACTIVATION_KEY);
		return key;
	}

	public static void removeActivationKey(HttpServletRequest request) {
		request.getSession().removeAttribute(SESSION_USER_ACTIVATION_KEY);
	}

	public static void setResetPasswordKey(HttpServletRequest request,
			String key) {
		request.getSession().setAttribute(SESSION_USER_RESETPASSWORD_KEY, key);
	}

	public static String getResetPasswordKey(HttpServletRequest request) {
		String key = (String) request.getSession().getAttribute(
				SESSION_USER_RESETPASSWORD_KEY);
		return key;
	}

	public static void removeResetPasswordKey(HttpServletRequest request) {
		request.getSession().removeAttribute(SESSION_USER_RESETPASSWORD_KEY);
	}

	public static String getSameTypeDate (String originDate) {
		String[] dateElement = originDate.split("-");

		if (dateElement.length != 3) {
			return originDate;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(dateElement[0]);
		if (Integer.valueOf(dateElement[1]) < 10) {
			sb.append("-");
			sb.append(0);
			sb.append(dateElement[1]);
		} else  {
			sb.append("-");
			sb.append(dateElement[1]);
		}

		if (Integer.valueOf(dateElement[2]) < 10) {
			sb.append("-");
			sb.append(0);
			sb.append(dateElement[2]);
		} else {
			sb.append("-");
			sb.append(dateElement[2]);
		}
		return sb.toString();
	}

}
