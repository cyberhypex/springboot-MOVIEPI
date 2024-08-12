package com.movieflix.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.DTO.MovieDTO;
import com.movieflix.Service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDTO> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto
                                                    ) throws IOException {
    MovieDTO dto=convertToMovieDto(movieDto);
    return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);


    }

    private MovieDTO convertToMovieDto(String movieDtoObj) throws JsonProcessingException { //private Method to convert a stringMovieDto to MovieDto

        ObjectMapper objectMapper=new ObjectMapper(); //Object mapping help from package
      return  objectMapper.readValue(movieDtoObj,MovieDTO.class); //readvalue from string movieDtoObj and map to MovieDTOobje
        //because response will be in JSON , and while dealing
        //with file and jSOn u can't directly send any files, convert in string


    }


    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovieHandler(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")

    public  ResponseEntity<List<MovieDTO>> getAllMovieHandler(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }
}
