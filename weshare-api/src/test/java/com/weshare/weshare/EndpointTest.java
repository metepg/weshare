package com.weshare.weshare;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndpointTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(value = 1)
    public void deleteAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/bills/delete").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(value = 2)
    public void dataBaseShouldBeEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/bills/count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("0")));
    }

//    @Test
//    @Order(value = 3)
//    public void createBill() throws Exception {
//        Bill bill = new Bill("Mete", "Alepa", 30.00, "kauppa", new Date(), 15.00);
//        mvc.perform(MockMvcRequestBuilders.post("/api/bills", bill)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bill)))
//                .andExpect(status().isCreated());
//    }

    @Test
    @Order(value = 4)
    public void dataBaseShouldContainOneBill() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/bills/count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("1")));
    }


}
