package com.wdpr.ee.service.customer.customerorder.model;


import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import com.wdpr.ee.service.customer.customerorder.model.CustomerServiceOption.orderOption;


public class CustomerOrder extends Order{
	

		public CustomerOrder(MultivaluedMap<String, String> params) {
			

			if(params!=null)
			{
			
				

			for(orderOption paramName : CustomerServiceOption.orderOption.values())
			{
				this.setOrderNumber(UUID.randomUUID().toString());
				if(paramName.toString().equals("customerId") && params.getFirst(paramName.toString())!=null)				
				this.setCustomerId(params.getFirst(paramName.toString()));
				if(paramName.toString().equals("shippingAddressId") && params.getFirst(paramName.toString())!=null)				
				this.setShippingAddressId(params.getFirst(paramName.toString()));			
				if(paramName.toString().equals("itemNumber") && params.getFirst(paramName.toString())!=null)				
				this.setItemNumber(params.getFirst(paramName.toString()));
				
				try{
				if(paramName.toString().equals("itemQuantity") && params.getFirst(paramName.toString())!=null)
					
				this.setItemQuantity(Integer.parseInt(params.getFirst(paramName.toString())));
				else
					this.setItemQuantity(1);
				}
				catch (Exception e)
				{
					throw new IllegalArgumentException("Illegal values for item quantity",  new Exception("Invalid Params"));	
				}
				
			
			}
			if(this.getCustomerId() == null || this.shippingAddressId ==null || this.getItemNumber()==null )
			{
							
				throw new IllegalArgumentException("Illegal values passed for customerid/shippingid/itemnumber",  new Exception("Invalid Params"));
			}
			}
		}
			
			
		private String  CustomerId;  
		private String shippingAddressId;
		public String getShippingAddressId() {
			return shippingAddressId;
		}
		public void setShippingAddressId(String shippingAddressId) {
			this.shippingAddressId = shippingAddressId;
		}
		public String getCustomerId() {
			return CustomerId;
		}
		public void setCustomerId(String customerId) {
			CustomerId = customerId;
		}
		public Order getOrder() {
			return order;
		}
		public void setOrder(Order order) {
			this.order = order;
		}
		private Order order;
		

//		public CustomerOrder(MultivaluedMap<String, String> params) {
//		if(params!=null)
//		{
//		
//			
//
//		for(orderOption paramName : CustomerServiceOption.orderOption.values())
//		{
//			this.setOrderNumber(UUID.randomUUID().toString());
//			if(paramName.toString().equals("customerId") && params.getFirst(paramName.toString())!=null)				
//			this.setCustomerId(params.getFirst(paramName.toString()));
//			if(paramName.toString().equals("shippingAddressId") && params.getFirst(paramName.toString())!=null)				
//			this.setShippingAddressId(params.getFirst(paramName.toString()));			
//			if(paramName.toString().equals("itemNumberAndQuantity") && params.getFirst(paramName.toString())!=null)
//			{
//				ArrayList<Order> ordersList = new ArrayList<Order>(); 
//			try{
//				
//				String[] itemsList =  params.getFirst(paramName.toString()).split(";");
//				for(int i=0; i <itemsList.length; i++)
//				{
//					Order order = new Order(); 
//					String[] itemAndQuantity =  itemsList[i].split(",");
//					order.setItemNumber(itemAndQuantity[0]);
//					if(itemAndQuantity[1] ==null)
//						order.setItemQuantity(1);
//					else
//						order.setItemQuantity(Integer.parseInt(itemAndQuantity[1]));
//					
//					ordersList.add(order);
//				}
//				this.OrderList = ordersList;
//			}
//			catch(Exception e){
//			throw new IllegalArgumentException("Illegal values passed while passing order",  new Exception("Missing shipping address/customer id/item number"));
//			}
//			
//			
//		
//		}
//		}
//		if(this.getCustomerId() == null || this.shippingAddressId ==null || this.getOrderList()==null )
//		{
//						
//			throw new IllegalArgumentException("Illegal values passed while passing order",  new Exception("Missing shipping address/customer id/item number"));
//		}
//		}
//	}
//		
//		
//	private String  CustomerId;  
//	private String shippingAddressId;
//	private String OrderNumber;
//	public String getOrderNumber() {
//		return OrderNumber;
//	}
//	public void setOrderNumber(String orderNumber) {
//		OrderNumber = orderNumber;
//		
//	}
//
//
//	private ArrayList<Order> OrderList;
//	public ArrayList<Order> getOrderList() {
//		return OrderList;
//	}
//	public void setOrderList(ArrayList<Order> orderList) {
//		OrderList = orderList;
//	}
//	public String getShippingAddressId() {
//		return shippingAddressId;
//	}
//	public void setShippingAddressId(String shippingAddressId) {
//		this.shippingAddressId = shippingAddressId;
//	}
//	public String getCustomerId() {
//		return CustomerId;
//	}
//	public void setCustomerId(String customerId) {
//		CustomerId = customerId;
//	}
//

}
