/*
 * Copyright 2016 the original author - Rajkumar
 */
package com.binaryfoundain.poc;

import static com.binaryfoundain.poc.util.ATCSUtils.asJsonString;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.binaryfoundain.poc.model.Aircraft;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TrafficControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loadACintoTrafficMSShouldReturnOK() throws Exception {
        // {"id":"cs01","type":"cargo","size":"small"} 
    	// id ::: <type><size><number>
        this.mockMvc.perform(post("/atcs/enqueue")
        		  .content(asJsonString(new Aircraft("cs01", "cargo", "small")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        
        // just do reset
        this.mockMvc.perform(get("/atcs/boot")).andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void dequeueTrafficShouldReturnOK() throws Exception {
    	this.mockMvc.perform(post("/atcs/enqueue")
      		  .content(asJsonString(new Aircraft("cs01", "cargo", "small")))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk());
        // just do reset
        this.mockMvc.perform(get("/atcs/boot")).andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void typePrecedanceTrafficMSShouldReturn() throws Exception {
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting cargo first
        		  .content(asJsonString(new Aircraft("cs01", "cargo", "small")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting passenger second
      		  .content(asJsonString(new Aircraft("ps02", "passenger", "small")))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("ps02")));
        
        // just do reset
        this.mockMvc.perform(get("/atcs/boot")).andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void sizePrecedanceTMSShouldReturn() throws Exception {
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting small first
        		  .content(asJsonString(new Aircraft("cs01", "cargo", "small")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting large second
      		  .content(asJsonString(new Aircraft("cl02", "cargo", "large")))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("cl02")));
        
        // just do reset
        this.mockMvc.perform(get("/atcs/boot")).andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void typeNsizePrecedanceTMSShouldReturn() throws Exception {
    	this.mockMvc.perform(post("/atcs/enqueue")  // inserting large first
        		  .content(asJsonString(new Aircraft("cl01", "cargo", "large")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting small second
        		  .content(asJsonString(new Aircraft("cs02", "cargo", "small")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting small third
      		  .content(asJsonString(new Aircraft("ps03", "passenger", "small")))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting large last
        		  .content(asJsonString(new Aircraft("pl04", "passenger", "large")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        
        // first one with large passenger
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("pl04")));
        
        // second one with small passenger
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("ps03")));
        
        // third one with large cargo
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("cl01")));
        
        // just do reset
        this.mockMvc.perform(get("/atcs/boot")).andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void timePrecedanceTMSShouldReturn() throws Exception {
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting small first
        		  .content(asJsonString(new Aircraft("cs01", "cargo", "small")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/atcs/enqueue")  // inserting large second
      		  .content(asJsonString(new Aircraft("cs02", "cargo", "small")))
      		  .contentType(MediaType.APPLICATION_JSON)
      		  .accept(MediaType.APPLICATION_JSON));
        
        // first in - first out for same category
        this.mockMvc.perform(get("/atcs/dequeue")).andDo(print()).andExpect(status().isOk())
        		.andExpect(content().string(containsString("cs01")));
        
        // just do reset
        this.mockMvc.perform(get("/atcs/boot")).andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void loadMultipleACTrafficMSShouldReturnOK() throws Exception {
        // {"id":"cs01","type":"cargo","size":"small"} 
    	// id ::: <type><size><number>
    	for(int cnt = 0; cnt > 10 ; cnt ++){
    		this.mockMvc.perform(post("/atcs/enqueue")
          		  .content(asJsonString(new Aircraft("cs0" + cnt, "cargo", "small")))
          		  .contentType(MediaType.APPLICATION_JSON)
          		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    	}
        for(int cnt = 0; cnt > 10 ; cnt ++){
        	this.mockMvc.perform(post("/atcs/enqueue")
          		  .content(asJsonString(new Aircraft("pl0" + cnt, "passenger", "large")))
          		  .contentType(MediaType.APPLICATION_JSON)
          		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
        }
    }
    
    // some negative
    
    @Test
    public void invalidTypeTMSShouldReturnMessage() throws Exception {
        // {"id":"cs01","type":"cargo","size":"small"} 
    	// id ::: <type><size><number>
        this.mockMvc.perform(post("/atcs/enqueue")
        		  .content(asJsonString(new Aircraft("cs01", "cargo0000", "small")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("Aircraft rejected due to invalid")));;
    }
    
    @Test
    public void invalidSizeTMSShouldReturnMessage() throws Exception {
        // {"id":"cs01","type":"cargo","size":"small"} 
    	// id ::: <type><size><number>
        this.mockMvc.perform(post("/atcs/enqueue")
        		  .content(asJsonString(new Aircraft("cs01", "cargo", "small22333")))
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("Aircraft rejected due to invalid")));;
    }
}
