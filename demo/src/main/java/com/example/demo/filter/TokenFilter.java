package com.example.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.entity.ResponseInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {



    private static final String SECRET_KEY = "synt_secret_key";

    //生成token
    public static String getToken(String userId){

        return Jwts.builder()
                .setSubject(userId) // 设置负载内容，可以是用户ID或其他信息
                .setIssuedAt(new Date()) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24 )) // 设置过期时间，这里设置为24小时后过期
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 使用HS256算法和密钥进行签名
                .compact(); // 生成紧凑的token字符串
    }
    //验证token
    public ResponseInfo checkToken(String token)  {

        //判断“token”是否存在
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }catch (Exception e){
            return ResponseInfo.error(400,"token失效");
        }
            return ResponseInfo.success(null);
    }
    @Override
    public void doFilter(ServletRequest servletRequest,//获取请求头
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String token = request.getHeader("token");

        //白名单过滤
        if (isWhite(request.getRequestURI()))
            filterChain.doFilter(request,response);
        else if(StringUtils.hasLength(token)){
            ResponseInfo res = checkToken(token);

            if(res.getCode()==200){
                filterChain.doFilter(request,response);
            }else {
                unAuthenticated(response);
            }
        }else {//无token
            unAuthenticated(response);
        }

    }
    private void unAuthenticated(HttpServletResponse response) throws IOException {
        ResponseInfo res = ResponseInfo.error(333, "token已经失效");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(res);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }


    private final List<String> whiteList= Arrays.asList("/user/login","/user/register");

    public boolean isWhite(String url){
        for(String s:whiteList){
            if(s.equals(url)){
                return true;
            }
        }
        return false;
    }
}
