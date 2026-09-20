package com.infy.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public class CustomerDTO {
	private Integer customerId;
	@Email(message = "{customer.email.invalid}")
	@NotNull(message = "{customer.email.absent}")
	private String emailId;
	@NotNull(message = "{customer.name.absent}")
	@Pattern(regexp = "^[A-Za-z]+( [A-Za-z]+)*$", message = "{customer.name.invalid}")
	private String name;
	@PastOrPresent(message = "{customer.dob.invalid}")
	private LocalDate dateOfBirth;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "CustomerDTO [customerId=" + customerId + ", emailId=" + emailId + ", name=" + name + ", dateOfBirth="
				+ dateOfBirth + "]";
	}
}
