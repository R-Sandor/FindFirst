package dev.findfirst.users.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
  @Id
  private Integer role_id;

  private URole name;
}
