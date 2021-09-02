package com.rodrigo.anime.util;

import com.rodrigo.anime.dto.AnimeDTO;
import com.rodrigo.anime.model.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder().nome("Dragon Ball Z").ano(1984L).build();
    }

    public static Anime createValidAnime() {
        return Anime.builder().id(1L).nome("Samurai Champloo").ano(2004L).build();
    }

    public static AnimeDTO createValidAnimeDTO() {
        return AnimeDTO.builder().id(1L).nome("Cowboy Bebop").ano(2002L).build();
    }

    public static Anime createAnimeNotName() {
        return Anime.builder().ano(2004L).build();
    }

    public static Anime createValidUpdateAnime() {
        return Anime.builder().id(1L).nome("Hunter X Hunter").ano(2004L).build();
    }
}
