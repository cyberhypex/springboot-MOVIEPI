package com.movieflix.DTO;

import java.util.List;

public record MoviePageResponse(List<MovieDTO> movieDTO,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                boolean isLast) {

}
