package com.example.EmployeeService.domain.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class PersonalDocument {
    @Id
    private String id;
    private String path;
    private String tittle;
    private String comment;
    private String createDate;
}
