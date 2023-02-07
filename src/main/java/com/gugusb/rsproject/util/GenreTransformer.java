package com.gugusb.rsproject.util;

import com.gugusb.rsproject.entity.MovieWithGeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreTransformer {

    public static List<Integer> TransformGenres(MovieWithGeners movie){
        List<Integer> list = new ArrayList<>();
        list.add(movie.getGenercount().intValue());
        list.add(movie.getAction().intValue());
        list.add(movie.getAdventure().intValue());
        list.add(movie.getAnimation().intValue());
        list.add(movie.getChildrens().intValue());
        list.add(movie.getComedy().intValue());
        list.add(movie.getCrime().intValue());
        list.add(movie.getDocumentary().intValue());
        list.add(movie.getDrama().intValue());
        list.add(movie.getFantasy().intValue());
        list.add(movie.getFilmNoir().intValue());
        list.add(movie.getGenercount().intValue());
        list.add(movie.getHorror().intValue());
        list.add(movie.getMusical().intValue());
        list.add(movie.getMystery().intValue());
        list.add(movie.getRomance().intValue());
        list.add(movie.getSciFi().intValue());
        list.add(movie.getThriller().intValue());
        list.add(movie.getWar().intValue());
        list.add(movie.getWestern().intValue());
        return list;
    }

    public static Map<Integer, List<Integer>> CreateGenreMap(List<MovieWithGeners> movies){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(MovieWithGeners movie : movies){
            map.put(movie.getId(), TransformGenres(movie));
        }
        return map;
    }
}
