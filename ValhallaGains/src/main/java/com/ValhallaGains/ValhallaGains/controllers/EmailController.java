package com.ValhallaGains.ValhallaGains.controllers;

import com.ValhallaGains.ValhallaGains.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {
    @Autowired
    EmailService emailService;
    @PostMapping("/emailAdmin")
    public ResponseEntity<Object>mensajeDeAdmin(@RequestParam String correoDestino, @RequestParam String mensaje){
            emailService.sendMessageAdmin(correoDestino, mensaje);
        return new ResponseEntity<>( "The admin has sent a message to."+ correoDestino, HttpStatus.OK);
    }
}
