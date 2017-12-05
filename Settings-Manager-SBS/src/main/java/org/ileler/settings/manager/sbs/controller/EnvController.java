package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.service.EnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/env")
public class EnvController {

    @Autowired
    private EnvService envService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(@RequestBody Env env, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : envService.add(env);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj del(String name, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : envService.del(name);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public RespObj mod(@RequestBody Env env, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : envService.mod(env);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj get() {
        return envService.get();
    }

    @RequestMapping(value = "/reset", method = RequestMethod.PUT)
    @ResponseBody
    public RespObj reset(String name, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : envService.reset(name);
    }

}