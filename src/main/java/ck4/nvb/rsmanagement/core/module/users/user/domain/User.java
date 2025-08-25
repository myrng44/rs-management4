package ck4.nvb.rsmanagement.core.module.users.user.domain;

import ck4.nvb.rsmanagement.base.domain.entity.FullAuditedSerialIdEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends FullAuditedSerialIdEntity {

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "full_name")
  private String name;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "phone")
  private String phone;

  public User(Long id) {
    setId(id);
  }
}
