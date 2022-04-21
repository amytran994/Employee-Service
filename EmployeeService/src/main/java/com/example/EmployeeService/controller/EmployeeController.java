package com.example.EmployeeService.controller;

import com.example.EmployeeService.domain.entity.Employee;
import com.example.EmployeeService.domain.entity.PersonalDocument;
import com.example.EmployeeService.domain.reponse.EmployeeIdName;
import com.example.EmployeeService.security.AuthUserDetail;
import com.example.EmployeeService.security.JwtProvider;
import com.example.EmployeeService.service.EmployeeService;
import com.example.EmployeeService.service.S3BucketStorageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;
    private JwtProvider jwtProvider;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Autowired
    S3BucketStorageService s3BucketStorageService;

    /* For HR */

    @GetMapping("employee/all")
    public ResponseEntity getAllEmployeeProfile(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        return new ResponseEntity(employeeService.findAll(page, size), HttpStatus.OK);
    }

    @GetMapping("employee/firstname")
    public ResponseEntity getAllEmployeeProfileByFirstName(@RequestParam("firstname") String firstname,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        return
        new ResponseEntity(employeeService.findByFirstName(page, size, firstname),
        HttpStatus.OK);
    }

    @GetMapping("employee/lastname")
    public ResponseEntity getAllEmployeeProfileByLastName(@RequestParam("lastname") String lastname,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        return
        new ResponseEntity(employeeService.findByLastName(page, size, lastname),
                HttpStatus.OK);
    }

    @PostMapping("employee/new")
    public ResponseEntity addNewEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        // assume that one user has one employee account
        Optional<AuthUserDetail> authUserDetailOptional = jwtProvider.resolveToken(request);
        if (authUserDetailOptional.isPresent()) {
            AuthUserDetail authUserDetail = authUserDetailOptional.get();
            employee.setUserID(authUserDetail.getUserId());
            employee.setEmail(authUserDetail.getEmail());

            if (employeeService.employeeExists(employee.getUserID(), employee.getEmail())) {
                return new ResponseEntity("User already has Employee Account", HttpStatus.OK);
            }
            return new ResponseEntity(employeeService.save(employee), HttpStatus.OK);
        }
        return new ResponseEntity("Please log in", HttpStatus.OK);

    }


    @PostMapping("employee/{employeeId}")
    public ResponseEntity updateEmployeeProfile(HttpServletRequest request,
        @PathVariable String employeeId, @RequestBody Employee employee) {
        employee.setId(employeeId);

        return new ResponseEntity(employeeService.save(employee), HttpStatus.OK);
    }

    @GetMapping("employee/{employeeId}")
    public ResponseEntity getEmployeeProfile(@PathVariable ObjectId employeeId) {

        return new ResponseEntity(employeeService.findById(employeeId), HttpStatus.OK);
    }

    @GetMapping("employee/{employeeId}/documents")
    public ResponseEntity getEmployeeDocuments(@PathVariable ObjectId employeeId) {

        return new ResponseEntity(employeeService.getAllDocuments(employeeId), HttpStatus.OK);
    }

    @PostMapping("employee/{employeeId}/documents/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileName") String fileName,
                                             @RequestParam("file") MultipartFile file,
                                             @PathVariable ObjectId employeeId) {

        s3BucketStorageService.uploadFile(employeeId.toString()+fileName, file);

        return new ResponseEntity(employeeService.uploadDocuments(employeeId, fileName), HttpStatus.OK);
    }

    @GetMapping("employee/{employeeId}/documents/download/{id}")
    public ResponseEntity<byte[]> downloadById(@PathVariable String employeeId, @PathVariable Integer id) {
        PersonalDocument personalDocument = employeeService.downloadDocument(employeeId, id);
        if (personalDocument == null) {
            return new ResponseEntity("Can not find document", HttpStatus.OK);
        }
        String filename = personalDocument.getPath();
        ByteArrayOutputStream downloadInputStream = s3BucketStorageService.downloadFile(filename);

        return ResponseEntity.ok()
                .contentType(contentType(filename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(downloadInputStream.toByteArray());
    }

    // for composite service
    @GetMapping("employee/employeeNameId/{userId}")
    public EmployeeIdName getEmployeeNameId(@PathVariable Integer userId) {
        Employee employee = employeeService.getEmployeeNameId(userId);
        return EmployeeIdName.builder().id(employee.getId())
        .name(employee.getFirstName() + " " + employee.getLastName()).build();

    }
    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }



}
