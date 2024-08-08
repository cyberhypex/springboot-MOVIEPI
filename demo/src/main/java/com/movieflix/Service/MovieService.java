package com.movieflix.Service;

import com.movieflix.DTO.MovieDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDTO addMovie(MovieDTO movieDTO, MultipartFile file) throws IOException;
    MovieDTO getMovie(Integer movieID);

    List<MovieDTO> getAllMovies();
}
