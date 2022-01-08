package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductDetail;
import com.shopme.common.entity.product.ProductImage;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositortTests {
	@Autowired
	private ProductRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {

		Brand brand = entityManager.find(Brand.class, 37);
		Category category = entityManager.find(Category.class, 3);

		Product product = new Product();
		product.setName("Accer Aspire Desktop");
		product.setAlias("accer_Aspire_desktop");
		product.setShortDescription(
				"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.");
		product.setFullDescription(
				" Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus..");

		product.setCategory(category);
		product.setBrand(brand);

		product.setPrice(999);
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = repo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);

	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> itProducts = repo.findAll();
		itProducts.forEach(System.out::println);
	}

	@Test
	public void testListAllProductsForImages() {
		Iterable<Product> itProducts = repo.findAll();
		itProducts.forEach(System.out::println);

	}

	@Test    //這是見老師的附件中的空格去掉
	public void testListAllProductImagesAndReplaceImageName() {

		List<Product> listProducts = new ArrayList<>();

		Iterable<Product> itProducts = repo.findAll();

		itProducts.forEach(listProducts::add);

		int length = listProducts.size();

		for (int i = 0; i < length; i++) {

			Product product = listProducts.get(i);
			// System.out.println("product : "+ product);
			String newMainImage = product.getMainImagePath();

			int biginIndex = newMainImage.lastIndexOf("/") + 1;
			int endIndex = newMainImage.length();
			newMainImage = newMainImage.substring(biginIndex, endIndex);
			newMainImage = newMainImage.replaceAll(" ", "-");
			System.out.println("newMainImage : " + newMainImage);
			product.setMainImage(newMainImage);

			Set<ProductImage> imageSet = product.getImages();

			Set<ProductImage> newImageSet = new HashSet<>();

			List<ProductImage> images = new ArrayList<>(imageSet);

			for (int j = 0; j < images.size(); j++) {
				ProductImage extraImage = images.get(j);
				String name = extraImage.getName();
				name = name.replaceAll(" ", "-");

				extraImage.setName(name);
				System.out.println("Extra Image ->  id: " + extraImage.getId() + " name:" + extraImage.getName()
						+ " , parent id:" + product.getId());
				newImageSet.add(extraImage);

			}
			product.setImages(newImageSet);

			repo.save(product);

		}

	}

	@Test
	public void testGetProduct() {
		Integer id = 2;
		Optional<Product> getProduct = repo.findById(id);
		assertThat(getProduct.isPresent()).isTrue();
	}

	@Test
	public void testUpdateProduct() {

		Integer id = 1;
		Product product = repo.findById(id).get();
		product.setPrice(399);

		Product savedProduct = repo.save(product);
		assertThat(savedProduct.getId()).isEqualTo(1);

	}

	@Test
	public void testDeleteProduct() {
		Integer id = 3;
		repo.deleteById(id);

		Optional<Product> result = repo.findById(id);

		assertThat(result.isPresent()).isFalse();

	}

	@Test
	public void testFindProductByName() {
		String name = "    Samsung Galaxy A31 ";
		name = name.trim();

		Product product = repo.findByName(name);
		assertThat(product).isNotNull();

		System.out.println("Product name is " + product.getName());

	}

	@Test
	public void testEnableProduct() {
		Integer id = 9;

		Optional<Product> productInDB = repo.findById(id);

		assertThat(productInDB.isPresent()).isTrue();

		repo.updateEnabledStatus(id, false);

		Optional<Product> updatedProductInDB = repo.findById(id);

		assertThat(updatedProductInDB.isPresent()).isTrue();

		Product product = updatedProductInDB.get();
		boolean isEnable = product.isEnabled();

		assertThat(isEnable).isTrue();

	}

	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = repo.findById(productId).get();

		product.setMainImage("Main_image1.png");
		product.addExtraImage("extract-image1.png");
		product.addExtraImage("extract-image2.png");
		product.addExtraImage("extract-image3.png");

		Product savedProduct = repo.save(product);

		assertThat(savedProduct.getImages().size()).isEqualTo(3);

	}

	@Test
	public void testSaveProductWithDetails() {
		Integer productId = 1;
		Product savedProduct = repo.findById(productId).get();

		// product.addDetail( "Cpu", "Intel 456" );
		// product.addDetail( "Memory", "16GB" );
		// product.addDetail( "HD Diver ", "512GB" );
		// product.addDetail( "Screen Size", "15 inch" );

		// Product savedProduct = repo.save(product);

		assertThat(savedProduct.getDetails()).isNotEmpty();

		assertThat(savedProduct.getDetails().size()).isEqualTo(4);

	}

}
