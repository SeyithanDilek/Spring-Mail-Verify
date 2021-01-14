package com.seyithandilek.springemailverify.repository;

import org.springframework.data.repository.CrudRepository;

import com.seyithandilek.springemailverify.model.ConfirmationToken;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
	
    ConfirmationToken findByConfirmationToken(String confirmationToken);

}
