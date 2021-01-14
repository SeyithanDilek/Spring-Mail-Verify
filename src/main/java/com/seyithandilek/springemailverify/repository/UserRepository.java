package com.seyithandilek.springemailverify.repository;
import org.springframework.data.repository.CrudRepository;
import com.seyithandilek.springemailverify.model.User;
public interface UserRepository extends CrudRepository<User, String> {
	
	User findByEmailIdIgnoreCase(String emailId);
}
