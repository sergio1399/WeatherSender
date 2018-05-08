package app.components.service;

import app.components.view.ForecastCityView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GetDataServiceTest {
    @Mock
    private GetDataService getDataService;

    @Test
    public void getData(){

        ForecastCityView view = new ForecastCityView("Boston", " MA", "United States", "48",
                "chill: 48, direction:0, speed:4", "Partly Cloudy", 1021.0, 12.5, 2367105, null);

        org.mockito.Mockito.when(getDataService.getView("Boston")).thenReturn(view);

        assertEquals(getDataService.getView("Boston"), view);
        org.mockito.Mockito.verify(getDataService).getView("Boston");
    }

}