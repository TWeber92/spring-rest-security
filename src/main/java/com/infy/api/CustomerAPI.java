package com.infy.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.dto.CustomerDTO;
import com.infy.exception.InfyBankException;
import com.infy.service.CustomerService;

@RestControllerAdvice
@RequestMapping("/infybank")
@Validated
public class CustomerAPI {

	@Autowired
	CustomerService customerService;
	@Autowired
	Environment environment;

	@GetMapping("/customers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws InfyBankException {
		List<CustomerDTO> customers = customerService.getAllCustomers();
		return new ResponseEntity<List<CustomerDTO>>(customers, HttpStatus.OK);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomer(
			@PathVariable @Min(value = 1, message = "{customer.customerid.invalid}") 
								 @Max(value = 100, message = "{customer.customerid.invalid}") Integer customerId)
			throws InfyBankException {
		CustomerDTO customer = customerService.getCustomer(customerId);
		return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);
	}
	
	@PostMapping("/customers")
	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws InfyBankException{
		Integer customerId = customerService.addCustomer(customerDTO);
		String successMsg = environment.getProperty("API.INSERT_SUCCESS") + customerId;
		return new ResponseEntity<String>(successMsg, HttpStatus.CREATED);
	}
	
	@PutMapping("/customer/{customerId}")
	public ResponseEntity<String> updateCustomer(Integer customerId, String emailId) throws InfyBankException{
		customerService.updateCustomer(customerId, emailId);
		String successMsg = environment.getProperty("API.UPDATE_SUCCESS");
		return new ResponseEntity<String>(successMsg, HttpStatus.OK);
	}
	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<String> deleteCustomer(Integer customer) throws InfyBankException{
		customerService.deleteCustomer(customer);
		String successMsg = environment.getProperty("API.DELETE_SUCCESS");
		return new ResponseEntity<String>(successMsg, HttpStatus.OK);
	}
}
