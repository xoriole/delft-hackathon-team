package com.github.sofaid.gov.controller;

import com.github.sofaid.gov.data.db.Citizen;
import com.github.sofaid.gov.service.CitizenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/citizens")
public class CitizenController {

    @Autowired
    private CitizenService citizenService;

    @GetMapping("")
    @ApiOperation(value = "Lists all registered citizens")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all registered citizens"),
    })
    public List<Citizen> getAllEmployees() {
        return citizenService.getAllCitizens();
    }
}