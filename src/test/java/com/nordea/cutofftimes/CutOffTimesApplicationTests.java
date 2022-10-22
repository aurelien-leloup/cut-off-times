package com.nordea.cutofftimes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordea.cutofftimes.controllers.Output;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CutOffTimesApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;



    @Test
    void invalidCurrencyPairFormat_shouldFailWith400() throws Exception {
        mvc.perform(get("/cut-off-time")
                        .param("currencyPair", "EUR-DKK"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void currencyNotInDb_shouldFailWith404() throws Exception {
        mvc.perform(get("/cut-off-time")
                        .param("currencyPair", "EUR/PHP"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertEquals("PHP is not supported", result.getResolvedException().getMessage()));
    }

    @Test
    void EUR_CZK_shouldReturnTodayAt11() throws Exception {
        Output expectedOutput = new Output(LocalDate.now().atStartOfDay().plusHours(11).format(DateTimeFormatter.ISO_DATE_TIME));

        mvc.perform(get("/cut-off-time")
                        .param("currencyPair", "EUR/CZK"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    void CHF_PLN_shouldReturnTodayAt10() throws Exception {
        Output expectedOutput = new Output(LocalDate
                .now()
                .atStartOfDay()
                .plusHours(10)
                .format(DateTimeFormatter.ISO_DATE_TIME)
        );

        mvc.perform(get("/cut-off-time")
                        .param("currencyPair", "CHF/PLN"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    void THB_NZD_shouldReturnTomorrowAt9() throws Exception {
        Output expectedOutput = new Output(LocalDate
                .now()
                .atStartOfDay()
                .plusDays(1)
                .plusHours(9)
                .format(DateTimeFormatter.ISO_DATE_TIME)
        );

        mvc.perform(get("/cut-off-time")
                        .param("currencyPair", "THB/NZD"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

    @Test
    void DKK_RSD_shouldReturnTodayAt1530() throws Exception {
        Output expectedOutput = new Output(LocalDate
                .now()
                .atStartOfDay()
                .plusHours(15)
                .plusMinutes(30)
                .format(DateTimeFormatter.ISO_DATE_TIME)
        );

        mvc.perform(get("/cut-off-time")
                        .param("currencyPair", "DKK/RSD"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedOutput)));
    }

}
