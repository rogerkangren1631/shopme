package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class SettingReposityorTests {
	@Autowired
	private SettingRepository repo;
	

	@Test
	public void testCreateGeneralSetting() {
		
		String newSiteValue  = "Shopme";
		//Setting  siteName = new Setting("SITE_NAME", newSiteValue, SettingCategory.GENERAL); 
		Setting  siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL); 
		Setting  copyright = new Setting("COPYRIGHT", "Copyright2021 Shopme ltd.", SettingCategory.GENERAL); 
	 repo.saveAll( List.of(siteLogo, copyright));
		 
	    Iterable<Setting> itSettings = repo.findAll();
	    
	    List<Setting> listSettings = new ArrayList<>();
	    
	    itSettings.forEach(  listSettings::add );    
		 assertThat(listSettings.size()).isEqualTo(3);		
	}
	
	@Test 
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY); 
		Setting symbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY); 
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY); 
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY); 
		Setting decimalDigits = new Setting("DECIMAL_DIGITS", "", SettingCategory.CURRENCY); 
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY); 
		
		repo.saveAll(List.of(  currencyId,symbol, symbolPosition, decimalPointType, decimalDigits, 
				thousandsPointType));
	}
	
	@Test 
	public void testGetSettingListByCategory() {
		List<Setting> findGeneralSettings = repo.findByCategory(SettingCategory.CURRENCY);
		
		findGeneralSettings.forEach(System.out::println);
	}
	
}
