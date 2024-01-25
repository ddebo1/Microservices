package com.demo.microservices.CurrencyConversionService;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	
	
	
	//Direct API Call
	
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion getConvertedCurrencyValue(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		//return new CurrencyConversion(10001L, from,to,quantity, BigDecimal.ONE, BigDecimal.ONE, "");
		
		HashMap<String,String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversion.class, uriVariables);
		
		CurrencyConversion cc = responseEntity.getBody();
		
		return new CurrencyConversion(cc.getId(), from,to,quantity, 
				cc.getConversionMultiple(), quantity.multiply(cc.getConversionMultiple()), cc.getEnvironment());
		
	}
	
	@Autowired
	private CurrencyExchangeProxy proxy;
	
	
	//Sending Request using Feign
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion getConvertedCurrencyValueusingFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		//return new CurrencyConversion(10001L, from,to,quantity, BigDecimal.ONE, BigDecimal.ONE, "");
	
		
		CurrencyConversion cc = proxy.getExchange(from, to);
		
		return new CurrencyConversion(cc.getId(), from,to,quantity, 
				cc.getConversionMultiple(), quantity.multiply(cc.getConversionMultiple()), cc.getEnvironment());
		
	}
	

}
