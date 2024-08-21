package com.IJP.hr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="hr",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "hrId"
                })
        }
)
public class HrEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int hrId;
    private String email;
    private String password;

}
