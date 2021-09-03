package com.rodrigo.anime.service;

import com.rodrigo.anime.dto.AnimeDTO;
import com.rodrigo.anime.exception.AnimeNotFoundException;
import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.repository.AnimeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;

    public Page<Anime> getAnimes(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public List<Anime> getListAnimes() {
        return animeRepository.findAll();
    }

    @Transactional
    public Anime save(AnimeDTO animeDTO) {
        Anime anime = new Anime();
        BeanUtils.copyProperties(animeDTO, anime);
        return animeRepository.save(anime);
    }

    public Anime getAnimeById(Long id) {
        return animeRepository.findById(id).orElseThrow(() -> new AnimeNotFoundException("Anime não encontrado!"));
    }

    @Transactional
    public void delete(Long id) {
        animeRepository.delete(getAnimeById(id));
    }

    public List<Anime> getAnimeByNome(String nome) {
        return animeRepository.findByNomeContainingIgnoreCase(nome);
    }
}
