package com.miisteuhdiack.springexceldatasave;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringExcelDataSaveApplicationTests {
    protected MockMvc mockMvc;
    public SpringExcelDataSaveApplicationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
}
