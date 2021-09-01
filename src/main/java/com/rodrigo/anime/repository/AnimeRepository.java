package com.rodrigo.anime.repository;

import com.rodrigo.anime.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long> {
}
