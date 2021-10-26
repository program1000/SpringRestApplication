package crypto.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import crypto.exception.CurrencyNotFoundException;
import crypto.exception.SortTypeNotSupportedException;
import crypto.model.Currency;
import crypto.repository.CurrencyRepository;

@RestController
public class CurrencyController {

    public static final String API_CURRENCIES = "/api/currencies";
    public static final String API_CURRENCIES_ID = "/api/currencies/{id}";
    public static final String SORT_TICKER = "ticker";
    public static final String SORT_NAME = "name";
    public static final String SORT_NUMBER_OF_COINS = "number_of_coins";
    public static final String SORT_MARKET_CAP = "market_cap";
    public static final String PROPERTY_TICKER = "ticker";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_NUMBER_OF_COINS = "numberOfCoins";
    public static final String PROPERTY_MARKET_CAP = "marketCap";
    
    private final Logger log = LoggerFactory.getLogger( CurrencyController.class );
    private final Map<String,String> sortMap;

    private CurrencyRepository repository;

    public CurrencyController( CurrencyRepository repository ) {
        this.repository = repository;
        sortMap = new HashMap<String,String>();
        sortMap.put( SORT_TICKER, PROPERTY_TICKER );
        sortMap.put( SORT_NAME, PROPERTY_NAME );
        sortMap.put( SORT_NUMBER_OF_COINS, PROPERTY_NUMBER_OF_COINS );
        sortMap.put( SORT_MARKET_CAP, PROPERTY_MARKET_CAP );
    }

    // Get all currencies
    @GetMapping( API_CURRENCIES )
    public List<Currency> getAllCurrencies( @RequestParam( required = false ) String sort,
                                            @RequestParam( defaultValue = "0" ) int page, 
                                            @RequestParam( defaultValue = "10" ) int size ) {
        log.info( "Call to Rest Api: GET ALL CURRENCIES, page [{}], size[{}], sort[{}]", page, size, sort );
        if ( sort!=null && sortMap.containsKey( sort ) == false ) {
            throw new SortTypeNotSupportedException( sort );
        }
        
        PageRequest req = null;
        if( sort == null ) {
            req = PageRequest.of( page, size );
        } else {
            req = PageRequest.of( page, size, Sort.by( sortMap.get( sort ) ) );
        }
        
        Page<Currency> pageCurrencies = repository.findAll( req );
        List<Currency> result = pageCurrencies.getContent();
        log.debug( "GET ALL CURRENCIES result: {}", result );
        return result;
    }
    
    public List<Currency> getAllCurrencies(Pageable page) {
        return null;
        
    }

    //Get a currency from id
    @GetMapping( API_CURRENCIES_ID )
    public Currency getOneCurrency( @PathVariable Long id ) {
        log.info( "Call to Rest Api: GET CURRENCY [{}]", id );
        Currency result = repository.findById( id ).orElseThrow( () -> new CurrencyNotFoundException( id ) );
        log.debug( "GET CURRENCY [{}]: {}", id, result );
        return result;
    }
    
    //Add a new currency
    @PostMapping( API_CURRENCIES )
    public Currency newCurrency( @RequestBody Currency newCurrency ) {
        log.info( "Call to Rest Api: ADD NEW CURRENCY [{}]", newCurrency.getName() );
        Currency result = repository.save( newCurrency );
        log.debug( "NEW CURRENCY result: {}", result );
        return result;
    }
    
    //Remove a currency
    @DeleteMapping( API_CURRENCIES_ID )
    public void deleteCurrency( @PathVariable Long id ) {
        log.info( "Call to Rest Api: DELETE CURRENCY [{}]", id );
        if ( repository.existsById( id ) == false ) {
            throw new CurrencyNotFoundException( id );
        }
        repository.deleteById(id);
        log.debug( "DELETE CURRENCY [{}]", id );
    }
    
    //Update a currency
    @PutMapping( API_CURRENCIES_ID )
    public Currency updateCurrency( @RequestBody Currency newCurrency, @PathVariable Long id ) {
        log.info( "Call to Rest Api: UPDATE CURRENCY [{}]", id );
        return repository.findById( id ).map( currency -> {
            currency.setTicker( newCurrency.getTicker() );
            currency.setName( newCurrency.getName() );
            currency.setNumberOfCoins( newCurrency.getNumberOfCoins() );
            currency.setMarketCap( newCurrency.getMarketCap() );
            log.debug( "UPDATE EXISTING CURRENCY result: {}", currency );
            return repository.save( currency );
        } ).orElseGet( () -> {
            newCurrency.setId( id );
            log.debug( "UPDATE NEW CURRENCY result: {}", newCurrency );
            return repository.save( newCurrency );
        } );
    }

}
