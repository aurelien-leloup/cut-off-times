package com.nordea.cutofftimes.controllers;

import com.nordea.cutofftimes.exceptions.InvalidInputException;
import com.nordea.cutofftimes.services.CutOffTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cut-off-time")

public class CutOffController {

    @Autowired
    CutOffTimeService cutOffTimeService;

    @GetMapping
    @ResponseBody
    @Operation(summary = "Get the cut-off time for a pair of currencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Output.class)) }),
            @ApiResponse(responseCode = "400", description = "The currency pair format is wrong",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "One of the currencies was not found in database",
                    content = @Content) })
    public Output getCutOffTime(
            @Parameter(description = "The currency pair, must be 2 ISO Codes separated by /", example = "EUR/DKK")
            @RequestParam String currencyPair) {
        if (currencyPair.length() != 7 || currencyPair.charAt(3) != '/') {
            throw new InvalidInputException("Wrong format for the currency pair. Format should be XXX/XXX");
        }

        List<String> currencies = Arrays.asList(currencyPair.split("/"));
        return new Output(this.cutOffTimeService.getCutOffTime(currencies).format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
