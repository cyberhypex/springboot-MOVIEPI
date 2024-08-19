package com.movieflix.Service;


import com.movieflix.DTO.MovieDTO;
import com.movieflix.DTO.MoviePageResponse;
import com.movieflix.Model.Movie;
import com.movieflix.Repository.MovieRepository;
import com.movieflix.exceptions.FileExistException;
import com.movieflix.exceptions.MovieNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new FileExistException("File name already exists");
        }

     String uploadedFileName=   fileService.uploadFile(path,file);
        //2. The value of field 'poster' as fileName
        movieDTO.setPoster(uploadedFileName);
        //3. Map DTO to Movie Object as MovieRepository accepts Movie Object

        Movie movie=new Movie(
                null, //this will help db generate primary key on its own
                movieDTO.getTitle(),
                movieDTO.getDirector(),
                movieDTO.getStudio(),
                movieDTO.getMovieCast(),
                movieDTO.getPoster(),
                movieDTO.getReleaseYear()

        );
        //4. Save the movie Object->saved Movie Object

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

        Movie movie=movieRepository.findById(movieID).orElseThrow(()->new FileExistException("Movie Not Fund"));

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

    @Override
    public MovieDTO updateMovie(Integer movieID, MovieDTO movieDTO, MultipartFile file) throws IOException {
        //1.Check if mv exists with the given movieID
        Movie mv=movieRepository.findById(movieID).orElseThrow(()->new MovieNotFoundException("Movie Not Fund"));



        //If file is null, do nothing
        //If file not null,delete existing file associated with the record
        //and upload new file

        String fileName=mv.getPoster();
        if(file!=null){
            Files.deleteIfExists(Paths.get(path+File.separator+fileName));
            fileName=fileService.uploadFile(path,file);
        }


        //Set MovieDTO's poster value acc to step 2.
        movieDTO.setPoster(fileName);

        //Map movieDTO  to mv Obj
        Movie movie=new Movie(
                mv.getMovieId(),
                movieDTO.getTitle(),
                movieDTO.getDirector(),
                movieDTO.getStudio(),
                movieDTO.getMovieCast(),
                movieDTO.getPoster(),
                movieDTO.getReleaseYear()
        );


        //save it to mv object->return mv obj
      Movie updatedMovie= movieRepository.save(movie);

        //generate poster url
        String movieURI=baseURL+"/file/"+fileName;


        //map to MOvieDTO and return response
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
    public String deleteMovie(Integer movieID) throws IOException {

        //1.Check if exists
        Movie mv=movieRepository.findById(movieID).orElseThrow(()->new MovieNotFoundException("Movie Not Fund"));
      Integer id=mv.getMovieId();
        //2.Delete the file associated with the ID
        Files.deleteIfExists(Paths.get(path+File.separator+mv.getPoster()));
        //3. Delete the movie Object
        movieRepository.delete(mv);



        return "Movie Deleted with id:"+id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize); //provided by springboot
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for (Movie movie : movies) {
            String posterURI = baseURL + "/file/" + movie.getPoster();
            MovieDTO movieDTO = new MovieDTO(
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
        return new MoviePageResponse(movieDTOS, pageNumber, pageSize, moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );

    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationandSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort=dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort); //provided by springboot
        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for (Movie movie : movies) {
            String posterURI = baseURL + "/file/" + movie.getPoster();
            MovieDTO movieDTO = new MovieDTO(
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
        return new MoviePageResponse(movieDTOS, pageNumber, pageSize, moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );
    }


}
