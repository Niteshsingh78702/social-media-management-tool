package com.flintzy.socialtool.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_accounts")
@Data
@NoArgsConstructor
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pageId;

    @Column(nullable = false)
    private String pageName;

    @Column(nullable = false, length = 500)
    private String pageAccessToken;

    @Column(nullable = false)
    private String platform;      // FACEBOOK, INSTAGRAM, YOUTUBE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
