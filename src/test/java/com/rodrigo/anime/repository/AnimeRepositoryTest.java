package com.rodrigo.anime.repository;

import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Testes para o AnimeRepository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    void devePersistirAnimeComSucesso() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSalvo = animeRepository.save(anime);

        Assertions.assertThat(animeSalvo).isNotNull();
        Assertions.assertThat(animeSalvo.getId()).isNotNull();
        Assertions.assertThat(animeSalvo.getNome()).isNotNull();
    }

    @Test
    void deveAtualizarAnimeComSucesso() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSalvo = animeRepository.save(anime);

        animeSalvo.setNome("Vinland Saga");
        Anime animeAtualizado = animeRepository.save(anime);

        Assertions.assertThat(animeAtualizado.getNome()).isEqualTo("Vinland Saga");
    }

    @Test
    void deveExcluirAnimeComSucesso() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSalvo = animeRepository.save(anime);
        animeRepository.delete(animeSalvo);

        Optional<Anime> animeOptional = animeRepository.findById(animeSalvo.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    void deveLocalizarAnimePeloNomeComSucesso() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSalvo = animeRepository.save(anime);

        List<Anime> animes = animeRepository.findByNomeContainingIgnoreCase("gon");

        Assertions.assertThat(animes).isNotEmpty();
        Assertions.assertThat(animes).contains(animeSalvo);
    }

    @Test
    void deveRetornarListaVaziaQuandoAnimeNaoEncontrado() {
        Anime anime = AnimeCreator.createAnimeToBeSaved();
        Anime animeSalvo = animeRepository.save(anime);

        List<Anime> animes = animeRepository.findByNomeContainingIgnoreCase("tata");

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    void deveLançarConstraintViolationExceptionQuandoNomeForNulo() {
        Anime anime = AnimeCreator.createAnimeNotName();

        Assertions.assertThatThrownBy(() -> animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void deveLançarConstraintViolationExceptionComMensagemQuandoNomeForNulo() {
        Anime anime = AnimeCreator.createAnimeNotName();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> animeRepository.save(anime))
                .withMessageContaining("Nome obrigatório");
    }











}