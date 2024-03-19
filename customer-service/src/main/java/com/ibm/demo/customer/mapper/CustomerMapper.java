package com.ibm.demo.customer.mapper;


import com.ibm.demo.customer.dto.CustomerDto;
import com.ibm.demo.customer.entity.Customer;

public class CustomerMapper {
    public static CustomerDto mapToCustomerDto(Customer customer){
        CustomerDto customerDto = new CustomerDto(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail()
              //  employee.getDepartmentCode()
                //   employee.getOrganizationCode()
        );
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto){
        Customer employee = new Customer(
                customerDto.getId(),
                customerDto.getFirstname(),
                customerDto.getLastname(),
                customerDto.getEmail()
               // employeeDto.getDepartmentCode()
                // employeeDto.getOrganizationCode()
        );
        return employee;
    }
}
