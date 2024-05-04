package com.kodnest.tunehub.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.service.SongService;
import com.kodnest.tunehub.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
	@Autowired
	UserService userService;
	
	@Autowired
	SongService songService;

	@GetMapping("/pay")
	public String pay() {
		return "pay";
	}
	
//	@GetMapping("/songs")
//	public String veiwSongs(Model model) {
//		List<Song> songList= songService.fetchAllSongs();
//		model.addAttribute("songs", songList);
//		//System.out.println(songList);
//		return "displaysongs";
//	}

	@SuppressWarnings("finally")
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {

		int  amount  = 2000;
		Order order=null;
		try {
			RazorpayClient razorpay=new RazorpayClient("rzp_test_NBK8IiwqeXiMLr", "LfKcTQqZAmRJpIZVeekVA7gy");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = razorpay.orders.create(orderRequest);

			String mail =  (String) session.getAttribute("email");

			User user = userService.getUser(mail);
			user.setIspremium(true);
			userService.updateUser(user);
			

		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		finally {
			return order.toString();
		}
	}
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam  String orderId, @RequestParam String paymentId,
											@RequestParam String signature) {
	    try {
	        // Initialize Razorpay client with your API key and secret
	        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_NBK8IiwqeXiMLr", 
	        								"LfKcTQqZAmRJpIZVeekVA7gy");
	        // Create a signature verification data string
	        String verificationData = orderId + "|" + paymentId;

	        // Use Razorpay's utility function to verify the signature
	        boolean isValidSignature = Utils.verifySignature(verificationData, signature, 
	        													"LfKcTQqZAmRJpIZVeekVA7gy");

	        return isValidSignature;
	    } catch (RazorpayException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	@GetMapping("/payment-success")
	public String paymentSuccess(HttpSession session){
		
		String email = (String) session.getAttribute("email");
		User user = userService.getUser(email);
		user.setIspremium(true);
		userService.updateUser(user);
		return "customerhome";
	}
	
	
	//payment-failure -> redirect to login 
	@GetMapping("/payment-failure")
	public String paymentFailure(){
		//payment-error page
		return "customerhome";
	}
}

/*
 * function verifyPayment(orderId, paymentId, signature) { $.ajax({ url:
 * "/verify", type: "POST", data: { orderId: orderId, paymentId: paymentId,
 * signature: signature }, complete: function() { // Redirect to the songs list
 * page after the server has responded to the POST request window.location.href
 * = "/songs"; } }); }
 */