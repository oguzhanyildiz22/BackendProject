package com.microservice.UserService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.UserService.entity.TerminalData;
import com.microservice.UserService.repository.TerminalDataRepository;

@RestController
@RequestMapping("api/terminal")
public class TerminalController {
    private final TerminalDataRepository terminalDataRepository;

    @Autowired
    public TerminalController(TerminalDataRepository terminalDataRepository) {
        this.terminalDataRepository = terminalDataRepository;
    }

    @GetMapping("/data")
    public List<TerminalData> getAllTerminals() {
        return terminalDataRepository.findAll();
    }
}

