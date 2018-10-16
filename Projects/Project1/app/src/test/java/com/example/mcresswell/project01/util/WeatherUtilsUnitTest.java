package com.example.mcresswell.project01.util;

import org.junit.Test;

import static com.example.mcresswell.project01.util.WeatherUtils.convertAndFormatKelvinTemp;
import static com.example.mcresswell.project01.util.WeatherUtils.formatCaseCity;
import static com.example.mcresswell.project01.util.WeatherUtils.formatCaseCountryCodeFromCountryName;
import static com.example.mcresswell.project01.util.WeatherUtils.kelvinToFarenheit;
import static org.junit.Assert.*;

public class WeatherUtilsUnitTest {

    @Test
    public void testKelvinToFarenheit() {
        assertEquals(kelvinToFarenheit(273.15), 32.0, 0.0003);
    }

    @Test
    public void testConvertAndFormatKelvinTemp() {
        assertEquals(convertAndFormatKelvinTemp(273.15), "32.0 °F");
    }

    @Test
    public void testFormatCaseCity() {
        assertEquals(formatCaseCity("new   york"), "New York");
        assertEquals(formatCaseCity("SALT LAKE CITY"), "Salt Lake City");
        assertEquals(formatCaseCity("provo"), "Provo");
        assertNull(formatCaseCity(null));

    }

    @Test
    public void testFormatCaseCountryCodeFromCountryName() {
        assertEquals(formatCaseCountryCodeFromCountryName("us"), "US");
        assertEquals(formatCaseCountryCodeFromCountryName("United States"), "US");
        assertEquals(formatCaseCountryCodeFromCountryName("US"), "US");
        assertEquals(formatCaseCountryCodeFromCountryName("INVALID COUNTRY CODE"), "US");
        assertNull(formatCaseCountryCodeFromCountryName(null));


    }
}