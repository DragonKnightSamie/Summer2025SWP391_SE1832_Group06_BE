package com.gender_healthcare_system.iservices;

import com.gender_healthcare_system.dtos.LoginResponse;

public interface ICustomerService {
    public LoginResponse getCustomerLoginDetails(int id); //lấy thông tin đăng nhập của customer theo id
}
