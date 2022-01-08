package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.order.PaymentMethod;
import com.shopme.common.entity.product.Product;

@Commit
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderRepositoryTests {

	@Autowired
	private OrderRepository repo;

	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManager.find(Customer.class, 37);
		Product product = entityManager.find(Product.class, 1);
		
		Order mainOrder = new Order();
		mainOrder.setCustomer(customer);
		mainOrder.setFirstName("Yang");
		mainOrder.setLastName( "Ren");
		mainOrder.setPhoneNumber("205-123-4005");
		mainOrder.setAddressLine1("1856 Russet Woods Ln");
		mainOrder.setCity("Huntsville");
		mainOrder.setState("Taiwan");
		mainOrder.setPostalCode("10013");
		mainOrder.setCountry( customer.getCountry().getName());
		
		mainOrder.setShippingCost(10);
		mainOrder.setProductCost( product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal( product.getPrice());
		
		mainOrder.setTotal(product.getPrice() +10);
	    mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
	    mainOrder.setStatus(OrderStatus.NEW);
	    mainOrder.setDeliverDate( new Date());
	    mainOrder.setDeliverDays(4);
	    mainOrder.setOrderTime(new Date());
	    
	    OrderDetail orderDetail = new OrderDetail();
	    orderDetail.setProduct(product);
	    orderDetail.setOrder(mainOrder);
	    orderDetail.setQuantity(1);
	    orderDetail.setProductCost(product.getCost());
	    orderDetail.setShippingCost(10);
	    orderDetail.setSubtotal(0);
	    orderDetail.setSubtotal(product.getPrice());
	    orderDetail.setUnitPrice(product.getPrice());
	    
	    mainOrder.getOrderDetails().add(orderDetail);
	    
	    Order savedOrder = repo.save(mainOrder);
	    assertThat(savedOrder).isNotNull();
	}
	
	@Test
	public void testCreateNewOrderWithMultipleProducts() {
		Customer customer = entityManager.find(Customer.class, 3);
		Product product1 = entityManager.find(Product.class, 4);
		Product product2 = entityManager.find(Product.class,10);
		
		Order mainOrder = new Order();
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();
		
	    OrderDetail orderDetail1 = new OrderDetail();
	    orderDetail1.setProduct(product1);
	    orderDetail1.setOrder(mainOrder);
	    orderDetail1.setQuantity(1);
	    orderDetail1.setProductCost(product1.getCost());
	    orderDetail1.setShippingCost(10);
	    orderDetail1.setSubtotal(40);
	    orderDetail1.setSubtotal(product1.getPrice());
	    orderDetail1.setUnitPrice(product1.getPrice());
	    
	    mainOrder.getOrderDetails().add(orderDetail1);
	    
	    OrderDetail orderDetail2 = new OrderDetail();
	    orderDetail2.setProduct(product2);
	    orderDetail2.setOrder(mainOrder);
	    orderDetail2.setQuantity(1);
	    orderDetail2.setProductCost(product2.getCost());
	    orderDetail2.setShippingCost(10);
	    orderDetail2.setSubtotal(0);
	    orderDetail2.setSubtotal(product2.getPrice());
	    orderDetail2.setUnitPrice(product2.getPrice());
	    
	    mainOrder.getOrderDetails().add(orderDetail2);
	    
	    
		mainOrder.setShippingCost(20);
		mainOrder.setProductCost( product1.getCost() + product2.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubtotal( product1.getPrice() +  product2.getPrice());
		
		mainOrder.setTotal(product1.getPrice() +  product2.getPrice()+10);
	    mainOrder.setPaymentMethod(PaymentMethod.COD);
	    mainOrder.setStatus(OrderStatus.PROCESSING);
	    mainOrder.setDeliverDate( new Date());
	    mainOrder.setDeliverDays(4);
	    mainOrder.setOrderTime(new Date());
	    
	    Order savedOrder = repo.save(mainOrder);
	    assertThat(savedOrder).isNotNull();
		
	}
		
	@Test
	public void testListOrders() {
		
		Iterable<Order> allOrders = repo.findAll();
		allOrders.forEach(System.out::println);
		assertThat(allOrders).size().isEqualTo(2);
	}
	
	@Test 
	public void testUpdate() {
		Integer id = 3;
		Order order = repo.findById(id).get();
		order.setStatus(OrderStatus.SHIPPING);
		order.setPaymentMethod(PaymentMethod.COD);
		
		Order savedOrder = repo.save(order);
		assertThat(savedOrder).isNotNull();
	}
	
	@Test
	public void testDeleteOrder() {	
		Integer orderId = 3;
		repo.deleteById(orderId);
		Optional<Order> op = repo.findById(orderId);
		assertThat(op.isPresent()).isFalse();		
	}
	
	@Test 
	public void testUpdateOrderTracks() {
		Integer orderId = 13;
		Order order = repo.findById(orderId).get();
		
		OrderTrack delivedTrack = new OrderTrack();
	    delivedTrack.setOrder(order);
	    delivedTrack.setUpdatedTime( new Date());
	    delivedTrack.setStatus(OrderStatus.DELIVERED);
	    delivedTrack.setNotes(OrderStatus.DELIVERED.defaultDescription());
	    
		OrderTrack paidTrack = new OrderTrack();
		paidTrack.setOrder(order);
		paidTrack.setUpdatedTime( new Date());
		paidTrack.setStatus(OrderStatus.PAID);
		paidTrack.setNotes(OrderStatus.PAID.defaultDescription());
	    
	    List<OrderTrack> orderTracks = order.getOrderTracks();
	    orderTracks.add(delivedTrack);
	    orderTracks.add(paidTrack);
	    
	     Order updatedOrder = repo.save(order);
	     
	    assertThat(updatedOrder.getOrderTracks().size()).isGreaterThan(0);
	    
	}
	
	
}
