package io.github.trashemail.utils.exceptions.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ResultCummulativeEmailsPerDay {
    String day;
    Long count;

    public ResultCummulativeEmailsPerDay(String day, Long count) {
        this.day = day;
        this.count = count;
    }
}
