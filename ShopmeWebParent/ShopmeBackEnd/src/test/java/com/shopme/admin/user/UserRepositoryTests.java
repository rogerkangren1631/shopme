package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userRoger = new User("rogerkangren@gmail.com", "wrongpassword", "Roger", "Ren");
		userRoger.addRole(roleAdmin);

		User savedUser = repo.save(userRoger);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUserWithTwoRole() {
		User userJuan = new User("jinjuan@gmail.com", "wrongpassword", "Juan", "Jin");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		userJuan.addRole(roleEditor);
		userJuan.addRole(roleAssistant);

		User savedUser = repo.save(userJuan);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		System.out.println("-----------------------------------------");

		listUsers.forEach(user -> System.out.println(user));
		System.out.println("________________________________________");
	}

	@Test
	public void testGetUserById() {
		System.out.println("-----------------------------------------");
		User userJuan = repo.findById(2).get();

		System.out.println(userJuan);
		System.out.println("________________________________________");
		assertThat(userJuan).isNotNull();
	}

	@Test
	public void testUpdateUserDetails() {
		System.out.println("-----------------------------------------");
		String newLastName = "Ren";
		String newFirstName = "Juan";
		User userJuan = repo.findById(2).get();
		userJuan.setFirstName(newFirstName);
		userJuan.setLastName(newLastName);

		repo.save(userJuan);

		System.out.println(userJuan);
		System.out.println("________________________________________");
		assertThat(userJuan).isNotNull();

		User updatedUser = repo.findById(2).get();
		assertTrue(updatedUser.getFirstName().equals(newFirstName));
		assertTrue(updatedUser.getLastName().equals(newLastName));
		System.out.println("________________________________________");
	}

	@Test
	public void testUpdatUserRoles() {
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);

		System.out.println("-----------------------------------------");
		User userJuan = repo.findById(2).get();
		userJuan.getRoles().remove(roleEditor);
		userJuan.addRole(roleSalesperson);

		repo.save(userJuan);

		System.out.println(userJuan);
		System.out.println("________________________________________");

	}

	@Test
	public void testDeleteUser() {

		Integer userId = 2;

		repo.deleteById(userId);
		Optional<User> deletedUser = repo.findById(userId);
		assertFalse(deletedUser.isPresent());

	}

	@Test
	public void testGetUserByEmail() {
		String email = "     rogerkangren@gmail.com     ";
		email = email.trim();
		User user = repo.getUserByEmail(email);
		assertTrue(user == null);
	}

	@Test
	public void testCountById() {

		Integer id = 1;

		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser() {
		Integer id = 12;
		repo.updateEnabledStatus(id, false);

	}

	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 6;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);

	}
	
	@Test 
	public void testSeachKeyWord() {
		int pageNumber = 0;
		int pageSize = 6;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		 String keyWord ="bruce";
		 
		 Page<User> page = repo.findAll(keyWord, pageable); 
			
		 List<User> listUsers = page.getContent();
			
		 listUsers.forEach(user -> System.out.println(user));

			assertThat(listUsers.size()).isGreaterThan(0);
			
	
	
	}
}
