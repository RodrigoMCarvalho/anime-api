package com.rodrigo.anime.controller;

import com.rodrigo.anime.dto.AnimeDTO;
import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.service.AnimeService;
import com.rodrigo.anime.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Testando AnimeControler")
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        Anime animeValido = AnimeCreator.createValidAnime();
        List<Anime> animes = List.of(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.getAnimes(ArgumentMatchers.any())).thenReturn(animePage);
        BDDMockito.when(animeServiceMock.getAnimeById(ArgumentMatchers.anyLong())).thenReturn(animeValido);
        BDDMockito.when(animeServiceMock.getAnimeByNome(ArgumentMatchers.anyString())).thenReturn(animes);
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimeDTO.class))).thenReturn(animeValido);
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    void deveRetornarListaDeAnimesPageComSucesso() {
        String nomeEsperado = AnimeCreator.createValidAnime().getNome();
        Page<Anime> animePage = animeController.getAnimes(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    void deveRetornarAnimePoIdComSucesso() {
        Long idEsperado = AnimeCreator.createValidAnime().getId();
        Anime anime = animeController.getAnimeById(idEsperado).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    void deveRetornarAnimePoNomeComSucesso() {
        String nomeEsperado = AnimeCreator.createValidAnime().getNome();
        List<Anime> animes = animeController.getAnimeByNome(nomeEsperado).getBody();

        Assertions.assertThat(animes).isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    void naoDeveRetornarAnimePoNome() {
        BDDMockito.when(animeServiceMock.getAnimeByNome(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Anime> animes = animeController.getAnimeByNome("qualquer").getBody();

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    void deveSalvarAnimeComSucesso() {
        AnimeDTO animeDTO = AnimeCreator.createValidAnimeDTO();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<Anime> responseEntity = animeController.saveAnime(animeDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");
    }

    @Test
    void deveDeletarAnimePoIdComSucesso() {
        Long idEsperado = AnimeCreator.createValidAnime().getId();
        ResponseEntity entity = animeController.deleteAnime(idEsperado);

        Assertions.assertThatCode(() -> animeController.deleteAnime(idEsperado)).doesNotThrowAnyException();
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}