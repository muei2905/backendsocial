package com.example.BackEndSocial.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User senderId;
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private User receiverId;
    private String content;
    private String picture;
    private String timestamp;
    private boolean isRead;
    private boolean isDeleted;
}
