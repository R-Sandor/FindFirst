package dev.findfirst.users.model.user;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@Table
public class Token {

  private static final int EXPIRATION = 60 * 24;

  @Id
  private Long id;

  @Column("token")
  private String tokenVal;

  @Column("user_id")
  private AggregateReference<User, Integer> user;

  private Date expiryDate;

  public Token(AggregateReference<User, Integer> user, String token) {
    this.user = user;
    this.tokenVal = token;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }

  private Date calculateExpiryDate(int expiryTimeInMinutes) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }
}
