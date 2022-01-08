package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@MockBean
	private CategoryRepository repo;

	@InjectMocks
	private CategoryService service;

	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null;
		String name = "Computers";
		String alias = "abc";

		Category category = new Category(id, name, alias);
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		System.out.println("******************************");
		System.out.println("Category's name = " + category.getName());
		System.out.println("Category's ID = " + category.getId());

		System.out.println("Category's alias = " + category.getAlias());
		System.out.println("******************************");

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id = null;
		String name = "NameABC";
		String alias = "computers";

		Category category = new Category(id, name, alias);
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		System.out.println("******************************");
		System.out.println("Category's name = " + category.getName());
		System.out.println("Category's ID = " + category.getId());

		System.out.println("Category's alias = " + category.getAlias());
		System.out.println("******************************");

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "NameABC";
		String alias = "NotExist";

	
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);


		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("OK");
	}
	
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "abc";

		Category category = new Category(2, name, "another thaing");
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id = 10;
		String name = "NameABC";
		String alias = "computers";

		Category category = new Category(1, "Computers", alias);
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		System.out.println("******************************");
		System.out.println("Category's name = " + category.getName());
		System.out.println("Category's ID = " + category.getId());

		System.out.println("Category's alias = " + category.getAlias());
		System.out.println("******************************");

		String result = service.checkUnique(id, name, alias);

		assertThat(result).isEqualTo("DuplicateAlias");
	//	assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "NameABC";
		String alias = "computers";

		Category category = new Category(1, "Computers", alias);
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(category);

		

		String result = service.checkUnique(id, name, alias);

	//	assertThat(result).isEqualTo("DuplicateAlias");
		assertThat(result).isEqualTo("OK");
	}
}
