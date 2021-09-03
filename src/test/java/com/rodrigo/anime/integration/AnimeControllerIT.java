package com.rodrigo.anime.integration;

import com.rodrigo.anime.dto.AnimeDTO;
import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.repository.AnimeRepository;
import com.rodrigo.anime.util.AnimeCreator;
import com.rodrigo.anime.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@DisplayName("Testes de integração AnimeController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    void deveRetornarListaDeAnimesPageComSucesso() {
        Anime anime = animeRepository.save(AnimeCreator.createValidAnime());
        PageableResponse<Anime> animePageableResponse = testRestTemplate.exchange("/v1/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();

        Assertions.assertThat(animePageableResponse).isNotNull();
        Assertions.assertThat(animePageableResponse.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePageableResponse.toList().get(0).getNome()).isEqualTo(anime.getNome());
    }

    @Test
    void deveRetornarListaDeAnimesComSucesso() {
        Anime anime = animeRepository.save(AnimeCreator.createValidAnime());
        List<Anime> animes = testRestTemplate.exchange("/v1/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes).isNotNull();
        Assertions.assertThat(animes).isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getNome()).isEqualTo(anime.getNome());
    }

    @Test
    void deveRetornarAnimePorNomeComSucesso() {
        String nomeEsperado = animeRepository.save(AnimeCreator.createValidAnime()).getNome();
        String url = String.format("/v1/animes/busca?nome=%s", nomeEsperado);
        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes).isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    void deveRetornarAnimePorIdComSucesso() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createValidAnime());
        Long idEsperado = animeSalvo.getId();
        Anime anime = testRestTemplate.getForObject("/v1/animes/{id}", Anime.class, idEsperado);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    void deveSalvarAnimeComSucesso() {
        AnimeDTO animeDTO = AnimeCreator.createValidAnimeDTO();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<AnimeDTO> responseEntity = testRestTemplate.postForEntity("/v1/animes", animeDTO, AnimeDTO.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/v1/animes/1");
    }

    @Test
    void deveAtualizarAnimeComSucesso() {
        Anime animeSalvo = animeRepository.save(AnimeCreator.createValidAnime());
        animeSalvo.setNome("Fly");

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.exchange("/v1/animes",
                HttpMethod.PUT, new HttpEntity<>(animeSalvo), Anime.class);

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(animeResponseEntity).isNotNull();
    }

    @Test
    void deveDeletarAnimePoIdComSucesso() {
        Long idEsperado = animeRepository.save(AnimeCreator.createValidAnime()).getId();
        
        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/v1/animes/{id}",
                HttpMethod.DELETE, null, Void.class, idEsperado);

        Assertions.assertThatCode(() -> testRestTemplate.delete("/v1/animes", idEsperado)).doesNotThrowAnyException();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}
