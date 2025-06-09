package ro.foame.employee_backend.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Table(name= "roles")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
