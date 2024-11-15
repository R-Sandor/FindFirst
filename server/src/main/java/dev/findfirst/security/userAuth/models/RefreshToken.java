package dev.findfirst.security.userAuth.models;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import dev.findfirst.users.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table
public class RefreshToken {

  @Id
  private Long id;

  @Column("user_id")
  AggregateReference<User, Integer> user; 

  private String token;

  private Instant expiryDate;
}
