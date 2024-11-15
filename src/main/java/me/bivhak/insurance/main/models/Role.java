package me.bivhak.insurance.main.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Role model class
 */
@Entity
@Getter
@Setter
@Table(name = "roles")
public final class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private Roles name;

    public Role() {
    }

    public Role(Roles name) {
        this.name = name;
    }
}