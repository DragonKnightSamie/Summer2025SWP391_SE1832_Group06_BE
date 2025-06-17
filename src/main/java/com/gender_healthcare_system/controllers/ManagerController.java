package com.gender_healthcare_system.controllers;

import com.cloudinary.Cloudinary;
import com.gender_healthcare_system.dtos.login.LoginResponse;
import com.gender_healthcare_system.dtos.todo.*;
import com.gender_healthcare_system.dtos.user.StaffDTO;
import com.gender_healthcare_system.entities.enu.AccountStatus;
import com.gender_healthcare_system.entities.user.AccountInfoDetails;
import com.gender_healthcare_system.payloads.login.LoginRequest;
import com.gender_healthcare_system.payloads.todo.*;
import com.gender_healthcare_system.payloads.user.ConsultantRegisterPayload;
import com.gender_healthcare_system.payloads.user.ManagerRegisterPayload;
import com.gender_healthcare_system.payloads.user.StaffRegisterPayload;
import com.gender_healthcare_system.payloads.user.StaffUpdatePayload;
import com.gender_healthcare_system.services.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@AllArgsConstructor
public class ManagerController {

    private final ImageUploadService imageUploadService; //Cloudinary image upload service

    private final BlogService blogService;

    private final ManagerService managerService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;

    private final StaffService staffService;

    private final CustomerService customerService;

    private final ConsultantService consultantService;

    private final TestingServiceFormService testingServiceFormService;

    private final TestingServiceTypeService testingServiceTypeService;

    private final TestingServiceResultService testingServiceResultService;

    private final TestingService_Service testingService_Service;

    private final PriceListService priceListService;

    //test
    //Manager registration
    @PostMapping("/register")
    public String register(@RequestBody ManagerRegisterPayload payload) {
        accountService.createManagerAccount(payload);
        return "Manager registered successfully";
    }

    //Manager login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid Username or Password");
        }

        boolean hasRole = authentication
                .getAuthorities()
                .stream()
                .anyMatch(x -> x
                        .getAuthority().equals("ROLE_MANAGER"));

        if (!hasRole) {
            throw new UsernameNotFoundException
                    ("Access denied for non-manager user");
        }

        AccountInfoDetails account =
                (AccountInfoDetails) authentication.getPrincipal();
        int id = account.getId();

        LoginResponse loginDetails = managerService
                .getManagerLoginDetails(id);
        loginDetails.setUsername(loginRequest.getUsername());

        String jwtToken = jwtService.generateToken(loginRequest.getUsername());
        loginDetails.setToken(jwtToken);

        return loginDetails;
        //return jwtService.generateToken(loginRequest.getUsername());

    }


    /// //////////////////////// Manage Blogs /////////////////////////////////////


    //getAllBlogs
    @GetMapping("/blogs/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> getAllBlogs
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(blogService.getAllBlogs(page, sort, order));
    }

    //getBlogsById
    @GetMapping("/blogs/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable int id) {

        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    //searchBlogs
    @GetMapping("/blogs/search")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> searchBlogs
    (@RequestParam String keyword,
     @RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "blogId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(blogService.searchBlogs(keyword, page, sort, order));
    }

    //MANAGER CREATE BLOGS
    @PostMapping("/blogs/create")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> createBlog(@RequestBody BlogRegisterPayload payload) {

        blogService.createBlog(payload);
        return ResponseEntity.ok("Blog created successfully");
    }

    //MANGER UPDATE BLOGS
    @PutMapping("/blogs/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateBlog
    (@PathVariable int id, @RequestBody BlogUpdatePayload payload) {

        blogService.updateBlog(id, payload);
        return ResponseEntity.ok("Blog updated successfully");
    }

    //MANGER DELETE BLOGS
    @DeleteMapping("/blogs/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteBlog(@PathVariable int id) {

        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted successfully");
    }


    /// //////////////////////// Manage Consultants /////////////////////////////////////


    //Manager get all Consultants
    @GetMapping("/consultants/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> getAllConsultants
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "managerId") String sort,
     @RequestParam(defaultValue = "asc") String order) {

        return ResponseEntity.ok(consultantService.getAllConsultants(page, sort, order));
    }

    //Manager get Consultant details by id
    @GetMapping("/consultants/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ConsultantDetailsDTO>
    getConsultantDetailsById(@PathVariable int id) {

        ConsultantDetailsDTO consultantDetails = consultantService.getConsultantDetails(id);
        if (consultantDetails != null) {
            return ResponseEntity.ok(consultantDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Manager create a new Consultant
    @PostMapping("/consultants/register")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> createConsultantAccount
    (@RequestBody ConsultantRegisterPayload payload) {

        accountService.createConsultantAccount(payload);
        return ResponseEntity.ok("Staff account created successfully");
    }

    //Manager update Consultant status
    @PostMapping("/consultants/update_status/{consultantId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateConsultantStatus
    (@PathVariable int consultantId, @RequestParam AccountStatus status) {

        accountService.updateCustomerStatus(consultantId, status);
        return ResponseEntity.ok("Consultant account status updated successfully");
    }

    //Manager delete Consultant from system
    @DeleteMapping("/consultants/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteConsultantAccount(@PathVariable int id) {

        accountService.deleteConsultantById(id);
        return ResponseEntity.ok("Consultant account deleted successfully");
    }


    /// //////////////////////// Manage Staffs /////////////////////////////////////


    //Manager get all Staffs
    @GetMapping("/staffs/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> getAllStaffs
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "staffId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(staffService.getAllStaffs(page, sort, order));
    }

    //Manager get staff details by id
    @GetMapping("/staffs/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable int id) {
        StaffDTO staffDetails = staffService.getStaffById(id);
        if (staffDetails != null) {
            return ResponseEntity.ok(staffDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //MANAGER CREATE STAFF ACCOUNT
    @PostMapping("/staffs/register")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> createStaffAccount(@RequestBody StaffRegisterPayload payload) {
        accountService.createStaffAccount(payload);
        return ResponseEntity.ok("Staff account created successfully");
    }

    //Manager update Staff infos
    @PutMapping("/staffs/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateStaffAccount
    (@PathVariable int id, @RequestBody StaffUpdatePayload payload) {

        staffService.updateStaffAccount(id, payload);
        return ResponseEntity.ok("Staff details updated successfully");
    }

    //Manager delete staff from system
    @DeleteMapping("/staffs/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteStaffAccount(@PathVariable int id) {

        accountService.deleteStaffById(id);
        return ResponseEntity.ok("Staff account deleted successfully");
    }


    /// //////////////////////// Manage Customer /////////////////////////////////////


    //Get all Customers
    @GetMapping("/customers/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Map<String, Object>> getAllCustomers
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "customerId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return ResponseEntity.ok(customerService.getAllCustomers(page, sort, order));
    }

    //Get customer details by id
    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<ManagerCustomerDTO> getCustomerById(@PathVariable int id) {
        ManagerCustomerDTO customerDetails = customerService.getCustomerById(id);
        if (customerDetails != null) {
            return ResponseEntity.ok(customerDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Manager change customer status
    @PostMapping("/customers/update_status/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> updateCustomerStatus
    (@PathVariable int customerId, @RequestParam AccountStatus status) {

        accountService.updateCustomerStatus(customerId, status);
        return ResponseEntity.ok("Customer account status updated successfully");
    }

    //Manager delete customer from system
    @DeleteMapping("/customers/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteCustomerAccount(@PathVariable int id) {

        accountService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer account deleted successfully");
    }

    /// //////////////////////// Manage Testing Service Type /////////////////////////////////////


    //get testing service type by ID
    @GetMapping("/testing-service-types/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public TestingServiceDTO getTestingServiceTypeById(@PathVariable int id) {
        return testingService_Service.getTestingServiceById(id);
    }

    //get all testing service types
    @GetMapping("/testing-services-types/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Map<String, Object> getAllTestingServiceTypes
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "serviceTypeId") String sort,
     @RequestParam(defaultValue = "asc") String order) {
        return testingServiceTypeService.getAllTestingServiceTypes(page, sort, order);
    }

    //create new testing service type
    @PostMapping("/testing-services-types/create")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String createTestingServiceType
    (@RequestBody TestingServiceTypeRegisterPayload payload) {
        testingServiceTypeService.createTestingServiceType(payload);
        return "Testing Service type created successfully";
    }

    //update testing service type
    @PutMapping("/testing-services-types/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String updateTestingServiceType
    (@PathVariable int id, @RequestBody TestingServiceTypeUpdatePayload payload) {

        testingServiceTypeService.updateTestingServiceType(id, payload);
        return "Testing Service type updated successfully";
    }

    //delete testing service type
    @DeleteMapping("/testing-services-types/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String deleteTestingServiceType(@PathVariable int id) {

        testingService_Service.deleteTestingService(id);
        return "Testing Service type deleted successfully";
    }


    /// //////////////////////// Manage Testing Service Results /////////////////////////////////////


    //update testing service result
    @PutMapping("/testing-services-results/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String updateTestingServiceResult
    (@PathVariable int id, @RequestBody TestingServiceResultPayload payload) {

        testingServiceResultService.updateTestingServiceResult(id, payload);
        return "Testing Service result updated successfully";
    }

    //delete testing service result
    @DeleteMapping("/testing-services-results/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String deleteTestingServiceResult(@PathVariable int id) {

        testingService_Service.deleteTestingService(id);
        return "Testing Service result deleted successfully";
    }


    /// //////////////////////// Manage Testing Services /////////////////////////////////////


    //get testing service by ID
    @GetMapping("/testing-services/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public TestingServiceDTO getTestingServiceById(@PathVariable int id) {
        return testingService_Service.getTestingServiceById(id);
    }

    //get all testing services
    @GetMapping("/testing-services/")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Map<String, Object> getAllTestingServices
    (@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "serviceId") String sort,
     @RequestParam(defaultValue = "asc") String order) {

        return testingService_Service.getAllTestingServices(page, sort, order);
    }

    //create new testing service
    @PostMapping("/testing-services/create")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String createTestingService
    (@RequestBody TestingServiceRegisterPayload payload) {
        testingService_Service.createTestingService(payload);
        return "Testing Service created successfully";
    }

    //Create new price list for existing testing service
    @PostMapping("/testing-services/price-lists/create/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void createTestingServicePriceList(@PathVariable int id,
                                              @RequestBody PriceListPayload payload) {
        priceListService.createNewPriceListForExistingService(id, payload);
    }

    //update testing service
    @PutMapping("/testing-services/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public String updateTestingService
    (@PathVariable int id, @RequestBody TestingServiceUpdatePayload payload) {

        testingService_Service.updateTestingService(id, payload);
        return "Testing Service updated successfully";
    }

    //delete testing service
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/testing-services/{id}")
    public String deleteTestingService(@PathVariable int id) {

        testingService_Service.deleteTestingService(id);
        return "Testing Service deleted successfully";
    }

    /// //////////////////////////// Testing Service Form Operations ///////////////////////////////


    //update testing service form by ID
    @PutMapping("/testing-service-forms/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void updateTestingServiceFormById
    (@PathVariable int id, @RequestBody @NotBlank
    @Length(min = 5, max = 255, message = "Content must be between 5 and 255 characters")
    String newContent) {
        testingServiceFormService.updateTestingServiceFormById(id, newContent);
    }

    //delete testing service form by ID
    @DeleteMapping("/testing-service-forms/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void deleteTestingServiceFormById(@PathVariable int id) {
        testingServiceFormService.deleteTestingServiceFormById(id);
    }

    /// //////////////////////////// Price List Operations ///////////////////////////////


    //update price list by ID
    @PutMapping("/price-lists/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void updatePriceList(@PathVariable int id,
                                @RequestBody PriceListPayload payload) {
        priceListService.updatePriceList(id, payload);
    }

    //delete price list by ID
    @DeleteMapping("/price-lists/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")

    public void deletePriceList(@PathVariable int id) {
        priceListService.deletePriceList(id);
    }


    /// //////////////////////// API UPLOAD IMAGE /////////////////////////////////////
    private final Cloudinary cloudinary;

    @PostMapping(value = "/image/upload", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ImageUploadDTO uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder) throws IOException {
        // Upload image to Cloudinary
        return imageUploadService.uploadImage(file, folder);
    }

    @PutMapping(value = "/image/replace", consumes = "multipart/form-data")
    public ImageUploadDTO replaceImage(@RequestParam("file") MultipartFile file,
                                       @RequestParam("folder") String folder,
                                       @RequestParam("id") String publicId) throws IOException {
        return imageUploadService.replaceImage(file, folder, publicId);
    }

    @DeleteMapping("/image/delete")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> deleteImage(@RequestParam("id") String publicId) throws IOException {
        boolean isDelete = imageUploadService.deleteImage(publicId);
        return isDelete ? ResponseEntity.ok("Deleted") : ResponseEntity.status(404).body("Not found");
    }

}

