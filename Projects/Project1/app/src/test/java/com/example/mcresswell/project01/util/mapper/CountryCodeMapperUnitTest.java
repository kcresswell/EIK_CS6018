package com.example.mcresswell.project01.util.mapper;

import org.junit.Test;

import static org.junit.Assert.*;

public class CountryCodeMapperUnitTest {

    @Test
    public void validCountryName() {
        assertTrue(CountryCodeMapper.getMapper().containsKey("Japan"));
        assertEquals(CountryCodeMapper.getCountryCode("Japan"), "JP");
    }

    @Test
    public void invalidCountryName() {
        assertEquals(CountryCodeMapper.getCountryCode("TEST COUNTRY"), "US");

    }

}