package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
public class CustomerListWrapper {
	
	private List<User> customer;
	
	@XmlElement(name = "customer")
	public List<User> getCustomer() {
		return customer;
	}
	
    public void setCustomer(List<User> customer) {
        this.customer = customer;
    }
}