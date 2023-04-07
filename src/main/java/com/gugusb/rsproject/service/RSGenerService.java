package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.MovieWithGenres;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSGenresRepository;
import com.gugusb.rsproject.util.GenreTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RSGenerService {
    @Autowired
    RSGenresRepository genresRepository;

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
        return genresRepository.findById(movieId).get();
    }

    public Map<Integer, List<Integer>> getAllMovieMap(){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(MovieWithGenres mwg : genresRepository.findAll()){
            int movieId = mwg.getId();
            map.put(movieId, GenreTransformer.TransformGenres(mwg));
        }
        return map;
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
