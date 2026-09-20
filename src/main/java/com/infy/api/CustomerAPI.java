package com.infy.api;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.CustomerDTO;
import com.infy.exception.InfyBankException;
import com.infy.service.CustomerService;

@RestController
@RequestMapping("/infybank")
@Validated
public class CustomerAPI {

	CustomerAPI(Environment environment, CustomerService customerService) {
		this.environment = environment;
		this.customerService = customerService;
	}

	private final CustomerService customerService;
	private final Environment environment;

	@GetMapping("/customers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() throws InfyBankException {
		List<CustomerDTO> customers = customerService.getAllCustomers();
		return new ResponseEntity<List<CustomerDTO>>(customers, HttpStatus.OK);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomer(
			@PathVariable @Min(value = 1, message = "{customer.customerid.invalid}") @Max(value = 100, message = "{customer.customerid.invalid}") Integer customerId)
			throws InfyBankException {
		CustomerDTO customer = customerService.getCustomer(customerId);
		return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);
	}

	@PostMapping("/customers")
	public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws InfyBankException {
		Integer customerId = customerService.addCustomer(customerDTO);
		String successMsg = environment.getProperty("API.INSERT_SUCCESS") + customerId;
		return new ResponseEntity<String>(successMsg, HttpStatus.CREATED);
	}

	@PutMapping("/customer/{customerId}")
	public ResponseEntity<String> updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerDTO customerDTO)
			throws InfyBankException {
		customerService.updateCustomer(customerId, customerDTO.getEmailId());
		String successMsg = environment.getProperty("API.UPDATE_SUCCESS");
		return new ResponseEntity<String>(successMsg, HttpStatus.OK);
	}

	@DeleteMapping("/customer/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Integer customerId) throws InfyBankException {
		customerService.deleteCustomer(customerId);
		String successMsg = environment.getProperty("API.DELETE_SUCCESS");
		return new ResponseEntity<String>(successMsg, HttpStatus.OK);
	}
}
