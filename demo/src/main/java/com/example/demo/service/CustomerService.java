package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.dto.CustomerDto;
import com.example.demo.service.dto.DtoMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    //todo admin only
    public CustomerDto save(CustomerDto customerDto) {
        Customer customer = DtoMapping.dtoToCustomer(customerDto);
        customer = customerRepository.save(customer);
        return DtoMapping.customerToDto(customer);
    }

    //todo admin only
    public void deactivateCustomer(CustomerDto customerDto) {
        customerRepository.findById(customerDto.getId()).ifPresent(customer -> {
            customer.setStatus(false);
            customerRepository.save(customer);
        });
    }

}
