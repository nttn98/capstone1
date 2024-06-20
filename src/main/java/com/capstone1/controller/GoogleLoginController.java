package com.capstone1.controller;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.GooglePojo;
import com.capstone1.model.User;
import com.capstone1.model.User.LoginType;
import com.capstone1.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleLoginController {
    @Value("${google.link.get.token}")
    private String GOOGLE_LINK_GET_TOKEN;
    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.redirect.uri}")
    private String GOOGLE_REDIRECT_URI;
    @Value("${google.grant.type}")
    private String GOOGLE_GRANT_TYPE;
    @Value("${google.link.get.user.info}")
    private String GOOGLE_LINK_GET_USER_INFO;
    @Value("${google.scope}")
    private String GOOGLE_SCOPE;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @GetMapping("/login-google")
    public String loginGoogle(@RequestParam(name = "code", required = false) String code, Model model,
            HttpSession session, HttpServletRequest request,
            @RequestParam(name = "error", required = false) String error) throws IOException {

        if ("access_denied".equals(error) || code == null || code.isEmpty()) {
            return userController.getLoginUser(model, " ", " ", session);
        }

        String accessToken = getToken(code);
        GooglePojo googlePojo = getUserInfo(accessToken);

        User u = userService.findByGgID(googlePojo.getId());
        String[] fullname = googlePojo.getEmail().split("@");
        if (u == null) {
            User user = new User();
            user.setGgID(googlePojo.getId());
            user.setEmail(googlePojo.getEmail());
            user.setUsername(fullname[0]);
            user.setType(LoginType.EMAIL);
            user.setStatus(1);
            userService.saveUser(user);
        }
        model.addAttribute("type", "EMAIL");
        model.addAttribute("ggId", googlePojo.getId());
        return userController.getLoginUser(model, fullname[0], " ", session);
    }

    private String getToken(String code) throws ClientProtocolException, IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", GOOGLE_CLIENT_ID)
                        .add("client_secret", GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    private GooglePojo getUserInfo(String accessToken)
            throws ClientProtocolException, IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken + "&scope=" +
                GOOGLE_SCOPE;

        String response = Request.Get(link).execute().returnContent().asString();
        return new Gson().fromJson(response, GooglePojo.class);
    }
}
