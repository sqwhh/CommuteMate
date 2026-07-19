package project.group1.commutemate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WeatherServiceTest {

    private final WeatherService service = new WeatherService();

    @Test
    void describeMapsKnownWeatherCodes() {
        assertEquals("Clear sky", service.describe(0));
        assertEquals("Snow", service.describe(71));
        assertEquals("Rain", service.describe(61));
    }

    @Test
    void describeReturnsUnknownForUnmappedCode() {
        assertEquals("Unknown", service.describe(999));
    }

    @Test
    void compassConvertsDegreesToDirection() {
        assertEquals("N", service.compass(0));
        assertEquals("E", service.compass(90));
        assertEquals("S", service.compass(180));
        assertEquals("W", service.compass(270));
    }
}
