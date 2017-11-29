package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.model.Server;
import org.ileler.settings.manager.sbs.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(String env, @RequestBody Server server) {
        return serverService.add(env, server);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj del(String env, String id) {
        return serverService.del(env, id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public RespObj mod(String env, @RequestBody Server server) {
        return serverService.mod(env, server);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj get(String env) {
        return serverService.get(env);
    }

    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    @ResponseBody
    public RespObj get(String env, String id) {
        return serverService.valid(env, id);
    }

}