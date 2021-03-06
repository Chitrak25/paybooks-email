package com.axis.paybooks.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axis.paybooks.constant.PermissionURLConstants;
import com.axis.paybooks.dto.EmailRequestDto;
import com.axis.paybooks.exception.AwsSesException;
import com.axis.paybooks.service.AwsSesService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/access")
public class EmailController {
	

	@Autowired
    AwsSesService awsSesService;

    @Autowired
    public EmailController(AwsSesService awsSesService) {
        this.awsSesService = awsSesService;
    }

    @PostMapping(value = "/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
    	
    	awsSesService.setSender(emailRequestDto.getFrom());
    	awsSesService.setSubject(emailRequestDto.getSubject());
    	
    	System.out.println(emailRequestDto.getFrom());
    	System.out.println(emailRequestDto.getSubject());
    	System.out.println(emailRequestDto.getBody());
    	System.out.println(emailRequestDto.getEmail());
    	
        try {
            awsSesService.sendEmail(emailRequestDto.getEmail(), emailRequestDto.getBody());
            return ResponseEntity.ok("Successfully Sent Email");
        } catch (AwsSesException e) {
            return ResponseEntity.status(500).body("Error occurred while sending email " + e);
        }
    }
}