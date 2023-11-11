package ca.sheridancollege.tamrakar.controller;

import ca.sheridancollege.tamrakar.beans.BuyerQuantity;
import ca.sheridancollege.tamrakar.beans.CheckUser;
import ca.sheridancollege.tamrakar.beans.Product;
import ca.sheridancollege.tamrakar.beans.RegisterUser;
import ca.sheridancollege.tamrakar.database.ProductRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@NoArgsConstructor
public class ProductController {
	@Autowired
	private ProductRepository productRepo;

	@GetMapping("/")
	public String homeToLogin() {
		return "redirect:/login";
	}

	@GetMapping("/sellerHome")
	public String loadRootSeller() {
		return "homeSeller.html";
	}

	@GetMapping("/buyerHome")
	public String loadRootBuyer() {
		return "homeBuyer.html";
	}

	@GetMapping("/login")
	public String login(Model m) {
		return "login";
	}

	@PostMapping("/login")
	public String checkUserAndRedirectToHome(
			@ModelAttribute("user") CheckUser user,
			Model m
	) {
		if (productRepo.getUserByUserName(user.getName()).size() > 0
				&& productRepo.getUserByUserName(user.getName()).get(0).getPassword().equals(user.getPassword())
		)
		{
			m.addAttribute("username", user.getName());
			if (productRepo.getUserByUserName(user.getName()).get(0).getRole().equals("seller")) {
				return "homeSeller";
			}
			return "homeBuyer";
		} else {
			m.addAttribute("errorUser", user.getName());
			return "invalid";
		}
	}

	@GetMapping("/register")
	public String register(Model m) {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(
			@ModelAttribute("user") RegisterUser user,
			Model m
	) {
		productRepo.insertUser(user);
		m.addAttribute("newUserMsg", "User Added Successfully!");
		return "login";
	}

	@GetMapping("/add")
	public String goAddProducts(Model model) {
		model.addAttribute("product", new Product());
		return "addProduct.html";
	}

	@PostMapping("/add")
	public String processAddProduct(@ModelAttribute Product product, Model model) {
		productRepo.addProduct(product);
		return "homeSeller.html";
	}

	@GetMapping("/viewSeller")
	public String viewProductsSeller(Model model) {
		model.addAttribute("products", productRepo.getProducts());
		return "viewProducts.html";
	}

	@GetMapping("/viewBuyer")
	public String viewProductsBuyer(Model model) {
		model.addAttribute("products", productRepo.getProducts());
		return "viewProductsBuyer.html";
	}

	@GetMapping("/viewInventory")
	public String viewProductsInventory(Model model) {
		model.addAttribute("inventoryProducts", productRepo.getProductsInventory());
		return "buyersInventory";
	}

	@GetMapping("/editLink/{id}")
	public String editLink(Model model, @PathVariable int id) {
		Product p = productRepo.getProductById(id);
		model.addAttribute("product", p);
		return "editProduct.html";
	}

	@GetMapping("/buyingLink/{id}")
	public String buyLink(Model model, @PathVariable int id) {
		Product p = productRepo.getProductById(id);
		model.addAttribute("product", p);
		model.addAttribute("buyerQuantity", new BuyerQuantity());
		return "buyRequest.html";
	}

	@PostMapping("/buyerReq")
	public String buyersRequest(@ModelAttribute BuyerQuantity buyerQuantity, Model model) {
		productRepo.updateQuantityAndInsertProduct(buyerQuantity);
		return "homeBuyer";
	}

	@PostMapping("/modify")
	public String modifyProduct(@ModelAttribute Product product, Model model) {
		productRepo.updateProduct(product);
		model.addAttribute("products", productRepo.getProducts());
		return "viewProducts.html";
	}

	@GetMapping("/deletelink/{id}")
	public String deletePage(@PathVariable int id, Model model) {
		productRepo.deleteProduct(id);
		model.addAttribute("products", productRepo.getProducts());
		return "viewProducts.html";
	}

	@GetMapping("/*")
	public String invalidPage() {
		return "invalid";
	}
}