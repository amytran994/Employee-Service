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
public class VisaStatus {
    @Id
    private String id;
    private String visaType;
    private String activeFlag;
    private String startDate;
    private String endDate;
    private String lastModificationDate;

}
