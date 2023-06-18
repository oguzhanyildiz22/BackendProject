package com.microservice.UserService.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.UserService.entity.MyObject;

@RestController
@RequestMapping("api/terminal")
public class TerminalController {

	@GetMapping("/data")
	public MyResponse getData() throws IOException {
	    InputStream inputStream = getClass().getResourceAsStream("/terminals.json");
	    ObjectMapper objectMapper = new ObjectMapper();
	    MyResponse response = objectMapper.readValue(inputStream, MyResponse.class);
	    return new MyResponse("SUCCESS", response.getData());
	}

	public static class MyResponse {
	    private String type;
	    private List<MyObject> data;

	    public MyResponse(String type, List<MyObject> data) {
	        this.type = type;
	        this.data = data;
	    }
	    
	    public MyResponse() {
	       
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public List<MyObject> getData() {
	        return data;
	    }

	    public void setData(List<MyObject> data) {
	        this.data = data;
	    }
	}
    
}
