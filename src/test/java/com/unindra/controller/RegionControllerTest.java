package com.unindra.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unindra.model.response.ProvinceResponse;
import com.unindra.model.response.WebResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetProvinceListSuccess() throws Exception {
        mockMvc.perform(
                get("/api/region/provinces")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<List<ProvinceResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getData());
                    assertFalse(response.getData().isEmpty());

                    // cek minimal salah satu ACEH ada
                    boolean acehExist = response.getData().stream()
                            .anyMatch(p -> "11".equals(p.getId()) && "ACEH".equals(p.getName()));
                    assertTrue(acehExist);

                    assertEquals("95", response.getData().get(36).getId());
                    assertEquals("PAPUA PEGUNUNGAN", response.getData().get(36).getName());
                });
    }

    @Test
    void testGetRegencyListSuccess() throws Exception {
        mockMvc.perform(
                get("/api/region/provinces/32/regencies")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<List<ProvinceResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });

                    assertNotNull(response.getData());
                    assertFalse(response.getData().isEmpty());

                    assertEquals("3201", response.getData().get(0).getId());
                    assertEquals("KAB. BOGOR", response.getData().get(0).getName());
                });
    }
}

