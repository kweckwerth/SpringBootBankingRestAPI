package com.banking.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureWebClient
@WebMvcTest(value = BranchController.class, secure = false)
/*
test harness.  Simulate Branch objects here.
 */
public class BankApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    Branch mockBranch = new Branch("branchName","streetAddress","city","countrySubDivision",
            "country","postCode",1234,5678);

    @Test
    public void testGetBranches() throws Exception {
        Branch mockBranch1 = new Branch("branchName","streetAddress","city","countrySubDivision",
                "country","postCode",1234,5678);

        Branch mockBranch2 = new Branch("branchName","streetAddress","city","countrySubDivision",
                "country","postCode",1234,5678);

        List<Branch> mockList = new ArrayList();
        mockList.add(mockBranch1);
        mockList.add(mockBranch2);
        Mockito.when(bankService.getBranches()).thenReturn(mockList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/branch").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String strResult=result.getResponse().getContentAsString();
        String origList=mapToJson(mockList);
        assertThat(strResult).isEqualTo(origList);
    }

    @Test
    public void testGetBranchesByCity() throws Exception {
        Branch mockBranch1 = new Branch("branchName","streetAddress","city","countrySubDivision",
                "country","postCode",1234,5678);

        List<Branch> mockList = new ArrayList();
        mockList.add(mockBranch1);
        Mockito.when(bankService.getBranchesByCity(Mockito.anyString())).thenReturn(mockList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/branch/city/IRVINE").
                accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String strResult=result.getResponse().getContentAsString();
        String origList=mapToJson(mockList);
        assertThat(strResult).isEqualTo(origList);
    }

    private String mapToJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }


}

