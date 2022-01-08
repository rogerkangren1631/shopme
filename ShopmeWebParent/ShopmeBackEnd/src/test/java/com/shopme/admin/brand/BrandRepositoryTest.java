package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BrandRepositoryTest {
	
	@Autowired
	private BrandRepository repo;
	
	@Autowired
	private CategoryRepository catRepo;
	
	@Autowired
	private TestEntityManager entityManager; 
	
	@Test
	public void testCreateBrandWithOneCategory() {
		Category laptopsCategory = entityManager.find(Category.class,  6);
		
		Brand acer = new Brand( "Acer");
		if( laptopsCategory != null) {
		    acer.addCategory(laptopsCategory);
		    
		    Brand savedBrand = repo.save(  acer );
		    
		    assertThat(savedBrand.getId()).isGreaterThan(0);
		    
		  }else
		  {
			  System.out.println("Can not find laptops category");
		  }
		
	}
	
	@Test
	public void testCreateBrandWithTwoCategory() {
		Brand dellBrand = new Brand( "Dell");
		
		Category internalHD = entityManager.find(Category.class,  24);
		if( internalHD !=null ) {
			dellBrand.addCategory(internalHD);
		}
		
		Category memory = catRepo.findByName("Memory");
		if(memory != null) {
			dellBrand.addCategory(memory);
		}
		
		Brand savedBrand = repo.save(dellBrand);
		
	    assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test 
	public void testPrintAllBrand() {
	     Iterable<Brand> listBrands = repo.findAll();
	     
	     
	     System.out.println(" -************************* " );
	     for( Brand brand : listBrands ) {
	    	 
	    	 System.out.println("Brand Name: " + brand.getName());
	    	 
	    	 brand.getCategories().forEach( cat -> System.out.println(" ->Category name: " + cat.getName() + ", which ID: " + cat.getId() ) );
	    	 
	    	 
	     }
	     
	     System.out.println(" -************************* " );
		
	}
	
	@Test 
	public void testFindBrandByID() {
		
		Optional<Brand> apple = repo.findById(2);
		assertThat(apple.isPresent()).isTrue();
		
	}
	
	@Test 
	public void testUpdateName() {
		
		Optional<Brand> samsung = repo.findById(3);
		
		if(samsung.isPresent()) {
			Brand samsungE = samsung.get();
			samsungE.setName("Samsung Electronics");
			 Brand updatedBrand = repo.save(samsungE);
			 
			 assertThat( updatedBrand != null).isTrue();			
		}
		
	}
	
	@Test
	public void testDeleteBrand() {
		Brand apple = repo.findById(2).get();	
		repo.delete(apple);		
		assertThat (repo.findById(2).isPresent() ).isFalse(); 
	
	}
	 
	@Test 
	public void testFindBrandByName() {
		Brand apple= repo.findByName("Apple");
		assertThat(apple != null).isTrue();
		Set<Category >listCats = (Set<Category>)apple.getCategories();
		
		assertThat(listCats.size()).isEqualTo(3);
	}
	
	@Test 
	public void testSeachKeyWord() {
		int pageNumber = 0;
		int pageSize = 6;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		 String keyWord ="Apple";
		 
		 Page<Brand> page = repo.findAll(keyWord, pageable); 
			
		 List<Brand> listBrands = page.getContent();
			
		 listBrands.forEach(brand -> System.out.println(brand));

			assertThat(listBrands.size()).isGreaterThan(0);
	
	
	}
	
	@Test 
	public void testFindALLPageable() {
		int pageNumber = 0;
		int pageSize = 6;

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		 
		 Page<Brand> page = repo.findAll( pageable); 
			
		 List<Brand> listBrands = page.getContent();
			
		 listBrands.forEach(brand -> System.out.println(brand));

			assertThat(listBrands.size()).isEqualTo(4);
	
	}
    
	@Test
	public void testFindAllSorted() {
	    List<Brand> listBrands = repo.findAll()	;
	    for(Brand brand : listBrands ) {
	    	System.out.println("Brand id: " +brand.getId() + ", Brand Name: "+ brand.getName());
	    }
		
	}
	
}
