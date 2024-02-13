package com.ValhallaGains.ValhallaGains.controllers;

import com.ValhallaGains.ValhallaGains.dtos.NewPurchaseDTO;
import com.ValhallaGains.ValhallaGains.dtos.PurchaseDTO;
import com.ValhallaGains.ValhallaGains.models.*;
import com.ValhallaGains.ValhallaGains.repositories.ClientRepository;
import com.ValhallaGains.ValhallaGains.repositories.ProductRepository;
import com.ValhallaGains.ValhallaGains.repositories.PurchaseProductRepository;
import com.ValhallaGains.ValhallaGains.repositories.PurchaseRepository;
import com.ValhallaGains.ValhallaGains.services.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PurchaseController {
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    MercadoPagoController mercadoPago;
    @Autowired
    EmailService emailService;

    @Autowired
    PurchaseProductRepository purchaseProductRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping("/purchase")
    @Transactional
    public ResponseEntity<Object> createPurchase(
            @RequestBody NewPurchaseDTO newPurchaseDTO,
            Authentication authentication) {

        // Obtener el cliente autenticado
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail);

        // Validar que el cliente exista
        if (client == null) {
            return new ResponseEntity<>("No estás autenticado. Inicia sesión salame", HttpStatus.FORBIDDEN);
        }

        // validar ramaño de las listas quantities nombreProductos
        if (newPurchaseDTO.quantities().size() != newPurchaseDTO.nombreProductos().size()) {
            return new ResponseEntity<>("Las listas(cantidades y productos) deben ser del mismo tamaño", HttpStatus.BAD_REQUEST);
        }

        // obtengo la lista de quantities y nombreProductos desde el DTO
        List<Integer> quantities = newPurchaseDTO.quantities();
        List<String> nombreDeProductos = newPurchaseDTO.nombreProductos();
        List<Integer> unitPrice = newPurchaseDTO.unit_price();

        // Creao la compra
        Purchase purchaseCreado = new Purchase();
        purchaseCreado.setClient(client);
        purchaseCreado.setTotalAmount(0);

        purchaseCreado.setData(LocalDateTime.now());
        String numberOfPurchase;
        do {
            numberOfPurchase = String.valueOf(getRandomNumber(00000000,99999999));
        }while (purchaseRepository.existsByNumberOfPurchase(numberOfPurchase));
        purchaseCreado.setNumberOfPurchase(numberOfPurchase);
        // Lista para almacenar los PurchaseProduct
        List<PurchaseProduct> purchaseProducts = new ArrayList<>();

        // Aca itero sobre ambasa listas(quanties y nombreProductos) para poder enviar en un pedido
        // varios productos con distintas cantidades
        for (int i = 0; i < quantities.size(); i++) {
            Integer quantity = quantities.get(i);
            String nombreProducto = nombreDeProductos.get(i);
            Product productSeleccionado = productRepository.findByName(nombreProducto);
            Long stockDelProductoSeleccionado= productSeleccionado.getStock();
            Long stockActualizado = stockDelProductoSeleccionado - quantity;
            // SI EL PRODUCTO QUE ME LLEGA DESDE EL FRONT NO EXISTE EN MI BASE DE DATOS DEVUELVO UN ERROR POR SER SALAME
            if (productSeleccionado == null) {
                return new ResponseEntity<>("NO EXISTE EL PRODUCTO: " + nombreProducto +" ,SOS SALAME CHE", HttpStatus.BAD_REQUEST);
            }
            if (quantity > stockDelProductoSeleccionado ){
                return new ResponseEntity<>("La cantidad pedida supera el stock disponible del producto " + productSeleccionado.getName(), HttpStatus.BAD_REQUEST);
            }
            productSeleccionado.setStock(stockActualizado);
            productRepository.save(productSeleccionado);

            Integer precioFinalProducto = (int) (quantity * productSeleccionado.getPrice());

            purchaseCreado.setTotalAmount(purchaseCreado.getTotalAmount() + precioFinalProducto);
            PurchaseProduct purchaseProduct = new PurchaseProduct(quantity);
            purchaseProduct.setProduct(productSeleccionado);
            purchaseProduct.setPurchase(purchaseCreado);


            purchaseProducts.add(purchaseProduct);

        }



        purchaseCreado.setPurchaseProducts(purchaseProducts);


        // Guardo en la base de datos
        purchaseRepository.save(purchaseCreado);
        purchaseProductRepository.saveAll(purchaseProducts);

        // Generate PDF
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"+"   "+"HH:mm");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        int totalAmount = purchaseCreado.getTotalAmount();
        String totalAmountLabel = "Total amount:";
        String formattedTotalAmount = currencyFormat.format(totalAmount);
        String formattedDateTime = purchaseCreado.getData().format(formatter);

        String productsData = "";
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            Product product = purchaseProduct.getProduct();
            Integer quantity = purchaseProduct.getQuantities();
            String formattedProductPrice = currencyFormat.format(product.getPrice());
            productsData += product.getName() + " " + quantity + " " + formattedProductPrice + "\n";
        }
        String purchaseData = "Ticket N°" + purchaseCreado.getNumberOfPurchase() + "\n"
                + "Date: " + formattedDateTime + "\n"
                + "Delivery status:" + purchaseCreado.getType() + "\n"
                + "thank you for your purchase " + client.getName() ;
        byte[] pdfBytes = PdfGenerator.generatePdf(productsData,purchaseData,formattedTotalAmount,totalAmountLabel);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "purchase_details.pdf");
        //ENVIO DE MAIL
        emailService.sendListEmail(client.getEmail(), pdfBytes);
        //FIN DE ENVIO DE MAIL

        //INICIO MERCADO PAGO
        mercadoPago.getList(newPurchaseDTO);
        //FIN MERCADO PAGO

        return new ResponseEntity<>(/*pdfBytes, headers,*/ mercadoPago.getList(newPurchaseDTO),HttpStatus.CREATED);

    }
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping("/purchase")
    public List<PurchaseDTO> getPurchaseProduct() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchase -> new PurchaseDTO(purchase))
                .collect(Collectors.toList());
    }
}