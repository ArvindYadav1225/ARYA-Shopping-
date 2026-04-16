package com.example.ARYAShopping.Appcontroller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ARYAShopping.entity.CartItems;
import com.example.ARYAShopping.entity.Orders;
import com.example.ARYAShopping.entity.Product;
import com.example.ARYAShopping.entity.SearchHistory;
import com.example.ARYAShopping.entity.User;
import com.example.ARYAShopping.repositery.CartRepository;
import com.example.ARYAShopping.repositery.OrderRepositery;
import com.example.ARYAShopping.repositery.ProductRepositery;
import com.example.ARYAShopping.repositery.SearchHistoryRepository;
import com.example.ARYAShopping.repositery.Userrepositery;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@Controller
public class EcommerciaAppController {
    @Autowired
    private JavaMailSender mailSender;
	@Autowired
	private SearchHistoryRepository sr;
	@Autowired
	private ProductRepositery pr;
	@Autowired
	private Userrepositery ur;
	@Autowired
	private CartRepository cr ;
	@Autowired
	private OrderRepositery or;
	@GetMapping("/home")
	public String home(Model m ,HttpSession session,HttpServletRequest request) {
		Cookie[] cookies=request.getCookies();
		if(cookies!=null) {
			for(Cookie cookie:cookies) {
				if("mobile".equals(cookie.getName())) {
					session.setAttribute("mobile", cookie.getValue());
					break;
				}
			}
		}
		String mobile=(String)session.getAttribute("mobile");
		 User u;
		if(mobile!=null){
	    u=ur.getByMobile(mobile);
	    session.setAttribute("user", u);
	   List<String> history= sr.findTop10ByUserOrderByCreatedAtDesc(u)
        .stream()
        .map(SearchHistory::getSearchText)
        .toList();

       m.addAttribute("searchhistory", history);
	   
		}
		//System.out.println("yadavji");
		List<String> images=new ArrayList<>();
		images.add("dis2.jpg");
		images.add("dis3.jpg");
		images.add("dis4.jpg");
		images.add("dis5.jpg");
		images.add("dis6.jpg");
	    if(!m.containsAttribute("products"))
		m.addAttribute("products",pr.findAll());
		m.addAttribute("images",images);
		return"home";
	}
	
	@PostMapping("/addtocart/{name}")
	public String addtocart(Model m, HttpSession session, @PathVariable String name) {
	    
	    String mobile = (String) session.getAttribute("mobile");
	    if (mobile == null) {
	        System.out.println("User not registered. Please signup first.");
	        return "redirect:/signup";
	    }

	    User u = ur.getByMobile(mobile);

	    Product pro = pr.getByName(name);
	     if (pro == null) {
	        System.out.println("Product not found: " + name);
	        return "redirect:/home";
	    }
	      
	    // Check if the product is already in the cart
	    boolean found = false;
	    for (CartItems ci : u.getCartItems()) {
	        if (ci.getProduct() != null && ci.getProduct().getId() == pro.getId()) {
	            // Increment quantity
	            ci.setQuantity(ci.getQuantity() + 1);
	            cr.save(ci);
	            found = true;
	            break;
	        }
	    }

	    // If not found, create a new CartItem
	    if (!found) {
	        CartItems cart = new CartItems();
	        cart.setProduct(pro);
	        cart.setUser(u);
	        cart.setQuantity(1);
	        cr.save(cart);
	        u.getCartItems().add(cart);
	    }

	    m.addAttribute("user", u);

	    return "redirect:/home";
	}
	@PostMapping("/submitSignup")
	public String submitSignup(@Valid @ModelAttribute("user") User user ,BindingResult result,@RequestParam String cpass,Model model,HttpSession session,HttpServletResponse response
			) {
		if(ur.getByGmail(user.getGmail())!=null) 
			result.rejectValue("gmail", null," this Email is already exist");
		if(  user.getMobile()==null||     user.getMobile().length()!=10) result.rejectValue("mobile", user.getMobile(),"enter 10 digit mobile number.");
		if(!result.hasFieldErrors("name")) {
		if(user.getName().length()<4)result.rejectValue("name",user.getName(),"name must be Minimum 3 character");
		else if(user.getName().length()>15)result.rejectValue("name",user.getName(),"name will be Maximum 15 character");
		}
		if(ur.getByMobile(user.getMobile())!=null) result.rejectValue("mobile", user.getMobile()," this mobile no is already exist");
		if(!result.hasFieldErrors("password")){
			if(user.getPassword().length()<=8)result.rejectValue("password", null,"password will be minimum 8 character.");
			else if(user.getPassword().length()>15)result.rejectValue("password", null," password will be maximum 15 character.");
		}
		if(!user.getPassword().equals(cpass))result.reject("password.mismatch", "password and confirm password cannot be match.");
		//if(!user.getPassword().equals(cpass))result.rejectValue("cpass", null,"password and confirm password is not matching.");
			if(result.hasErrors()) {
				return "signup";
			}
			
			ur.save(user);
			session.setAttribute("mobile",user.getMobile());
			Cookie cookie=new Cookie("mobile",user.getMobile());
			cookie.setMaxAge(60*60*24*365);
			cookie.setPath("/");
			response.addCookie(cookie);
			return"redirect:/home";
			
		}
//		User u=ur.getByMobile(user.getMobile());
//		System.out.println("signup user is:"+u);
//		if(u==null) {
//	            session.setAttribute("mobile", user.getMobile());
//	          
//	            ur.save(user);
//		
//		
//		return "redirect:/home";
//		}
//		else return"redirect:/signup";

	@GetMapping("/signup")
	public String signup( Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	@GetMapping("/login")
	public String login( Model m) {
		
		return "login";
	}
	@PostMapping("/submitLogin")
	public String submitLogin(@RequestParam("mobile") String mobile,@RequestParam("password") String password,@RequestParam("cpassword") String cpassword,HttpSession session,Model m,HttpServletResponse response) {
		
		if(!password.equals(cpassword)) {
			m.addAttribute("mismatchpassword", "password and confirm password is not matching.");
			System.out.println(m.getAttribute("mismatchpassword"));
			return"redirect:/login";
			}
		  User u=ur.getByMobile(mobile);
		  if(u==null) {
			  m.addAttribute("invalidnumber","this number is not registered.");
			  System.out.println(m.getAttribute("invalidnumber"));
			  return " redirect:/login";
		  }
		  if(u.getPassword().equals(password)) {
			 session.setAttribute("mobile", mobile);
			 Cookie cookie=new Cookie("mobile",u.getMobile());
				cookie.setMaxAge(60*60*24*365);
				cookie.setPath("/");
				response.addCookie(cookie);
			  return "redirect:/home";
		  }
		  m.addAttribute("invalidpassword","invalid password.");
		  System.out.println(m.getAttribute("invalidpassword"));
		return "redirect:/login";
	}
	@GetMapping("/logout")
	public String logout( HttpSession session,HttpServletResponse response) {
		Cookie cookie=new Cookie("mobile",null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		session.removeAttribute("mobile");
		
		return "redirect:/home";
	}
	@GetMapping("/search")
	public String searchHistory(@RequestParam("searchValue") String searchValue,HttpSession session,Model m) {
		List<String>history=new ArrayList<>();
		
		String mob=(String)session.getAttribute("mobile");
//		if(mob!=null) {
//			User u=ur.getByMobile(mob);
//			
//			  history= sr.findTop10ByUserOrderByCreatedAtDesc(u)
//				        .stream()
//				        .map(SearchHistory::getSearchText)
//				        .toList();
//			  SearchHistory sh = new SearchHistory();
//				sh.setSearchText(searchValue);
//				sh.setUser(u);   // important
//				sr.save(sh); 
//		}
//		if(mob==null) {
			if(session.getAttribute("searchhistory")!=null)
				history=(List<String>)session.getAttribute("searchhistory");
			System.out.println("search history"+m.getAttribute("searchhistory"));
			
		//}
		
	      history.add(searchValue);
	     history= history.reversed();
	     while(history.size()>10) history.remove(history.size()-1);
//		Stack<String> search=new Stack<>();
//		search=u.getSearchhistory();
//	    search.push(searchValue);
//	    u.setSearchhistory(search);
		 
         session.setAttribute("searchhistory", history);
	     m.addAttribute("searchhistory", history);
		List<String> images=new ArrayList<>();
		images.add("dis2.jpg");
		images.add("dis3.jpg");
		images.add("dis4.jpg");
		images.add("dis5.jpg");
		images.add("dis6.jpg");
	    if(!m.containsAttribute("products"))
		//m.addAttribute("products",pr.findAll());
		m.addAttribute("images",images);
		
	    //model.addAttribute("searchHistory",search);
		//System.out.println("search history is : "+u.getSearchhistory());
		List<Product> p=pr.findAll();
		List<Product>p2=new ArrayList<>();
		for(Product pro:p) {
			
			List<String> category=pro.getProductCategory();
			boolean flag=false;
			    for(String s:category) {
			    	if(s.toUpperCase().contains(searchValue.toUpperCase()) || s.toUpperCase().equals(searchValue.toUpperCase())) {p2.add(pro);flag=true;}
			    	else {
			    	  Set<Character> set=new HashSet<>();	
			    	    for(char c :s.toCharArray()) {
			    	    	set.add(c);
			    	    	
			    	    }
			    	    int matches=0;
			    	    for(char c:searchValue.toCharArray()) {
			    	    	if(set.contains(c)) matches++;
			    	    }
			    	    if(matches>=s.length()*9.8/10) {
			    	    	p2.add(pro);
			    	    	flag=true;
			    	    }
			    	    }
			    	if(flag==true)break;
			    	}
			    	}
			
		
		 m.addAttribute("products",p2);
		 System.out.println("search products are:"+p2);
		//return "home";
		return "home";
	}
	@GetMapping("/viewProduct")
	public String viewProduct(@RequestParam  String productName,Model model) {
		Product p=pr.getByName(productName);
		
		 model.addAttribute("product",p);  
		 model.addAttribute("hilights",p.getHilights().entrySet());
		 model.addAttribute("hilights",p.getHilights().entrySet());
		 System.out.println("all product hilights is:"+p.getHilights());
		return "viewProduct";
	}
	@GetMapping("/cartpage")
	public String cartPage(HttpSession session, Model model) {

	    String mob = (String) session.getAttribute("mobile");
	    User u = ur.getByMobile(mob);
         System.out.println("cart items are cccccccccccc :"+u.getCartItems());
	    List<CartItems> validCartItems = u.getCartItems()
	            .stream()
	            .filter(ci -> ci.getProduct() != null)
	            .toList();
       double totalOldPrice=0;
       double totalPrice=0;
       for ( CartItems c : validCartItems) {
           totalOldPrice += c.getProduct().getOldprice();
           totalPrice += c.getProduct().getPrice();
           
       }

	    model.addAttribute("cartItems", validCartItems);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("totalOldPrice",totalOldPrice);
        model.addAttribute("totalDiscount",(totalOldPrice-totalPrice));
	    return "Cartpage";
	}
	@GetMapping("/removeItemFromCart")
	public String removeItemFromCart(@RequestParam int productId,
	                                 HttpSession session) {
		Product pro=pr.getById(productId);
        //System.out.println(" deleted product is "+productId);
	    User u = ur.getByMobile((String) session.getAttribute("mobile"));
	    List<CartItems> products = u.getCartItems();
        System.out.println("all product are :arfhvg"+products.size());
	    Iterator<CartItems> iterator = products.iterator();
	    products.forEach(a->System.out.println("cartb items :"+a));
	   for(int i=1;i<products.size();i++) {
		       
		      if( products.get(i).getProduct().getId()==pro.getId()) {
		    	  cr.deleteByUserAndProductId(u, productId);
		      products.remove(i);
		      
			   break;
		   }
		   
	   }
        
	    u.setCartItems(products);
	    ur.save(u);
        System.out.println("cartItems are:"+u.getCartItems());
	    return "redirect:/cartpage";
	}
	@GetMapping("/buyProducts")
	public String buyProduct( Model model,HttpSession session,@RequestParam(required=false) String productName) {
		 User u=ur.getByMobile((String)session.getAttribute("mobile"));
		 if(u==null) {
			 return "redirect:/signup";
		 }
		 model.addAttribute("address",u.getLocation());
		model.addAttribute("productName", productName);
		 System.out.println(u.getLocation());
		return"buyProductPage";
	}
	@GetMapping("/confirmOrder")
	public String confirmOrder(@RequestParam String address,  @RequestParam String houseNo, @RequestParam String city, @RequestParam  String state,@RequestParam(required=false) String productName,HttpSession session,Model model) {
		     String fullAddress=address+","+houseNo+","+city+","+state;
		         User u=ur.getByMobile((String)session.getAttribute("mobile"));
//		     System.out.println("full address is "+fullAddress);	
//		     System.out.println("payment methos : "+paymentMethod);
		     if(!productName.equals("")) {
		    	// System.out.println("fullAddress is 11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111:"+productName);
				 
		    	 Product p=pr.getByName(productName);
		    	 List<Orders> orders=u.getOrder();
		    	 Orders o=new Orders();
		    	 o.setProductName(p.getName());
			    	o.setQuantity(1);
			    	o.setUser(u);
			    	o.setDate(LocalDate.now());
			    	or.save(o);
			    	orders.add(o);
			    	u.setOrder(orders);
			    	if(u.getLocation()==null)u.setLocation(fullAddress);
			    	ur.save(u);
		    	 return"confirmOrder";
		     }
		     List<CartItems> cartItems=u.getCartItems();
		     List<Product> products=new ArrayList<>();
		     List<Orders> orders=u.getOrder();
//		     for(CartItems ci:cartItems) {
//		    	 
//		    	 if(ci.getProduct()!=null)products.add(ci.getProduct());
//		     }
		     int cartItemsSize=cartItems.size();
		     for(int i=0;i<cartItemsSize;i++) {
		    	// System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiIIIIIIIIIIIIIIIIIIIIIII"+i);
		    	 CartItems ci=cartItems.get(i);
		    	 products.add(ci.getProduct());
		    	 cr.deleteByUserAndProductId(u, ci.getProduct().getId());
		    	
		    	// cr.deleteByUserAndProductName(u, ci.getProduct().getName());
		    	
		    	 //cr.deleteByUserAndProductId(u,ci.getProduct().getId());
		    	 
		    	 
		    	 
		    	 
		     }
		     cartItems.clear();
		  
		     for(Product pro :products) {
		    	 Orders o=new Orders();
		    	o.setProductName(pro.getName());
		    	o.setQuantity(1);
		    	o.setUser(u);
		    	o.setDate(LocalDate.now());
		    	or.save(o);
		    	orders.add(o);
		     }
		    // u.setLocation(fullAddress);
		   System.out.println(u.getLocation());
		     u.setOrder(orders);
		     if( u.getLocation()==null ||u.getLocation().equals(null) ) 
		     u.setLocation(fullAddress);
		     System.out.println("all cartitems product are asasasdasasd "+cartItems);
		     u.setCartItems(cartItems);
		     ur.save(u);
		     System.out.println("my orders product are:"+u.getOrder());
		    
		return"confirmOrder";
	}
	@GetMapping("/myOrders")
	public String myOrders( HttpSession session,Model m ) {
		 User u=ur.getByMobile((String)session.getAttribute("mobile"));
		 System.out.println(u.getCartItems()+"arvind yadav bca ");
		 List<Orders> orders=u.getOrder();
		 int x=u.getOrder().size();
		 //List<Product> products=new ArrayList<>();
		 Map<Map<Integer,Product>,Map<LocalDate,Long>> products=new HashMap<>();
		
		 for(Orders s: orders) {
			
			 Map<LocalDate,Long> d=new HashMap<>();
			 d.put(s.getDate(),7-ChronoUnit.DAYS.between(s.getDate(),LocalDate.now()));
			 Map<Integer,Product> p=new HashMap<>();
			 p.put(x++, pr.getByName(s.getProductName()));
			 products.put(p,d);
			
			 		 }
		 m.addAttribute("products",products);
		
		 System.out.println("my orders are :"+products);
		return "myorders";
	}
	@GetMapping("/paymentMethod")
	public String paymentMethod(@RequestParam(required=false) String address,@RequestParam(required=false) String houseNo,@RequestParam(required=false) String city,@RequestParam(required=false)  String state,@RequestParam("paymentMethod") String paymentMethod,@RequestParam(required=false)  String productName ){
		 System.out.println("productname is:"+productName);       
		if(paymentMethod.equals("upi"))return"upiPayment";
		        if(paymentMethod.equals("netBanking"))return"netBankingPayment";
		        if(paymentMethod.equals("debitCard"))return"debitCard";
		        if(paymentMethod.equals("cod"))return "redirect:/confirmOrder?address="+address +"&houseNo="+houseNo+"&city="+city+"&state="+state+"&productName="+productName;
		     return "asfd";
	}
	@GetMapping("/forgotePassword")
	public String forgotePassword() {
		return "forgotePage";
	}
	String otp;
	String otptime;
	@PostMapping("OtpGenerate")
	public String OtpGenerate(@RequestParam("gmail") String gmail,Model m)throws Exception {
		User u=ur.getByGmail(gmail);
		System.out.println("user name is "+u.getName());
		 if(u!=null) {
			 
			 Random random = new Random();

		        int otp = 100000 + random.nextInt(900000);
		        System.out.println(otp);
		        this.otp=String.valueOf(otp);
		        m.addAttribute("otp",otp);
		        MimeMessage helper = mailSender.createMimeMessage();
		        MimeMessageHelper message = new MimeMessageHelper(helper);
                 m.addAttribute("gmail",gmail);
                 message.setFrom(new InternetAddress("arvyadav936987@gmail.com", "Yadav Shopping Support"));
		        message.setTo(gmail);
		        message.setSubject("Logging in yadavShopping Application.");
		        message.setText("don,t share this OTP to anyone."+"your otp is:"+this.otp);

		        mailSender.send(helper);

		        System.out.println("Mail Sent Successfully...");
		        return "forgotePage";
		 }
		
		 
		return "forgotePage";
	}
	@PostMapping("/OtpVerification")
	public String OtpVerification( @RequestParam String otp,@RequestParam String gmail,HttpSession session,Model m) {
		if(otp.equals(this.otp)) {
			System.out.println("aaaaaaaaaaaaaaaaa"+gmail);
			
		session.setAttribute("mobile",ur.getByGmail(gmail).getMobile());
		  return "redirect:/home";
			}
		System.out.println(this.otp+"hhhh"+otp);
		m.addAttribute("otp","8575");
		m.addAttribute("invalidotp","invalidotp");
		m.addAttribute("gmail",gmail);
		return"forgotePage";
	}
	@PostMapping("/buyproduct2")
	public String buyproduct2() {
		
		return "buyproduct";
	}

	
	
}
