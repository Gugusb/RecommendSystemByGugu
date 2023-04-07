package com.gugusb.rsproject.util;

import com.gugusb.rsproject.entity.MovieWithGenres;

import java.util.*;

public class GenreTransformer {
    private static String[] names = {"", "Action", "Adventure", "Animation", "Childrens", "Comedy", "Crime", "Documentary",
            "Drama", "Fantasy", "FilmNoir", "Horror", "Musical", "Mystery", "Romance", "SciFi",
            "Thriller", "War", "Western"};
    //返回ID对应的特征的名字 注意 ID从1开始
    public static String getGenreName(Integer no){
        return names[no];
    }

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
        list.add(movie.getHorror().intValue());
        list.add(movie.getMusical().intValue());
        list.add(movie.getMystery().intValue());
        list.add(movie.getRomance().intValue());
        list.add(movie.getSciFi().intValue());
        list.add(movie.getThriller().intValue());
        list.add(movie.getWar().intValue());
        list.add(movie.getWestern().intValue());//18
        return list;
    }
    public static MovieWithGenres TransformGenres(Integer[] genres){
        MovieWithGenres movieWithGenres = new MovieWithGenres();
        Integer count = 0;
        for(int i = 0;i < 18;i ++){
            if(genres[i] == 0){
                count ++;
            }
        }
        movieWithGenres.setGenercount(count.byteValue());
        movieWithGenres.setAction(genres[0].byteValue());
        movieWithGenres.setAdventure(genres[1].byteValue());
        movieWithGenres.setAnimation(genres[2].byteValue());
        movieWithGenres.setChildrens(genres[3].byteValue());
        movieWithGenres.setComedy(genres[4].byteValue());
        movieWithGenres.setCrime(genres[5].byteValue());
        movieWithGenres.setDocumentary(genres[6].byteValue());
        movieWithGenres.setDrama(genres[7].byteValue());
        movieWithGenres.setFantasy(genres[8].byteValue());
        movieWithGenres.setFilmNoir(genres[9].byteValue());
        movieWithGenres.setHorror(genres[10].byteValue());
        movieWithGenres.setMusical(genres[11].byteValue());
        movieWithGenres.setMystery(genres[12].byteValue());
        movieWithGenres.setRomance(genres[13].byteValue());
        movieWithGenres.setSciFi(genres[14].byteValue());
        movieWithGenres.setThriller(genres[15].byteValue());
        movieWithGenres.setWar(genres[16].byteValue());
        movieWithGenres.setWestern(genres[17].byteValue());

        return movieWithGenres;
    }


    public static Map<Integer, List<Integer>> CreateGenreMap(List<MovieWithGenres> movies){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(MovieWithGenres movie : movies){
            map.put(movie.getId(), TransformGenres(movie));
        }
        return map;
    }
}
