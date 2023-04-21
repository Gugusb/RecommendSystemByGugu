package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.MovieWithGenres;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSMovieRepository;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.GenreTransformer;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RSMovieService {
    @Autowired
    RSMovieRepository movieRepository;
    @Autowired
    RSRatingRepository ratingRepository;

    public Boolean updateMovieInf(Integer movieId, String name, List<Integer> genres){
        if(movieRepository.existsById(movieId)){
            movieRepository.updateTitleById(name, movieId);
            String newGenres = "";
            Boolean isF = false;
            for(int i : genres){
                if(!isF){
                    newGenres += GenreTransformer.getGenreName(i);
                    isF = true;
                }else{
                    newGenres += "|" + GenreTransformer.getGenreName(i);
                }
            }
            movieRepository.updateGenresById(newGenres, movieId);
            return true;
        }else{
            return false;
        }
    }

    public Boolean removeMovie(Integer movieId){
        if(movieRepository.existsById(movieId)){
            movieRepository.deleteById(movieId);
            ratingRepository.deleteByMovieid(movieId);
            return true;
        }else{
            return false;
        }
    }

    public Page<RSMovie> getAllMovies(Pageable pageable){
        return movieRepository.findAll(pageable);
    }

    public Page<RSMovie> getMovieById(Integer movieId, Pageable pageable){
        return movieRepository.findById(movieId, pageable);
    }

    public List<String> getNamesFromRating(Page<RSRating> ratings){
        List<String> res = new Stack<>();
        for(RSRating rating : ratings){
            res.add(movieRepository.findById(rating.getMovieid()).get().getTitle());
        }
        return res;
    }

    public List<RSMovie> getMovieFromGenres(Page<MovieWithGenres> genres){
        List<RSMovie> res = new Stack<>();
        for(MovieWithGenres mwg : genres){
            res.add(movieRepository.findById(mwg.getId()).get());
        }
        return res;
    }

    public Boolean addMovie(RSMovie movie, Integer[] genres){
        String genreS = "";
        for(int i = 0;i < 18;i ++){
            if(i != 0){
                genreS += "|";
            }
            if(genres[i] == 1){
                genreS += GenreTransformer.getGenreName(i + 1);
            }
        }
        movie.setGenres(genreS);
        movieRepository.save(movie);
        return true;
    }
    public Boolean deleteMovie(Integer movieid){
        if(checkExist(movieid)){
            RSMovie movie = new RSMovie();
            movie.setId(movieid);
            movieRepository.delete(movie);
            return true;
        }
        return false;
    }
    public Boolean checkExist(Integer movieid){
        if(movieRepository.findById(movieid).isEmpty()){
            return false;
        }
        return true;
    }
}
