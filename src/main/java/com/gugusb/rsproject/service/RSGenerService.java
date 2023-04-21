package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.MovieWithGenres;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSGenresRepository;
import com.gugusb.rsproject.util.GenreTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RSGenerService {
    @Autowired
    RSGenresRepository genresRepository;

    public Boolean updateMovieInf(Integer movieId, List<Integer> genres){
        if(genresRepository.existsById(movieId)){
            List<Integer> list = new Stack<>();
            for(int i = 0;i < 18;i ++){
                list.add(0);
            }
            for(int i: genres){
                list.set(i - 1, 1);
            }
            MovieWithGenres mwr = GenreTransformer.TransformGenres(list);
            mwr.setId(movieId);
            genresRepository.save(mwr);
            return true;
        }else {
            return false;
        }
    }

    public Page<MovieWithGenres> searchMovieByTagsOrderById(Pageable pageable, List<Integer> tags, Boolean isAsc){
        if(isAsc){
            return genresRepository.findByActionInAndAdventureInAndAnimationInAndChildrensInAndComedyInAndCrimeInAndDocumentaryInAndDramaInAndFantasyInAndFilmNoirInAndHorrorInAndMusicalInAndMysteryInAndRomanceInAndSciFiInAndThrillerInAndWarInAndWesternInOrderByIdAsc(
                    (tags.get(0) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(1) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(2) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(3) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(4) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(5) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(6) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(7) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(8) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(9) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(10) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(11) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(12) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(13) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(14) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(15) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(16) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(17) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    pageable
            );
        }else{
            return genresRepository.findByActionInAndAdventureInAndAnimationInAndChildrensInAndComedyInAndCrimeInAndDocumentaryInAndDramaInAndFantasyInAndFilmNoirInAndHorrorInAndMusicalInAndMysteryInAndRomanceInAndSciFiInAndThrillerInAndWarInAndWesternInOrderByIdDesc(
                    (tags.get(0) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(1) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(2) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(3) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(4) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(5) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(6) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(7) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(8) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(9) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(10) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(11) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(12) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(13) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(14) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(15) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(16) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    (tags.get(17) > 0 ? List.of(new Byte[]{(byte) 1}):List.of(new Byte[]{(byte) 0, (byte) 1})),
                    pageable
            );
        }


    }

    public Boolean addMovie(Integer[] genres){
        MovieWithGenres movieWithGenres = GenreTransformer.TransformGenres(genres);
        genresRepository.save(movieWithGenres);
        return true;
    }
    public Boolean deleteMovie(Integer movieid){
        if(checkExist(movieid)){
            MovieWithGenres movie = new MovieWithGenres();
            movie.setId(movieid);
            genresRepository.delete(movie);
            return true;
        }
        return false;
    }
    public Boolean checkExist(Integer movieid){
        if(genresRepository.findById(movieid).isEmpty()){
            return false;
        }
        return true;
    }

    public MovieWithGenres getMovieGenresById(Integer movieId){
        if(genresRepository.existsById(movieId)){
            return genresRepository.findById(movieId).get();
        }else{
            return null;
        }

    }

    public Map<Integer, List<Integer>> getAllMovieMap(){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(MovieWithGenres mwg : genresRepository.findAll()){
            int movieId = mwg.getId();
            map.put(movieId, GenreTransformer.TransformGenres(mwg));
        }
        return map;
    }

    public List<Integer> getMovieMap(MovieWithGenres mwr){
        List<Integer> list = GenreTransformer.TransformGenres(mwr).subList(1, 19);
        List<Integer> res = new Stack<>();
        for(int i = 0;i < list.size();i ++){
            if(list.get(i) != 0){
                res.add(i + 1);
            }
        }
        return res;
    }

    public List<MovieWithGenres> getAllMovieList(){
        return genresRepository.findAll();
    }

    public List<MovieWithGenres> TransMovieToMovieWithGenre(List<RSMovie> movies){
        List<MovieWithGenres> movieWithGenresList = new ArrayList<>();
        for(RSMovie movie : movies){
            movieWithGenresList.add(genresRepository.findById(movie.getId()).get());
        }
        return movieWithGenresList;
    }

}
