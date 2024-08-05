package com.movieflix.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter

@Entity
@Table(name = "moviepi")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please provide movie title")
    private String title;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie director")
    private String director;
    @Column(nullable = false)
    @NotBlank(message = "Please provide movie studio name")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast") //create a table inside and also can chaneg with the change in entity
    private Set<String> movieCast;



    @Column(nullable = false)
    @NotBlank(message = "Please provide the poster")
    private String poster;

    @Column(nullable = false,length = 4)
    @NotBlank(message = "Please provide the poster")
    private Integer releaseYear;


}
