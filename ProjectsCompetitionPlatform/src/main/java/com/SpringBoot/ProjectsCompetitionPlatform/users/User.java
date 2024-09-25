package com.SpringBoot.ProjectsCompetitionPlatform.users;

import com.SpringBoot.ProjectsCompetitionPlatform.projects.Project;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Project> projects;

    private String firstName, lastName;
    @Setter
    @Getter
    private String status = "User";


    public User(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, String status){
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public String toString(){
        return firstName + " " + lastName + ", Status: " + status;
    }


}