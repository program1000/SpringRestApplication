package crypto;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import crypto.builder.CurrencyBuilder;
import crypto.controller.CurrencyController;
import crypto.exception.CurrencyNotFoundException;
import crypto.exception.SortTypeNotSupportedException;
import crypto.model.Currency;

@SpringBootTest
@AutoConfigureMockMvc
// Reset database
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllCurrenciesTest() throws Exception {
        CurrencyBuilder builder = new CurrencyBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );

        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
    }
    
    @Test
    public void getAllCurrenciesPageTest() throws Exception {
        CurrencyBuilder builder = new CurrencyBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        
        //size test
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES  )
                .accept( MediaType.APPLICATION_JSON ).param( "size", "3" ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //page test
        currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES  )
                .accept( MediaType.APPLICATION_JSON ).param( "size", "2" ).param( "page", "1" ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
    }

    @Test
    public void getAllCurrenciesSortTest() throws Exception {
        CurrencyBuilder builder = new CurrencyBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        
        //add new currency
        Currency newCurrency = builder.buildBitcoin();
        newCurrency.setName( "New currency" );
        newCurrency.setTicker( "NEW" );
        newCurrency.setNumberOfCoins( 250000000000l );
        newCurrency.setMarketCap( new BigDecimal( 200000000000l ) );
        String body = objectMapper.writeValueAsString( newCurrency );
        mvc.perform( MockMvcRequestBuilders.post( CurrencyController.API_CURRENCIES ).contentType( MediaType.APPLICATION_JSON ).content( body ) ).andExpect( status().isOk() );
        modifyCurrency( newCurrency, 5l );
        
        //ticker sort test
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( newCurrency );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES  )
                .accept( MediaType.APPLICATION_JSON ).param( "sort", CurrencyController.SORT_TICKER ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //name sort test
        currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( newCurrency );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES  )
                .accept( MediaType.APPLICATION_JSON ).param( "sort", CurrencyController.SORT_NAME ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //number of coins sort test
        currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( newCurrency );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES  )
                .accept( MediaType.APPLICATION_JSON ).param( "sort", CurrencyController.SORT_NUMBER_OF_COINS ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //market cap sort test
        currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( newCurrency );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES  )
                .accept( MediaType.APPLICATION_JSON ).param( "sort", CurrencyController.SORT_MARKET_CAP ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
    }
    
    @Test
    public void getAllCurrenciesSortTypeNotSupportedTest() throws Exception {
        try {
            mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES ).param( "sort", "unknown" ) );
            fail( "Should throw SortTypeNotSupportedException" );
        } catch( NestedServletException e ) {
            Throwable cause = e.getCause();
            assertTrue( cause.getClass().equals( SortTypeNotSupportedException.class ) );
            assertTrue( cause.getMessage().equals( "Sort type [unknown] is not supported" ) );
        }
    }

    @Test
    public void getCurrencyByIdTest() throws Exception {
        CurrencyBuilder builder = new CurrencyBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        Currency expectedResult = modifyCurrency( builder.buildBitcoin(), 1l );

        //check first currency
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES_ID, 1l )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( expectedResult ) ) ) );

        //check last currency
        expectedResult = modifyCurrency( builder.buildBitcoinCash(), 4l );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES_ID, 4l )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( expectedResult ) ) ) );
        
    }

    @Test
    public void getCurrencyByIdNotFoundTest() throws Exception {
        try {
            mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES_ID, 5l ) );
            fail( "Should throw CurrencyNotFoundException" );
        } catch( NestedServletException e ) {
            Throwable cause = e.getCause();
            assertTrue( cause.getClass().equals( CurrencyNotFoundException.class ) );
            assertTrue( cause.getMessage().equals( "Could not find currency with id [5]" ) );
        }
    }
    
    @Test
    public void addNewCurrencyTest() throws Exception {
        //current currencies
        CurrencyBuilder builder = new CurrencyBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //add new currency
        Currency newCurrency = builder.buildBitcoin();
        newCurrency.setName( "New currency" );
        String body = objectMapper.writeValueAsString( newCurrency );
        mvc.perform( MockMvcRequestBuilders.post( CurrencyController.API_CURRENCIES ).contentType( MediaType.APPLICATION_JSON ).content( body ) ).andExpect( status().isOk() );
        
        //new currency should be added
        currencies.add( modifyCurrency( newCurrency, 5l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
    }
    
    @Test
    public void removeCurrencyTest() throws Exception {
        //current currencies
        CurrencyBuilder builder = new CurrencyBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( builder.buildEthereum(), 2l ) );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //remove currency with id 2
        mvc.perform( MockMvcRequestBuilders.delete( CurrencyController.API_CURRENCIES_ID, 2l )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() );
        
        //new currency should be removed
        currencies.remove( 1 );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
    }
    
    @Test
    public void deleteCurrencyByIdNotFoundTest() throws Exception {
        try {
            mvc.perform( MockMvcRequestBuilders.delete( CurrencyController.API_CURRENCIES_ID, 5l ) );
            fail( "Should throw CurrencyNotFoundException" );
        } catch( NestedServletException e ) {
            Throwable cause = e.getCause();
            assertTrue( cause.getClass().equals( CurrencyNotFoundException.class ) );
            assertTrue( cause.getMessage().equals( "Could not find currency with id [5]" ) );
        }
    }
    
    @Test
    public void updateCurrencyByIdTest() throws Exception {
        //get currency with id 2
        ResultActions result = mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES_ID, 2l ).accept( MediaType.APPLICATION_JSON ) );
        String response = result.andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        Currency currency = objectMapper.readValue( response, Currency.class );
        String newTicker = "NEW";
        String newName = "Newname";
        long newNumberOfCoins = 1000;
        BigDecimal newMarketCap = new BigDecimal( 50000000l );
        
        //update existing currency
        currency.setTicker( newTicker );
        currency.setName( newName );
        currency.setNumberOfCoins( newNumberOfCoins );
        currency.setMarketCap( newMarketCap );
        String body = objectMapper.writeValueAsString( currency );
        mvc.perform( MockMvcRequestBuilders.put( CurrencyController.API_CURRENCIES_ID, currency.getId() )
                .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON ).content( body ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( body ) ) );
        
        //check all currencies
        CurrencyBuilder builder = new CurrencyBuilder();
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add( modifyCurrency( builder.buildBitcoin(), 1l ) );
        currencies.add( modifyCurrency( currency, 2l ) );
        currencies.add( modifyCurrency( builder.buildRipple(), 3l ) );
        currencies.add( modifyCurrency( builder.buildBitcoinCash(), 4l ) );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
        //update new currency
        Currency newCurrency = builder.buildRipple();
        newCurrency.setName( "New ripple" );
        modifyCurrency( newCurrency, 6l );
        body = objectMapper.writeValueAsString( newCurrency );
        result = mvc.perform( MockMvcRequestBuilders.put( CurrencyController.API_CURRENCIES_ID, newCurrency.getId() )
                .accept( MediaType.APPLICATION_JSON ).contentType( MediaType.APPLICATION_JSON ).content( body ) );
        response = result.andReturn().getResponse().getContentAsString();
        newCurrency = objectMapper.readValue( response, Currency.class );
        body = objectMapper.writeValueAsString( newCurrency );
        result.andExpect( status().isOk() ).andExpect( content().string( equalTo( body ) ) );
        
        //check all currencies
        currencies.add( newCurrency );
        mvc.perform( MockMvcRequestBuilders.get( CurrencyController.API_CURRENCIES )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( equalTo( objectMapper.writeValueAsString( currencies ) ) ) );
        
    }

    private Currency modifyCurrency( Currency currency, long id ) {
        currency.setId( id );
        currency.setMarketCap( currency.getMarketCap().setScale( 2 ) );
        return currency;
    }
}
