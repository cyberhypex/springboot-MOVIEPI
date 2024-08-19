package com.movieflix.Service;

import com.movieflix.DTO.MovieDTO;
import com.movieflix.DTO.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException;
    MovieDTO getMovie(Integer movieID);

    List<MovieDTO> getAllMovies();

    MovieDTO updateMovie(Integer movieID,MovieDTO movieDTO,MultipartFile file) throws IOException;


    String deleteMovie(Integer movieID) throws IOException;

    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber,Integer pageSize);

    MoviePageResponse getAllMoviesWithPaginationandSorting(Integer pageNumber,Integer pageSize,String sortBy,String dir);

}
