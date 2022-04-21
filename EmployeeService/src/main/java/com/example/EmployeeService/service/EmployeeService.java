package com.example.EmployeeService.service;

import com.example.EmployeeService.dao.EmployeeRepository;
import com.example.EmployeeService.domain.entity.Employee;
import com.example.EmployeeService.domain.entity.PersonalDocument;
import com.example.EmployeeService.domain.reponse.AllEmployeeResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    // save or update Employee info
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    // find all employee
    public AllEmployeeResponse findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Employee> employeeSlice = employeeRepository.findAll(pageable);

        return AllEmployeeResponse.builder().employees(employeeSlice.toList())
        .build();
    }

    public AllEmployeeResponse findByLastName(int page, int size, String lastName) {
        Pageable pageable = PageRequest.of(page, size);

        return AllEmployeeResponse.builder()
        .employees(employeeRepository.findByLastName(lastName, pageable).toList())
                .build();
    }

    public AllEmployeeResponse findByFirstName(int page, int size, String firstName) {
        Pageable pageable = PageRequest.of(page, size);

        return AllEmployeeResponse.builder()
            .employees(employeeRepository.findByFirstName(firstName, pageable).toList())
                .build();
    }


    public Employee findById(ObjectId id) {
        return employeeRepository.findById(id);
    }

    public List<PersonalDocument> uploadDocuments(ObjectId employeeId, String fileName) {

        Employee employee = employeeRepository.findById(employeeId);
        employee.setId(employeeId.toString());

        String[] fileArrSplit = fileName.split("\\.");
        String type = fileArrSplit[fileArrSplit.length - 1];
        String path = employeeId.toString()+fileName;
        String description = "";
        String title = "";

        List<PersonalDocument> docList = employee.getPersonalDocuments();

        if (docList == null) {
            docList = new ArrayList<>();
        }

        Integer docId = docList.size() + 1;

        PersonalDocument doc = PersonalDocument.builder()
                .id(docId.toString())
                .path(path)
                .createDate(LocalDate.now().toString())
                .comment(fileName)
                .build();

        docList.add(doc);
        employee.setPersonalDocuments(docList);
        Employee updatedEmployee = employeeRepository.save(employee);

        return updatedEmployee.getPersonalDocuments();
    }

    public List<PersonalDocument> getAllDocuments(ObjectId employeeId) {
        return employeeRepository.findById(employeeId).getPersonalDocuments();
    }

    public PersonalDocument downloadDocument(String employeeId, Integer id) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            return employee.get().getPersonalDocuments().get(id-1);
        }
        return null;
    }

    public boolean employeeExists(int userId, String email) {
        return employeeRepository.findByUserIDAndEmail(userId, email) != null;
    }

    public Employee getEmployeeNameId(Integer userId) {
        return employeeRepository.findByUserID(userId);
    }


}
