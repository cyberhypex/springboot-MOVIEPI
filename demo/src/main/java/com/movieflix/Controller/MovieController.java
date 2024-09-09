package com.movieflix.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.DTO.MovieDTO;
import com.movieflix.DTO.MoviePageResponse;
import com.movieflix.Service.MovieService;
import com.movieflix.exceptions.EmptyFileException;
import com.movieflix.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import java.io.IOException;

import static com.movieflix.utils.AppConstants.PAGE_NUMBER;
import static com.movieflix.utils.AppConstants.PAGE_SIZE;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAuthority('ADMIN')") //due to web method security
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDTO> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto
                                                    ) throws IOException, EmptyFileException {
        if(file.isEmpty()){
            throw new EmptyFileException("File is Empty! Please send another file");
        }
    MovieDTO dto=convertToMovieDto(movieDto);
    return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);


    }




    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDTO> getMovieHandler(@PathVariable Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")

    public  ResponseEntity<List<MovieDTO>> getAllMovieHandler(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDTO> updateMovieHandler(@PathVariable Integer movieId,
                                                       @RequestPart MultipartFile file,
                                                       @RequestPart String movieDtoObj) throws IOException {
        if (file.isEmpty()) file = null;
        MovieDTO movieDto = convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto, file));
    }

    @DeleteMapping("/delete/{movieID}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieID) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieID));
    }

    private MovieDTO convertToMovieDto(String movieDtoObj) throws JsonProcessingException { //private Method to convert a stringMovieDto to MovieDto

        ObjectMapper objectMapper=new ObjectMapper(); //Object mapping help from package
        return  objectMapper.readValue(movieDtoObj,MovieDTO.class); //readvalue from string movieDtoObj and map to MovieDTOobje
        //because response will be in JSON , and while dealing
        //with file and jSOn u can't directly send any files, convert in string


    }

    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMovieWithPagination(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize


    ){

        return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber,pageSize));



    }


    @GetMapping("/allMappingPageSort")
    public ResponseEntity<MoviePageResponse> getMovieWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(defaultValue =AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false)String dir

    ){

        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationandSorting(pageNumber,pageSize,sortBy,dir));



    }
}
