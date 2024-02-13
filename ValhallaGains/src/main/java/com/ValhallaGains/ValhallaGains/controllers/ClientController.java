package com.ValhallaGains.ValhallaGains.controllers;

import com.ValhallaGains.ValhallaGains.dtos.ClientDTO;
import com.ValhallaGains.ValhallaGains.dtos.NewClientDTO;
import com.ValhallaGains.ValhallaGains.dtos.TrainingType;
import com.ValhallaGains.ValhallaGains.models.Client;
import com.ValhallaGains.ValhallaGains.models.Direction;
import com.ValhallaGains.ValhallaGains.models.Trainingtype;
import com.ValhallaGains.ValhallaGains.repositories.ClientRepository;
import com.ValhallaGains.ValhallaGains.repositories.DirectionRepository;
import com.ValhallaGains.ValhallaGains.services.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DirectionRepository directionRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/client")
    @Transactional
    public ResponseEntity<Object> createClient(@RequestBody NewClientDTO newClientDTO){
        if (newClientDTO.name() == null) {
            return new ResponseEntity<>("You must enter a name.", HttpStatus.BAD_REQUEST);
        }
        if (newClientDTO.email() == null) {
            return new ResponseEntity<>("You must enter an email.",HttpStatus.BAD_REQUEST);
        }
        if (newClientDTO.password() == null) {
            return new ResponseEntity<>("You must enter an password.",HttpStatus.BAD_REQUEST);
        }
        if (newClientDTO.lastName() == null) {
            return new ResponseEntity<>("You must enter an last name ",HttpStatus.BAD_REQUEST);
        }

        if (clientRepository.existsByEmail(newClientDTO.email())) {
            return new ResponseEntity<>("The email is already in use.",HttpStatus.BAD_REQUEST);

        }

        //CREO CLIENTE Y LE ASIGNO LOS DATOS
        Client newClient = new Client();
        newClient.setName(newClientDTO.name());
        newClient.setEmail(newClientDTO.email());
        newClient.setPassword(passwordEncoder.encode(newClientDTO.password()));
        newClient.setLastName(newClientDTO.lastName());
//        Direction direction = new Direction();
//        direction.setStreet(newClientDTO.street());
//        direction.setPostalCode(newClientDTO.postalCode());
//        direction.setNeighborhood(newClientDTO.neighborhood());
//        direction.setCounty(newClientDTO.county());
//        direction.setHouseNumber(newClientDTO.houseNumber());
//        direction.setCity(newClientDTO.city());
//        direction.setClient(newClient);
        //ASIGNO CLIENTE A DIRECCION

        String accessCode;
        do {
            accessCode = valueOf(getRandomNumber(00000000,99999999));
        }while (clientRepository.existsByaccessCode(accessCode));
        newClient.setAccessCode(accessCode);
/*
        newClient.setDirections(direction);
*/
        clientRepository.save(newClient);
/*
        directionRepository.save(direction);
*/

        emailService.sendWelcome(newClient.getEmail(), accessCode);

        //GUARDO CLIENTE CON DATOS CARGADOS EN LA BASE DE DATOS
        return new ResponseEntity<>("client created", HttpStatus.CREATED);

    }
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping("/client")
    public List<ClientDTO> getCLients( ){
        return clientRepository
                .findAll()
                .stream()
                .map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
    @GetMapping("/client/{email}")
    public ClientDTO getClientByEmail(@PathVariable String email){
        return new ClientDTO(clientRepository.findByEmail(email));
    }
    @GetMapping("/client/count")
    public ResponseEntity<Long> getClientCount() {
        Long clientCount = clientRepository.count();
        return new ResponseEntity<>(clientCount, HttpStatus.OK);
    }

    @PatchMapping("/client")
    public ResponseEntity<Object> modifyClient(@RequestBody NewClientDTO newClientDTO, Authentication authentication){
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>("You are not authenticated, you do not have permission to modify the data.",HttpStatus.BAD_REQUEST);
        }
        Client clientToModify = clientRepository.findByEmail(authentication.getName());
        if (newClientDTO == null) {
            return new ResponseEntity<>("NO DATA PROVIDED FOR MODIFICATION", HttpStatus.BAD_REQUEST);
        }
        if (newClientDTO.name() != null) {
            clientToModify.setName(newClientDTO.name());
        }
        if (newClientDTO.lastName() != null) {
            clientToModify.setLastName(newClientDTO.lastName());

        }
        if (newClientDTO.email() != null) {
            clientToModify.setEmail(newClientDTO.email());
        }
        if (newClientDTO.city() != null) {
            clientToModify.getDirections().setCity(newClientDTO.city());
        }
        if (newClientDTO.houseNumber() != null) {
            clientToModify.getDirections().setHouseNumber(newClientDTO.houseNumber());
        }
        if (newClientDTO.neighborhood() != null) {
            clientToModify.getDirections().setNeighborhood(newClientDTO.neighborhood());
        }
        if (newClientDTO.street() != null) {
            clientToModify.getDirections().setStreet(newClientDTO.street());

        }
        if (newClientDTO.postalCode() != null) {
            clientToModify.getDirections().setPostalCode(newClientDTO.postalCode());
        }
        if (newClientDTO.password() != null) {
            clientToModify.setPassword(passwordEncoder.encode(newClientDTO.password()));
        }
        clientRepository.save(clientToModify);


        return new ResponseEntity<>("Customer data has been successfully modified."  , HttpStatus.OK);
    }
    @DeleteMapping("/client/delete/{email}")
    public ResponseEntity<Object> deleteClient(@PathVariable String email){
        // Buscar el cliente por el email
        Client clientToDelete = clientRepository.findByEmail(email);

        if (clientToDelete == null) {
            return new ResponseEntity<>("Client does not exist.", HttpStatus.BAD_REQUEST);
        }

        // Obtener la dirección asociada al cliente
        Direction directionToDelete = clientToDelete.getDirections();

        if (directionToDelete != null) {
            // Desvincular la dirección del cliente
            directionToDelete.setClient(null);
            directionRepository.save(directionToDelete); // Guardar los cambios en la dirección
        }

        // Finalmente, eliminar al cliente
        clientRepository.delete(clientToDelete);
        directionRepository.delete(directionToDelete);
        return new ResponseEntity<>( "Client deleted.", HttpStatus.OK);
    }
    @PatchMapping("/client/training")
    public ResponseEntity<Object> trainingChanges(Authentication authentication,
                                                  @RequestParam String trainingType) {

        Client clientToUpdate = clientRepository.findByEmail(authentication.getName());

        if (clientToUpdate == null) {
            return new ResponseEntity<>("Client not found,Please log in or register", HttpStatus.BAD_REQUEST);
        }
        Trainingtype enumValue = Trainingtype.valueOf(trainingType);
        System.out.printf(trainingType);
        System.out.println(enumValue);

        clientToUpdate.setType(enumValue);
        clientRepository.save(clientToUpdate);

        return new ResponseEntity<>("Successfully updated training", HttpStatus.OK);
    }


}