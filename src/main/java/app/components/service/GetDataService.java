package app.components.service;

import app.components.util.ForecastConverter;
import app.components.util.HandmadeException;
import app.components.view.ForecastCityView;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class GetDataService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public ForecastCityView getView(String city) throws HandmadeException {

        RestTemplate restTemplate = new RestTemplate();
        String str_uri = "https://query.yahooapis.com/v1/public/yql?q=";
        str_uri += String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", city);

        restTemplate.setMessageConverters(getMessageConverters());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<Object> response =
                restTemplate.exchange(str_uri, HttpMethod.GET, entity, Object.class, "1");
        Object object = response.getBody();

        JSONObject json = new JSONObject((LinkedHashMap<String, String>) object);
        if(json.getJSONObject("query").get("results") == null){
            throw new HandmadeException("There's no such a city!");
        }
        ForecastCityView forecastCityView = ForecastConverter.jsonToForecastCityView(json);

        jmsTemplate.convertAndSend(forecastCityView);

        return forecastCityView;
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters =
                new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }

}
