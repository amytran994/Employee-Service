package com.example.EmployeeService.dao;

import com.example.EmployeeService.domain.entity.Employee;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String>{

    // save or update Employee info
    Employee save(Employee employee);

    // find all employee
    Page<Employee> findAll(Pageable page);

    Slice<Employee> findByFirstName(String firstName, Pageable page);
    Slice<Employee> findByLastName(String lastName, Pageable page);

    Employee findById(ObjectId id);
    Employee findByUserIDAndEmail(int userId, String email);
    Employee findByUserID(int userId);


}
