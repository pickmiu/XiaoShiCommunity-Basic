package com.sicnu.community.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sicnu.community.enums.LoginTypeEnum;
import com.sicnu.community.json.BackFrontMessage;
import com.sicnu.community.pojo.User;
import com.sicnu.community.service.login.UserService;
import com.sicnu.community.service.login.UserServiceFactory;
import com.sicnu.community.util.JwtTokenUtil;
import com.sicnu.community.util.ResponseUtil;
import com.sicnu.community.util.UserAuthenticationUtil;
import com.sicnu.community.util.ValidationUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserServiceFactory userServiceFactory;

    @Resource
    private AntPathMatcher antPathMatcher;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        log.info("[op:preHandle] url={}", request.getRequestURI());
        if (passUrl(request.getRequestURI())) {
            // 匿名url
            return true;
        }
        String token = request.getHeader("token");
        if (ValidationUtil.notNullAndEmptyBoolean(token)) {
            try {
                Claims claims = jwtTokenUtil.parseToken(token);
                String loginType = (String) claims.get("loginType");
                String username = (String) claims.get("username");
                String userId = claims.getSubject();
                if (ValidationUtil.notNullAndEmptyBoolean(userId, loginType, username)) {
                    UserService userService = userServiceFactory.getUserService(LoginTypeEnum.DEFAULT.getValue());
                    User user = userService.loadUserByUserId(Integer.parseInt(userId));
                    if (checkUser(user, loginType, username)
                        && jwtTokenUtil.isCreateAfterLastPasswordReset(claims, user.getPasswordChangeTime())) {
                        // token合法
                        // 上下文设置当前用户信息，便于访问
                        UserAuthenticationUtil.setCurrentUser(user);
                        return true;
                    }
                }
            } catch (ExpiredJwtException e) {
                // 捕捉过期异常
                log.info("[op:preHandle] 用户:{} token已过期 过期时间:{}", e.getClaims().getSubject(),
                    e.getClaims().getExpiration());
            } catch (SignatureException e) {
                // token缺少一部分
                log.error("[op:preHandle] catch-exception request={} response={} handler={}", request, response,
                    handler, e);
            } catch (JwtException e) {
                // 捕捉其他jwt异常
                log.error("[op:preHandle] catch-exception request={} response={} handler={}", request, response,
                    handler, e);
            }
        }
        // 发送前端失败json
        ResponseUtil.writeJson(response, new BackFrontMessage(500, "登录失败", null));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        // 退出时清除用户信息
        UserAuthenticationUtil.removeUser();
    }

    private boolean passUrl(String url) {
        return antPathMatcher.match("/anon/*", url);
    }

    private boolean checkUser(User user, String loginType, String username) {
        // 双重检测 判断token自带的用户信息是否正确
        // 原因：token中用的是userId(从1自增) 作为唯一标识 防止散列算法失效了后 攻击者使用伪造token带上别人的userId
        if (LoginTypeEnum.PHONE.getValue().equals(loginType)) {
            if (user.getPhone().equals(username)) {
                return true;
            } else {
                return false;
            }
        } else if (LoginTypeEnum.EMAIL.getValue().equals(loginType)) {
            if (user.getEmail().equals(username)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
