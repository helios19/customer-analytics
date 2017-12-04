package com.ing.direct.transaction.controller;

import com.google.common.collect.Lists;
import com.ing.direct.transaction.model.Transaction;
import com.ing.direct.transaction.service.ClassificationEnum;
import com.ing.direct.transaction.service.ClassificationService;
import com.ing.direct.transaction.service.TransactionService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.ing.direct.common.utils.ClassUtils.toDate;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
public class TransactionControllerTest {

    @InjectMocks
    TransactionController controller;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ClassificationService classificationService;

    private MockMvc mvc;

    private Transaction transaction = Transaction
            .builder()
            .customer("1")
            .date(toDate("1/10/2016 2:51:23 AM"))
            .amount(BigDecimal.valueOf(23.4))
            .description("first transaction description")
            .build();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void shouldFindByCustomerIdAndMonth() throws IOException {

        when(transactionService.findByCustomerId(any(String.class)))
                .thenReturn(Lists.newArrayList(transaction));

        when(classificationService.classifyCustomer(any(List.class)))
                .thenReturn(Lists.newArrayList(ClassificationEnum.MORNING_PERSON));

        given().
                when().
                get("/transaction-summary/1/10").
                then().
                statusCode(HttpServletResponse.SC_OK).
                contentType(ContentType.JSON).
                body("customerId", equalTo("1")).
                body("month", equalTo("10")).
                body("currentBalance", is((float) 23.4)).
                body("classification[0]", equalTo(ClassificationEnum.MORNING_PERSON.name())).
                body("transactions", notNullValue()).
                log().all(true);

        verify(transactionService, times(1)).findByCustomerId(any(String.class));
        verifyNoMoreInteractions(transactionService);
        verify(classificationService, times(1)).classifyCustomer(any(List.class));
        verifyNoMoreInteractions(classificationService);
    }

}
