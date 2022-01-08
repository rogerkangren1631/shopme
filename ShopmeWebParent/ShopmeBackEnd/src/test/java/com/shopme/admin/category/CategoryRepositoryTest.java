package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronic");
		Category savedCategory =repo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
	   Category parent = new Category(5);
	   Category subCategory =  new Category("Memory", parent);
	  
	   
	   Category savedCategory = repo.save( subCategory );
	   assertThat(savedCategory.getId()).isGreaterThan(0);
	
	
	}
	
	@Test
	public void testGetCategory() {
	
		Category category = repo.findById(2).get();
		System.out.println(category.getName());
		
		Set<Category> children =category.getChildren();
		
		for(Category subCategory : children) {
			
			System.out.println(subCategory.getName());
		}
		
		assertThat(children.size()).isGreaterThan(0);
	}

	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repo.findAll();
		for(Category category :categories) {		
			if(category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<Category> children =category.getChildren();
				for(Category  subCategory : children) {
					System.out.println("--" + subCategory.getName());
				}
			}
					
		}
		
	}
	
	@Test
	public void testListRootCategories() {
		List<Category> listRoots = repo.findRootCategories(Sort.by("name").ascending());
		
		for (Category cat : listRoots) {
			System.out.println("->" + cat.getName());
		}
		
		
	}
	
	@Test
	public void testFindByName() {
		  String testName = "Computers";
		  
		  Category category = repo.findByName(testName);
		  
		  assertThat(category).isNotNull();
		  
		  String categoryName = category.getName();
		  assertThat(categoryName).isEqualTo(testName); 
				  
	}
	
	@Test
	public void testFindByAlias() {
		  String testName = "Memory";
		  
		  Category category = repo.findByAlias(testName);
		  
		  assertThat(category).isNotNull();
		  
		  String categoryAlias = category.getAlias();
		  assertThat(categoryAlias).isEqualTo(testName); 
				  
	}
	
}
