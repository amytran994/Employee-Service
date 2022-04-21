package com.example.EmployeeService.domain.reponse;

import com.example.EmployeeService.domain.entity.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllEmployeeResponse {
    List<Employee> employees;
}
