package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

	static void deleteExtraImagesWereRemovedOnForm(Product product) {
		String extraImageDir = "product-images/" + product.getId() + "/extras/";

		List<String> listObjectKeys = AmazonS3Util.listFolder(extraImageDir);
		for (String objectKey : listObjectKeys) {
			int lastIndexOfSlash = objectKey.lastIndexOf("/");
			String fileName = objectKey.substring(lastIndexOfSlash + 1, objectKey.length());
			if (!product.containsImageName(fileName)) {

				AmazonS3Util.deleteFile(objectKey);
				System.out.println("Deleted extra image: " + objectKey);
			}

		}

	}

	static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

		if (imageIDs == null || imageIDs.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			name = name.replaceAll(" ", "-");
			images.add(new ProductImage(id, name, product));

		}
		product.setImages(images);
	}

	static void setProductDetails(String[] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = 0;
			
			if( detailIDs[count] != null && !detailIDs[count].isEmpty()) {	
				id = Integer.parseInt(detailIDs[count]);
			}
			if (id== null || id != 0) {
				product.addDetail(id, name, value);
			} else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
		}

	}

	static void saveUpLoadedImages(MultipartFile mainImageMultipart, MultipartFile[] extramageMultiparts,
			Product savedProduct) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			fileName = fileName.replaceAll(" ", "-");
			String uploadDir = "product-images/" + savedProduct.getId();

			List<String> listObjectKeys = AmazonS3Util.listFolder(uploadDir + "/");
			for (String objectKey : listObjectKeys) {
				if (!objectKey.contains("/extras")) {
					AmazonS3Util.deleteFile(objectKey);
				}
			}

			AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());

			// FileUploadUtil.cleanDir(uploadDir);
			// FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extramageMultiparts.length > 0) {
			String uploadDir = "product-images/" + savedProduct.getId() + "/extras";

			for (MultipartFile multipartFile : extramageMultiparts) {
				if (multipartFile.isEmpty()) {
					continue;
				} else {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					fileName = fileName.replaceAll(" ", "-");

					AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());

					// FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				}
			}
		}
	}

	static void setNewExtraImageNames(MultipartFile[] extramageMultiparts, Product product) {
		if (extramageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extramageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					fileName = fileName.replaceAll(" ", "-");

					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}

	}

	static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			fileName = fileName.replaceAll(" ", "-");
			product.setMainImage(fileName);
		}
	}

}
