package org.ileler.settings.manager.sbs.controller;

import org.ileler.settings.manager.sbs.model.Profile;
import org.ileler.settings.manager.sbs.model.RespObj;
import org.ileler.settings.manager.sbs.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public RespObj add(String env, @RequestBody Profile profile) {
        return profileService.add(env, profile);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public RespObj del(String env, String id) {
        return profileService.del(env, id);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public RespObj mod(String env, @RequestBody Profile profile) {
        return profileService.mod(env, profile);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RespObj get(String env, String sid) {
        return profileService.get(env, sid);
    }

}