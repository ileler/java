package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.Profile;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(String env, @RequestBody Profile profile, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : profileService.add(env, profile);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj del(String env, String id, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : profileService.del(env, id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public RespObj mod(String env, @RequestBody Profile profile, HttpSession session) {
        Object user = session.getAttribute("user");
        return StringUtils.isEmpty(user) ? new RespObj("401", "Unauthorized") : profileService.mod(env, profile);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj get(String env, String sid) {
        return profileService.get(env, sid);
    }

}