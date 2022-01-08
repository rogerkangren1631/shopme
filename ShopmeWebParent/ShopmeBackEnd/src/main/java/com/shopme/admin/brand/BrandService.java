package com.shopme.admin.brand;

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


@Service
@Transactional
public class BrandService {
   public static final int BRANDS_PER_PAGE = 7;
	
   @Autowired
   private BrandRepository repo;
	
	public List<Brand> listAll(){
		
		return (List<Brand> )repo.findAll();
		
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		
		helper.listEntities(pageNum, BRANDS_PER_PAGE, repo);
	};
	
	public Brand save(Brand brand) {
		return repo.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			Brand brand = repo.findById(id).get();
			return brand;
			
		}catch (NoSuchElementException ex) {
			throw new BrandNotFoundException("Cound not find any brand with ID " + id);
		}	
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		Long countById = repo.countById(id);
		if (countById == null || countById == 0) {
			throw new BrandNotFoundException("Cound not find any brand with ID " + id);
		}

		repo.deleteById(id);
	}
	
	public Brand getName(String name) {
		   return repo.findByName(name);
		}
	
	public boolean isNameUnique(Integer id, String name) {		
		name = name.trim();
		Brand brandInDB = repo.findByName(name); 
		
		if( brandInDB == null) return true;
		
		boolean isCreatingNew = (id == null || id==0);
		if( isCreatingNew) {
			if( brandInDB != null ) return false;
		} else {
			
			if(brandInDB.getId() != id){
				return false;
			}else
			{
				return true;
			}				
		}
		 return true;

	}
}
