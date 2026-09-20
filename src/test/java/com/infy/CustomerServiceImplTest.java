package com.infy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.infy.dto.CustomerDTO;
import com.infy.entity.Customer;
import com.infy.exception.InfyBankException;
import com.infy.repository.CustomerRepository;
import com.infy.service.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer createCustomer(Integer id, String email, String name, LocalDate dob) {
        Customer c = new Customer();
        c.setCustomerId(id);
        c.setEmailId(email);
        c.setName(name);
        c.setDateOfBirth(dob);
        return c;
    }

    @Test
    void getAllCustomers_returnsListOfDTOs() throws Exception {
        Customer c1 = createCustomer(1, "charles@example.com", "Charles Smith", LocalDate.of(1985, 4, 12));
        Customer c2 = createCustomer(2, "jane@example.com", "Jane Doe", LocalDate.of(1990, 7, 23));
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CustomerDTO> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getCustomerId());
        assertEquals("charles@example.com", result.get(0).getEmailId());
        assertEquals("Jane Doe", result.get(1).getName());
    }

    @Test
    void getAllCustomers_emptyList_throwsException() {
        when(customerRepository.findAll()).thenReturn(List.of());

        InfyBankException ex = assertThrows(InfyBankException.class, () -> customerService.getAllCustomers());
        assertEquals("Service.CUSTOMERS_NOT_FOUND", ex.getMessage());
    }

    @Test
    void getCustomer_existingId_returnsDTO() throws Exception {
        Customer c = createCustomer(1, "charles@example.com", "Charles Smith", LocalDate.of(1985, 4, 12));
        when(customerRepository.findById(1)).thenReturn(Optional.of(c));

        CustomerDTO result = customerService.getCustomer(1);

        assertNotNull(result);
        assertEquals(1, result.getCustomerId());
        assertEquals("charles@example.com", result.getEmailId());
        assertEquals("Charles Smith", result.getName());
    }

    @Test
    void getCustomer_nonExistentId_throwsException() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        InfyBankException ex = assertThrows(InfyBankException.class, () -> customerService.getCustomer(999));
        assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
    }

    @Test
    void addCustomer_returnsGeneratedId() throws Exception {
        CustomerDTO dto = new CustomerDTO();
        dto.setEmailId("new@example.com");
        dto.setName("New Customer");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));

        Customer saved = createCustomer(50, "new@example.com", "New Customer", LocalDate.of(2000, 1, 1));
        when(customerRepository.save(any(Customer.class))).thenReturn(saved);

        Integer result = customerService.addCustomer(dto);

        assertEquals(50, result);
    }

    @Test
    void updateCustomer_existingId_updatesEmail() throws Exception {
        Customer c = createCustomer(1, "old@example.com", "Charles Smith", LocalDate.of(1985, 4, 12));
        when(customerRepository.findById(1)).thenReturn(Optional.of(c));

        customerService.updateCustomer(1, "new@example.com");

        assertEquals("new@example.com", c.getEmailId()); // mutated in place
    }

    @Test
    void updateCustomer_nonExistentId_throwsException() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        InfyBankException ex = assertThrows(InfyBankException.class,
                () -> customerService.updateCustomer(999, "x@example.com"));
        assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
    }

    @Test
    void deleteCustomer_existingId_callsDeleteById() throws Exception {
        Customer c = createCustomer(1, "charles@example.com", "Charles Smith", LocalDate.of(1985, 4, 12));
        when(customerRepository.findById(1)).thenReturn(Optional.of(c));

        customerService.deleteCustomer(1);

        verify(customerRepository).deleteById(1);
    }

    @Test
    void deleteCustomer_nonExistentId_throwsException() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        InfyBankException ex = assertThrows(InfyBankException.class, () -> customerService.deleteCustomer(999));
        assertEquals("Service.CUSTOMER_NOT_FOUND", ex.getMessage());
        verify(customerRepository, never()).deleteById(anyInt());
    }
}