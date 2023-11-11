package ca.sheridancollege.tamrakar.database;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ca.sheridancollege.tamrakar.beans.BuyerQuantity;
import ca.sheridancollege.tamrakar.beans.Product;
import ca.sheridancollege.tamrakar.beans.RegisterUser;
import ca.sheridancollege.tamrakar.beans.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ProductRepository {
	protected NamedParameterJdbcTemplate jdbc;

	public List<User> getUserByUserName(String username) {
		MapSqlParameterSource namedPara = new MapSqlParameterSource();
		String query = "SELECT * FROM users WHERE name=:username";
		namedPara.addValue("username", username);

		return jdbc.query(query, namedPara,
				new BeanPropertyRowMapper<>(User.class));
	}

	public int insertUser(RegisterUser user) {
		MapSqlParameterSource namedPara = new MapSqlParameterSource();
		String query = "INSERT INTO users (name, password, role) VALUES (:name,:password,:role)";
		namedPara.addValue("name", user.getName());
		namedPara.addValue("password", user.getPassword());
		namedPara.addValue("role", user.getRole());

		return jdbc.update(query, namedPara);
	}

	public void addProduct(Product product) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();

		String query = "INSERT INTO products (name, description, quantity, price) " +
				"VALUES (:name, :description, :quantity, :price)";

		parameters.addValue("name", product.getName());
		parameters.addValue("description", product.getDescription());
		parameters.addValue("quantity", product.getQuantity());
		parameters.addValue("price", product.getPrice());

		jdbc.update(query, parameters);
	}

	public ArrayList<Product> getProductsInventory() {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM inventory";

		ArrayList<Product> products = (ArrayList<Product>) jdbc.query(query, parameters, new BeanPropertyRowMapper<>(Product.class));
		return products;
	}

	public ArrayList<Product> getProducts() {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM products";

		ArrayList<Product> products = (ArrayList<Product>) jdbc.query(query, parameters, new BeanPropertyRowMapper<>(Product.class));

		return products;
	}


	public Product getProductById(int id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM products WHERE id = :id";
		parameters.addValue("id", id);

		ArrayList<Product> products = (ArrayList<Product>) jdbc.query(query, parameters, new BeanPropertyRowMapper<>(Product.class));

		return products.size() > 0 ? products.get(0) : null;
	}


	public void updateProduct(Product product) {
		String query = "UPDATE products SET name=:name, description=:description, quantity=:quantity, price=:price WHERE id=:id";

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", product.getId());
		parameters.addValue("name", product.getName());
		parameters.addValue("description", product.getDescription());
		parameters.addValue("quantity", product.getQuantity());
		parameters.addValue("price", product.getPrice());

		jdbc.update(query, parameters);
	}

	public void updateQuantityAndInsertProduct(BuyerQuantity buyerQuantity) {
		String selectQuery = "SELECT * FROM products WHERE id=:id1";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id1", buyerQuantity.getId());

		Product product = jdbc.query(selectQuery, parameters, new BeanPropertyRowMapper<>(Product.class)).get(0);

		String query = "UPDATE products SET quantity=:quantity WHERE id=:id";

		parameters.addValue("quantity", product.getQuantity() - buyerQuantity.getQuantity());
		parameters.addValue("id", buyerQuantity.getId());

		jdbc.update(query, parameters);

		String query2 = "INSERT INTO inventory (name, description, quantity, price) " +
				"VALUES (:name, :description, :quantity, :price)";

		parameters.addValue("name", product.getName());
		parameters.addValue("description", product.getDescription());
		parameters.addValue("quantity", buyerQuantity.getQuantity());
		parameters.addValue("price", product.getPrice());

		jdbc.update(query2, parameters);
	}


	public int deleteProduct(int id) {
		String query = "DELETE FROM products WHERE id=:id";

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", id);

		return jdbc.update(query, parameters);
	}
	
}