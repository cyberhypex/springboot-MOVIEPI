package com.movieflix.Service;


import com.movieflix.DTO.MovieDTO;
import com.movieflix.Model.Movie;
import com.movieflix.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    @Value("${project.poster}")
    private String path;


    @Value("${base.url}")
    private String baseURL;

    private final MovieRepository movieRepository;

    private final FileService fileService ;

    public MovieServiceImpl( MovieRepository movieRepository, FileService fileService) {

        this.movieRepository = movieRepository; //constructor injection
        //type of Autowiring
        this.fileService = fileService;
    }


    @Override
    public MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException {
         // 1. Upload file
     String uploadedFileName=   fileService.uploadFile(path,file);
        //2. The value of field 'poster' as fileName
        movieDTO.setPoster(uploadedFileName);
        //3. Map DTO to Movie Object as MovieRepository accepts Movie Object

        Movie movie=new Movie(
                movieDTO.getMovieId(),
                movieDTO.getTitle(),
                movieDTO.getDirector(),
                movieDTO.getStudio(),
                movieDTO.getMovieCast(),
                movieDTO.getPoster(),
                movieDTO.getReleaseYear()

        );
        //4. Save tge movie Object->saved Movie Object

        Movie savedMovie= movieRepository.save(movie);
        //Generate the poster URL

        String posterUrl=baseURL+"/file/"+uploadedFileName;
        //Map the movie object to DTO object and return it

       MovieDTO response=new MovieDTO(
               savedMovie.getMovieId(),
               savedMovie.getTitle(),
               savedMovie.getDirector(),
               savedMovie.getStudio(),
               savedMovie.getMovieCast(),
               savedMovie.getPoster(),
               savedMovie.getReleaseYear(),
               posterUrl


       );

        return response;
    }

    @Override
    public MovieDTO getMovie(Integer movieID) {
        //Check if data exist, if yes return with ID;

        Movie movie=movieRepository.findById(movieID).orElseThrow(()->new RuntimeException("Movie Not Fund"));

        //Generate Poster URL
        String movieURI=baseURL+"/file/"+movie.getPoster(); //get poster file name from DB


        //Map to Movie DTO object and return
        MovieDTO response=new MovieDTO(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getPoster(),
                movie.getReleaseYear(),
                movieURI
        );



        return response;
    }

    @Override
    public List<MovieDTO> getAllMovies() {


        //to fetch all data from db
        List<Movie> movies=movieRepository.findAll(); //since in DB movie objects are stored

        List<MovieDTO> movieDTOS=new ArrayList<>();


        //iterate through list and generate posterurl for each movie obj
        for(Movie movie:movies){
            String posterURI=baseURL+"/file/"+movie.getPoster();
            MovieDTO movieDTO=new MovieDTO(
                    movie.getMovieId(),
                   movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getPoster(),
                    movie.getReleaseYear(),
                    posterURI


            );
            movieDTOS.add(movieDTO);


        }

        //..map to MovieDto obj
        return movieDTOS;
    }
}
