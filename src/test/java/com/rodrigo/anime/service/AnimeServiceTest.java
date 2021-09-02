package com.rodrigo.anime.service;

import com.rodrigo.anime.dto.AnimeDTO;
import com.rodrigo.anime.exception.AnimeNotFoundException;
import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DisplayName("Testes para o AnimeService")
@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup() {
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        Anime animeValido = AnimeCreator.createValidAnime();
        List<Anime> animes = List.of(AnimeCreator.createValidAnime());

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(animeValido));
        BDDMockito.when(animeRepositoryMock.findByNomeContainingIgnoreCase(ArgumentMatchers.anyString())).thenReturn(animes);
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class))).thenReturn(animeValido);
        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    void deveRetornarListaDeAnimesPageComSucesso() {
        String nomeEsperado = AnimeCreator.createValidAnime().getNome();
        Page<Anime> animePage = animeService.getAnimes(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    void deveRetornarAnimePoIdComSucesso() {
        Long idEsperado = AnimeCreator.createValidAnime().getId();
        Anime anime = animeService.getAnimeById(idEsperado);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(idEsperado);
    }

    @Test
    void deveLancarAnimeNotFoundException() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(AnimeNotFoundException.class)
                .isThrownBy(() -> animeService.getAnimeById(2L))
                .withMessageContaining("Anime não encontrado!");
    }

    @Test
    void deveRetornarAnimePoNomeComSucesso() {
        String nomeEsperado = AnimeCreator.createValidAnime().getNome();
        List<Anime> animes = animeService.getAnimeByNome(nomeEsperado);

        Assertions.assertThat(animes).isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getNome()).isEqualTo(nomeEsperado);
    }

    @Test
    void naoDeveRetornarAnimePoNome() {
        BDDMockito.when(animeRepositoryMock.findByNomeContainingIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        List<Anime> animes = animeService.getAnimeByNome("qualquer");

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    void deveSalvarAnimeComSucesso() {
        AnimeDTO animeDTO = AnimeCreator.createValidAnimeDTO();
        Anime animeSalvo = animeService.save(animeDTO);

        Assertions.assertThat(animeSalvo.getNome()).isEqualTo(animeDTO.getNome());
    }

    @Test
    void deveDeletarAnimePoIdComSucesso() {
        Long idEsperado = AnimeCreator.createValidAnime().getId();

        Assertions.assertThatCode(() -> animeService.delete(idEsperado)).doesNotThrowAnyException();
    }

}