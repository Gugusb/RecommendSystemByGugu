package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.MovieWithGenres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

public interface RSGenresRepository extends JpaRepository<MovieWithGenres, Integer> {
    Page<MovieWithGenres> findByActionInAndAdventureInAndAnimationInAndChildrensInAndComedyInAndCrimeInAndDocumentaryInAndDramaInAndFantasyInAndFilmNoirInAndHorrorInAndMusicalInAndMysteryInAndRomanceInAndSciFiInAndThrillerInAndWarInAndWesternInOrderByIdAsc(Collection<Byte> actions, Collection<Byte> adventures, Collection<Byte> animations, Collection<Byte> childrens, Collection<Byte> comedies, Collection<Byte> crimes, Collection<Byte> documentaries, Collection<Byte> dramas, Collection<Byte> fantasies, Collection<Byte> filmNoirs, Collection<Byte> horrors, Collection<Byte> musicals, Collection<Byte> mysteries, Collection<Byte> romances, Collection<Byte> sciFis, Collection<Byte> thrillers, Collection<Byte> wars, Collection<Byte> westerns, Pageable pageable);

    Page<MovieWithGenres> findByActionInAndAdventureInAndAnimationInAndChildrensInAndComedyInAndCrimeInAndDocumentaryInAndDramaInAndFantasyInAndFilmNoirInAndHorrorInAndMusicalInAndMysteryInAndRomanceInAndSciFiInAndThrillerInAndWarInAndWesternInOrderByIdDesc(Collection<Byte> actions, Collection<Byte> adventures, Collection<Byte> animations, Collection<Byte> childrens, Collection<Byte> comedies, Collection<Byte> crimes, Collection<Byte> documentaries, Collection<Byte> dramas, Collection<Byte> fantasies, Collection<Byte> filmNoirs, Collection<Byte> horrors, Collection<Byte> musicals, Collection<Byte> mysteries, Collection<Byte> romances, Collection<Byte> sciFis, Collection<Byte> thrillers, Collection<Byte> wars, Collection<Byte> westerns, Pageable pageable);

}