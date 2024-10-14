package ru.gb.timesheet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_roles")
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id; // primary key

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "role_name")
  private String roleName;

}
