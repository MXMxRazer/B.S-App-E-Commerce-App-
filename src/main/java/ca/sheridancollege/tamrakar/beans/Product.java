package ca.sheridancollege.tamrakar.beans;

import java.io.Serializable;

import ca.sheridancollege.tamrakar.database.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class Product implements Serializable {
	private int id;
	private String name;
	private String description;
	private int quantity;
	private int price;

	public Product(int quantity) {
		this.quantity = quantity;
	}
}