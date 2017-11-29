package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.service.EnvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/env")
public class EnvController {

    @Autowired
    private EnvService envService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(@RequestBody Env env) {
        return envService.add(env);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj del(String name) {
        return envService.del(name);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public RespObj mod(@RequestBody Env env) {
        return envService.mod(env);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj get() {
        return envService.get();
    }

    @RequestMapping(value = "/reset", method = RequestMethod.PUT)
    @ResponseBody
    public RespObj reset(String name) {
        return envService.reset(name);
    }

}