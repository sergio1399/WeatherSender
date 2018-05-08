package app.components.controller;

import java.util.*;

import app.components.service.GetDataService;
import app.components.util.ForecastConverter;
import app.components.view.ForecastCityView;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class ForecastController {

    @Autowired
    private GetDataService getDataService;

    @RequestMapping(path={"/"},method=RequestMethod.GET)
    public String init(Model model) {
        return "index";
    }

    @RequestMapping(path={"/forecast"},method=RequestMethod.GET)
    public String forecast(@RequestParam(value="city", required=true) String city, Model model) {
        ForecastCityView forecastCityView = null;
        try {
            forecastCityView = getDataService.getView(city);
        }
        catch (Exception e){

        }

        model.addAttribute("city", city);
        model.addAttribute("forecast",forecastCityView.toString());
        return "forecast";
    }



}