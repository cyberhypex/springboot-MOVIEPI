package com.movieflix.DTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data //remove boiler plate getter and setter as we will use this class to get repsonse and set response
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO { //DTO class do not need  DB level Validation


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;


    @NotBlank(message = "Please provide movie title")
    private String title;

    @NotBlank(message = "Please provide movie director")
    private String director;

    @NotBlank(message = "Please provide movie studio name")
    private String studio;


    @CollectionTable(name = "movie_cast") //create a table inside and also can chaneg with the change in entity
    private Set<String> movieCast;




    @NotBlank(message = "Please provide the poster")
    private String poster;



    private Integer releaseYear;


    @NotBlank(message = "Please Provide poster url") //validation
    private String posterUrl; //to serve it on the frontend
}
