package com.epam.admissions.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer budgetPlaces;
    private Integer contractPlaces;

    @ManyToMany(mappedBy = "selectedFaculties")
    private Set<User> candidates;

    private Boolean finalized;

    @Override
    public String toString() {
        return "[" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "budgetPlaces = " + budgetPlaces + ", " +
                "contractPlaces = " + contractPlaces + ", " +
                "]";
    }
}
