package com.sciul.cloud_configurator.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * Created by sumeetrohatgi on 1/22/15.
 */
@Controller
public class WebController {

  private static Logger logger = LoggerFactory.getLogger(WebController.class);

  @RequestMapping(value = "/", method = RequestMethod.GET)
  String greeting(@RequestParam(value="name", defaultValue = "World") String name, Model model) {
    model.addAttribute("name", name);
    return "index";
  }

}
