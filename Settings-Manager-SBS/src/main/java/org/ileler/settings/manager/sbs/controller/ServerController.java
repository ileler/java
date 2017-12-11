package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.model.Server;
import org.ileler.settings.manager.sbs.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(String env, @RequestBody Server server, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : serverService.add(env, server);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj del(String env, String id, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : serverService.del(env, id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public RespObj mod(String env, @RequestBody Server server, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : serverService.mod(env, server);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj get(String env, HttpSession session) {
        Object user = session.getAttribute("user");
        return serverService.get(env, !StringUtils.isEmpty(user));
    }

    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    @ResponseBody
    public RespObj valid(String env, String id) {
        return serverService.valid(env, id);
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    @ResponseBody
    public RespObj services(String env, String id) {
        return serverService.services(env, id);
    }

    @RequestMapping(value = "/kill", method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj kill(String env, String id, Long pid) {
        return serverService.kill(env, id, pid);
    }

    @RequestMapping(value = "/login-logs", method = RequestMethod.GET)
    @ResponseBody
    public RespObj loginLogs(String env, String id) {
        return serverService.loginLogs(env, id);
    }

    @RequestMapping(value = "/oper-logs", method = RequestMethod.GET)
    @ResponseBody
    public RespObj operLogs(String env, String id) {
        return serverService.operLogs(env, id);
    }

}