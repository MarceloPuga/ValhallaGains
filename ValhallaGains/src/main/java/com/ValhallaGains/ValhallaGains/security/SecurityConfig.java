package com.ValhallaGains.ValhallaGains.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //DEFINICION DE ENDPOINTS AUTORIZADOS
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                                // Rutas permitidas para todossss
                                "/api/client","/api/product", "/api/purchase","/api/category","index.html","index.js","api/mp","/api/category/count","/api/email","create-product.html","/api/client/count","dashboard.html","dashboard.html","clients.html","/api/product/count","products.html","/api/client/delete","admin/**","web/**","assets/**").permitAll()
                        // Rutas permitidas para todos para  POST
                        .requestMatchers(HttpMethod.POST, "/api/client","/api/emailAdmin", "/api/product", "/api/purchase","/api/login","api/mp","/api/category","/api/logout").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/client/delete/**","/api/product/delete/**","/api/product/delete").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/client/training").permitAll()
                        .anyRequest().permitAll()
                        // Todas las demás rutas están denegadassssss
                        );
        //DESABILITO EL TOKEN YA QUE POR AHORA NO LO VOY A USAR
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //CONFIGURO CUAL VA A SER LA PAGINA DE LOGIN PARA NO USAR LA QUE TRAE POR DEFECTO SPRING SECURITY

        http.formLogin(formLogin ->
                formLogin.loginPage("/admin/login.html")
                        //CONFIGURO LAS QUERY PARAMS. ESTOS SERAN LAS "KEY" CON LAS QUE VOY A TRABAJAR EN EL FRONT
                        .usernameParameter("email")
                        .passwordParameter("password")
                        //ENDPOINT PARA LA PETICION (NO HACE FALTA CREAR UN CONTROLADOR PORQUE LO HACE POR
                        //DEFECTO)
                        .loginProcessingUrl("/api/login")
                        //MANEJADOR DE FALLAS. ESTO SIRVE PARA, EN EL CASO DE QUE FALLE, SEPA QUE HAY QUE HACER
                        //TIENE 3 PARAMETROS, EN ESTE CASO TRABAJO CON "response" PORQUE QUIERO MANEJAR LA
                        //RESPUESTA QUE LE DOY AL USUARIO
                        .failureHandler((request, response, exception) ->
                                response.sendError(407))

                        //CADA VEZ QUE EL USER HAGA UN REGISTRO FALLIDO O  SE PROVOQUE UN ERROR
                        //SE DEJAN RASTROS... LO QUE HAGO CON ESTE HANDLER ES ELIMINAR ESE RASTRO DE ERRORES
                        //UTILIZANDO EL METODO "clearAuthenticationAttributes" PARA LOGRAR LA LIMPIEZA
                        .successHandler((request, response, authentication) ->
                                clearAuthenticationAttributes(request))
                        .permitAll());
        //DEFINO CUAL VA A SER EL ENDPINT PARA EL LOGOUT
        http.logout(logout ->
                logout.logoutUrl("/api/logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
        );
        //MANEJO LO QUE PASA CUANDO UN USER TRATA DE ENTRAR EN UN LUGAR DONDE NO PUEDE
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) ->
                        response.sendError(419))
        );
        //LE DICE AL SERVIDOR QUE NOS RECUERDE PARA QUE NO PIDA CONSTANTEMENTE EL LOGIN. UNA VEZ QUE TERMINE EL
        //TIEMPO DE SESION RECIEN AHI VA A PEDIR EL LOGIN NUEVAMENTE
        http.rememberMe(Customizer.withDefaults());


        return http.build();
    }
    private void clearAuthenticationAttributes(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }
    }

}