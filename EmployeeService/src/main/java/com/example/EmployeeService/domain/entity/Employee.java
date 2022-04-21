package com.example.EmployeeService.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//@Entity
//@Table(name = "Employee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Employee {

    // Database table
    @Id
    private String id;

    private Integer userID;

    private String firstName;

    private String lastName;

    private String middleName;

    private String preferedName;

    private String email;

    private String cellPhone;

    private String alternatePhone;

    private String gender;

    private String SSN;

    private String DOB;

    private String startDate;

    private String endDate;

    private String driverLicense;

    private String driverLicenseExpiration;

    private Integer houseId;

    private List<Contact> contact;

    private List<Address> addresses;

    private List<VisaStatus> visaStatuses;

    private List<PersonalDocument> personalDocuments;

}
