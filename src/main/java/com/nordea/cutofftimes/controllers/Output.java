package com.nordea.cutofftimes.controllers;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Output {
    @Schema(description = "The cut-off time in ISO format. See : https://en.wikipedia.org/wiki/ISO_8601")
    private String cutOffTime;
}
