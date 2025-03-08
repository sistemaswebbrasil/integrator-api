package br.com.siswbrasil.integrator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.TokenRequest;

@Configuration
public class Auth0ManagementConfig {

    @Value("${auth0.mgmt.domain:${auth0.domain}}")
    private String domain;

    @Value("${auth0.mgmt.clientId:${auth0.clientId}}")
    private String clientId;

    @Value("${auth0.mgmt.clientSecret:${auth0.clientSecret}}")
    private String clientSecret;

    @Bean
    public ManagementAPI managementAPI() throws Auth0Exception {
        // Obter token de acesso para Management API
        AuthAPI authAPI = new AuthAPI(domain, clientId, clientSecret);
        TokenRequest tokenRequest = authAPI.requestToken("https://" + domain + "/api/v2/");
        
        // Fix: Get the TokenHolder from the response
        TokenHolder holder = tokenRequest.execute().getBody();
        String accessToken = holder.getAccessToken();
        
        // Criar client do Management API com o token obtido
        return new ManagementAPI(domain, accessToken);
    }
}