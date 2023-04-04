package com.gugusb.rsproject.util;

import com.gugusb.rsproject.entity.MovieWithGenres;

import java.util.*;

public class GenreTransformer {

    public static List<Integer> TransformGenres(MovieWithGenres movie){
        List<Integer> list = new ArrayList<>();
        list.add(movie.getGenercount().intValue());//0
        list.add(movie.getAction().intValue());
        list.add(movie.getAdventure().intValue());
        list.add(movie.getAnimation().intValue());
        list.add(movie.getChildrens().intValue());
        list.add(movie.getComedy().intValue());//5
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
        list.add(movie.getWestern().intValue());//19
        return list;
    }

    public static Map<Integer, List<Integer>> CreateGenreMap(List<MovieWithGenres> movies){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(MovieWithGenres movie : movies){
            map.put(movie.getId(), TransformGenres(movie));
        }
        return map;
    }
}
