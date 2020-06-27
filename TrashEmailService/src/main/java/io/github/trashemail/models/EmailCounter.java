package io.github.trashemail.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "emailCounter")
public class EmailCounter {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer count;
}
