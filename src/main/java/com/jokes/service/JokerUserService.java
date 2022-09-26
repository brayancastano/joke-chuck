package com.jokes.service;

import com.jokes.domain.JokesUser;
import com.jokes.service.dto.Joke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokerUserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public JokerUserService() {}

    public JokesUser transaction() {
        JokesUser jokesUser = new JokesUser();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.chucknorris.io/jokes/random";
        ResponseEntity<Joke> response = restTemplate.getForEntity(url, Joke.class);
        jokesUser.setIdJoke(response.getBody().getId());
        jokesUser.setUrl(response.getBody().getUrl());
        jokesUser.setIconUrl(response.getBody().getIconUrl());
        jokesUser.setValue(response.getBody().getValue());
        return jokesUser;
    }
}
