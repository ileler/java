package org.ileler.demo2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HelloController {

    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    @ResponseBody
    public String get1(String arg1, String arg2, String arg4) {
        return "get response";
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    @ResponseBody
    public String get2(@RequestParam("arg1") String xxxx, @RequestParam() String arg2) {
        return "get response";
    }

    @RequestMapping(value = "/hello3", method = RequestMethod.GET)
    @ResponseBody
    public String get3(Map<String, String> map) {
        return "get response";
    }

    @RequestMapping(value = "/hello11", method = RequestMethod.POST)
    @ResponseBody
    public String post1(@RequestBody Map<String, String> map) {
        return "post response";
    }

    @RequestMapping(value = "/hello12", method = RequestMethod.POST)
    @ResponseBody
    public String post2(@JsonParam("arg1") String arg1, @JsonParam("arg2") String arg2) {
        return "post response";
    }

    @RequestMapping(value = "/hello13", method = RequestMethod.POST)
    @ResponseBody
    public String post(@JsonParam("arg1") String arg1, @JsonParam("arg2") String arg2, @RequestBody Map<String, String> map) {
        return "post response";
    }

}
