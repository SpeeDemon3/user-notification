package com.aruiz.user.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NotificationEntity {

    @Id
    @GeneratedValue
    private String id;

    private String content;
    private LocalDateTime creationDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity destinationUser;

}
