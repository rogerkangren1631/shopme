package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 5;
	@Autowired
	private ProductRepository repo;

	public List<Product> ListAll() {

		return (List<Product>) repo.findAll();

	}

	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId) {
		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);

		String keyword = helper.getKeyword();
		Page<Product> page = null;

		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + categoryId + "-";
				page = repo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			} else {

				page = repo.findAll(keyword, pageable);
			}

		} else {

			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + categoryId + "-";
				page =  repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
			} else {
				page = repo.findAll(pageable);			
			}
		}
		helper.updateModelAttributes(pageNum, page);
	};

	public void searchProducts(int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		
		Page<Product> page = repo.searchProductsByName(keyword, pageable); 
		helper.updateModelAttributes(pageNum, page);
	}
	
	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());
		return repo.save(product);
	}

	public void saveProductPrice(Product product) {
		Product productInDB = repo.findById(product.getId()).get();

		productInDB.setPrice(product.getPrice());
		productInDB.setCost(product.getCost());
		productInDB.setDiscountPercent(product.getDiscountPercent());

		repo.save(productInDB);
	}

	public boolean isNameUnique(Integer id, String name) {
		name = name.trim();
		Product productInDB = repo.findByName(name);
		if (productInDB == null) {
			return true;
		} else {
			boolean isCreatingNew = (id == null || id == 0);
			if (isCreatingNew) {
				return false;
			} else if (productInDB.getId() != id) {
				return false;
			}
			return true;
		}
	}

	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}

	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Cound not find any product with ID " + id);
		}
		repo.deleteById(id);
	}

	public Product get(Integer id) throws ProductNotFoundException {

		try {
			return repo.findById(id).get();

		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}

	}

}
