package com.aruiz.user.notification.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {
    private Long id;
    private String img;
    private String birthdate;
    private User user;

}
