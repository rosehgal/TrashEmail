package io.github.trashemail.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "used_user_ids")
public class UsedUserId {
    @Id
    @GeneratedValue
    private Long id;

    private Long chatId;
    private Boolean isActive;
    private String userId;

    @CreationTimestamp
    private LocalDateTime createDateTime;
}

