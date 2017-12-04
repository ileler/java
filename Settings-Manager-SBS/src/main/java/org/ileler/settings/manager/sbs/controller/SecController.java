package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.model.Sec;
import org.ileler.settings.manager.sbs.service.SecService;
import org.ileler.settings.manager.sbs.util.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/sec")
public class SecController {

    @Autowired
    private SecService secService;

    @RequestMapping(value = "pwd", method = RequestMethod.GET)
    @ResponseBody
    public String pwd(String str) {
        return Password.createPassword(str);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public RespObj login(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) return new RespObj("401", "Unauthorized");
        return new RespObj(user);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(@RequestBody Sec sec, HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) new RespObj("11111111", "args invalid");
        return new RespObj(secService.add(sec));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public RespObj login(@RequestBody Sec sec, HttpSession session) {
        String username = sec.getUsername();
        String password = sec.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || !secService.login(sec)) return new RespObj("11111111", "args invalid");
        session.setAttribute("user", username);
        return new RespObj(username);
    }

    @RequestMapping(value = "modify", method = RequestMethod.PUT)
    @ResponseBody
    public RespObj modify(@RequestBody Sec sec, HttpSession session) {
        String username = sec == null ? null : sec.getUsername();
        Object sname = session.getAttribute("user");
        if (username != null && sname != null && username.equals(sname.toString()) && secService.modify(sec)) return new RespObj(sec.getUsername());
        return new RespObj("99999999", "modify failed.");
    }

    @RequestMapping(value = "login", method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj logout(HttpSession session) {
        session.removeAttribute("user");
        return new RespObj(null);
    }

}