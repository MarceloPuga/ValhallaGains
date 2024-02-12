package com.ValhallaGains.ValhallaGains.controllers;

import com.ValhallaGains.ValhallaGains.dtos.NewPurchaseDTO;
import com.ValhallaGains.ValhallaGains.dtos.UserBuyer;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MercadoPagoController {
    @Value("${codigo.mercadoLibre}")
    private String mercadoLibreToken;
    public String getList( NewPurchaseDTO userBuyer){
        if (userBuyer == null) {
            return "error jsons";
        }
        List<String> title = userBuyer.nombreProductos();
        List<Integer> quantity = userBuyer.quantities();
        List<Integer> price = userBuyer.unit_price();

        try {
            MercadoPagoConfig.setAccessToken(mercadoLibreToken);
            //--------------------CREACION DE PREFERENCIAS
            // 1 - PREFERENCIA DE VENTA
            List<PreferenceItemRequest> items = new ArrayList<>();
            for (int i = 0; i < title.size(); i++) {
                PreferenceItemRequest itemRequest = PreferenceItemRequest
                        .builder()
                        .title(title.get(i))
                        .quantity(quantity.get(i))
                        .unitPrice(new BigDecimal(price.get(i)))
                        .currencyId("ARS")
                        .build();
                items.add(itemRequest);
            }

            // 2 - Preferencia de control de sucesos
            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest
                    .builder()
                    .success("https://youtube.com")
                    .pending("https://youtube.com")
                    .failure("https://youtube.com")
                    .build();

            //-----------------------ENSAMBLE DE PREFERENCIAS
            //------CREO UNA PREFERENCIA QUE CONTROLA TODAS LAS PREFERENCIAS
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrlsRequest)
                    .build();
            //creo un objeto tipo client para comunicarme con MP
            PreferenceClient client = new PreferenceClient();
            //creo preferencia que es igual a la respuesta  que el client creara
            //a partir de la informacion que se envia
            Preference preference = client.create(preferenceRequest);
            System.out.println("preferenciaaaa: "+preference.getId());
            return preference.getId();

        }catch(MPException | MPApiException e ) {return title.toString();}

    }
}
