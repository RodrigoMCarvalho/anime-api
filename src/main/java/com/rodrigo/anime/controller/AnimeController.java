package com.rodrigo.anime.controller;

import com.rodrigo.anime.dto.AnimeDTO;
import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.service.AnimeService;
import com.rodrigo.anime.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/animes")
@Log4j2
public class AnimeController {

    private DateUtil dateUtile;
    private AnimeService animeService;

    @Autowired
    public AnimeController(DateUtil dateUtile, AnimeService animeService) {
        this.dateUtile = dateUtile;
        this.animeService = animeService;
    }

    @GetMapping
    public ResponseEntity<List<Anime>> getAnimes() {
        //log.info(dateUtile.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.getAnimes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anime> getAnimeById(@PathVariable Long id) {
        return ResponseEntity.ok(animeService.getAnimeById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAnime(@PathVariable Long id) {
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Anime> saveAnime(@RequestBody @Valid AnimeDTO animeDTO, HttpServletResponse response) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(animeDTO.getId())
                .toUri();
        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(animeService.save(animeDTO));
    }

    @PutMapping
    public ResponseEntity<Anime> updateAnime(@RequestBody @Valid AnimeDTO animeDTO) {
        return ResponseEntity.ok(animeService.save(animeDTO));

    }








}
