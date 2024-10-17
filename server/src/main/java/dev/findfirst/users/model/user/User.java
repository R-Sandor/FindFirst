package dev.findfirst.users.model.user;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  public User(String username, String email, String encodedPasswd, boolean enabled) {
    this.username = username;
    this.email = email;
    this.password = encodedPasswd;
    this.enabled = enabled;
  }

  public User(SignupRequest signup, String encodedPasswd) {
    this(signup.username(), signup.email(), encodedPasswd, false);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "enabled")
  private boolean enabled;

  @Nonnull
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 255)
  private String password;

  @ManyToOne(optional = false)
  private Role role;

  @JsonIgnore
  @Column(nullable = false)
  private Integer tenantId;
}
