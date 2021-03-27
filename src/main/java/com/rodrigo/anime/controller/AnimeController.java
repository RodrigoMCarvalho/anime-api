package com.rodrigo.anime.controller;

import com.rodrigo.anime.model.Anime;
import com.rodrigo.anime.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
public class AnimeController {

    private DateUtil dateUtile;

    @Autowired
    public AnimeController(DateUtil dateUtile) {
        this.dateUtile = dateUtile;
    }

    @GetMapping
    public List<Anime> getAnimes() {
        log.info(dateUtile.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return Arrays.asList(new Anime("DBZ"), new Anime("Naruto"), new Anime("Berserk"));
    }

}
