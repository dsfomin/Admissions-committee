package com.epam.admissions.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Message() {
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "unknown";
    }

    public Message(String text, String tag, User author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
    }
}