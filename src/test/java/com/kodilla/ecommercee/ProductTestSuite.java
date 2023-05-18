package com.kodilla.ecommercee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.ecommercee.controller.ProductController;
import com.kodilla.ecommercee.domain.Group;
import com.kodilla.ecommercee.domain.Product;
import com.kodilla.ecommercee.dto.ProductDto;
import com.kodilla.ecommercee.mapper.ProductMapper;
import com.kodilla.ecommercee.repository.GroupRepository;
import com.kodilla.ecommercee.repository.ProductRepository;
import com.kodilla.ecommercee.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.ServletContext;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EcommerceeApplication.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductTestSuite {

    private MockMvc mockMvc;
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductService service;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(service, productMapper)).build();
    }

    @Test
    public void whenServletContext_thenItProvidesProductController() {
        // Given
        ServletContext servletContext = webApplicationContext.getServletContext();

        // Then
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("productController"));
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsStatusOk_andGetAllProductsTest() {
        // Given
        Product product1 = new Product(1L, "Product1", "New product1",
                1, new BigDecimal(25));
        Product product2 = new Product(2L, "Product2", "New product2",
                2, new BigDecimal(50));

        // When
        productRepository.save(product1);
        productRepository.save(product2);

        // Then
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc.perform(get("/v1/products")).andExpect(status().isOk()).
                    andExpect(content().contentType(CONTENT_TYPE)).andDo(print()).andReturn();
        } catch (Exception e) {
            e.getCause();
        }

        assert mvcResult != null;
        assertEquals(CONTENT_TYPE, mvcResult.getResponse().getContentType());
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(2, productRepository.findAll().size());
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsStatusOk_andGetProductByIdTest() {
        // Given
        Product product1 = new Product(1L, "Product1", "New product1",
                1, new BigDecimal(25));
        Product product2 = new Product(2L, "Product2", "New product2",
                2, new BigDecimal(50));

        // When
        productRepository.save(product1);
        productRepository.save(product2);

        // Then
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc.perform(get("/v1/products/{productId}", product1.getProductId())).andExpect(status().isOk()).
                    andExpect(content().contentType(CONTENT_TYPE)).andDo(print()).andReturn();
        } catch (Exception e) {
            e.getCause();
        }

        assert mvcResult != null;
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(productRepository.existsById(product1.getProductId()));
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsStatusOk_andCreateProductTest() {
        // Given
        ProductDto productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setProductName("Product1");
        productDto.setProductDescription("New product1");
        productDto.setProductQuantity(1);
        productDto.setProductPrice(new BigDecimal(25));

        // When
        Product product = productMapper.mapToProduct(productDto);
        productRepository.save(product);

        // Then
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/products").
                    content(asJsonString(productDto)).contentType(MediaType.APPLICATION_JSON).
                    accept(MediaType.APPLICATION_JSON)).
                    andExpect(status().isOk()).andDo(print()).andReturn();
        } catch (Exception e) {
            e.getCause();
        }

        assert mvcResult != null;
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(productRepository.existsById(product.getProductId()));
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsStatusOk_andUpdateProductTest() {
        // Given
        ProductDto productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setProductName("Product100");
        productDto.setProductDescription("New product1");
        productDto.setProductQuantity(1);
        productDto.setProductPrice(new BigDecimal(25));

        // When
        Product product = productMapper.mapToProduct(productDto);
        productRepository.save(product);

        // Then
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.put("/v1/products", "/1").
                    content(asJsonString(productDto)).contentType(MediaType.APPLICATION_JSON).
                    accept(MediaType.APPLICATION_JSON)).
                    andExpect(status().isOk()).andDo(print()).andReturn();
        } catch (Exception e) {
            e.getCause();
        }

        assert mvcResult != null;
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(productRepository.existsById(product.getProductId()));
    }

    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsStatusOk_andDeleteProductById_andWithoutDeleteGroupTest() {
        // Given
        Product product1 = new Product(1L, "Product1", "New product1",
                1, new BigDecimal(25));

        Group group1 = new Group(1L, "Group1");
        group1.getProductList().add(product1);

        // When
        productRepository.save(product1);
        Group id = groupRepository.save(group1);

        // Then
        MvcResult mvcResult = null;
        try {
            mvcResult = this.mockMvc.perform(delete("/v1/products/{productId}", product1.getProductId())).
                    andExpect(status().isOk()).andDo(print()).andReturn();
        } catch (Exception e) {
            e.getCause();
        }

        assert mvcResult != null;
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertFalse(productRepository.existsById(product1.getProductId()));
        assertEquals(1L, group1.getGroupId().longValue());
        assertEquals(1, groupRepository.findAll().size());
        assertTrue(groupRepository.existsById(id.getGroupId()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}