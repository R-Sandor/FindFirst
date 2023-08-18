package dev.findfirst.bookmarkit.users.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer role_id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private URole name;

  public Role(URole name) {
    this.name = name;
  }

  public Integer getId() {
    return role_id;
  }

  public void setId(Integer id) {
    this.role_id = id;
  }

  public URole getName() {
    return name;
  }

  public void setName(URole name) {
    this.name = name;
  }
}
