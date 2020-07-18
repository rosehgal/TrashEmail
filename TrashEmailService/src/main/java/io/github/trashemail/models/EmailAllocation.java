package io.github.trashemail.models;

import io.github.trashemail.DTO.CreateEmailRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name="email_allocated")
@Entity
public class EmailAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailId;
    private String forwardsTo;
    private Boolean isActive;

    private String source;
    private String destination;
    private String destinationType;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    public EmailAllocation(CreateEmailRequest emailRequest){
        this.emailId = emailRequest.getEmailId();
        this.isActive = true;
        this.source = emailRequest.getSource();
        this.destination = emailRequest.getDestination();
        this.destinationType = emailRequest.getDestinationType();
    }

    @Override public String toString() {
        return "EmailAllocation{" +
                "id=" + id +
                ", emailId='" + emailId + '\'' +
                ", forwardsTo='" + forwardsTo + '\'' +
                ", isActive=" + isActive +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", destinationType='" + destinationType + '\'' +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
