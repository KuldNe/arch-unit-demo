package com.tietoevry.archunitdemo.domain;

import javax.persistence.*;


@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

}
