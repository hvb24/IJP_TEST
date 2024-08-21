package com.IJP.candidate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="employees",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "empID"
                })
        }

)
public class Candidate {

    @Id
    private String empID;
    private  String firstName;
    private String lastName;
    private String email;
    private String dob;
    private String managerId;
    private String managerEmail;

    static private long candidatesCount = 0;
    @PrePersist
    public void generateCandidateId()
    {
        System.out.println("hello");
        candidatesCount++;
        this.empID =  "EMP" + String.format("%04d",candidatesCount);
    }




}
