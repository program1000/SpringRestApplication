# crypto-api

An example of a Spring Boot application. It uses a REST api to return market data from crypto currency.

It loads the following test data:
|ticker|name|number of coins|marketcap|
|------|----|---------------|--------:|
|BTC|Bitcoin|16.770.000|$189.580.000.000|
|ETH|Ethereum|96.710.000|$69.280.000.000|
|XRP|Ripple|38.590.000.000|$64.750.000.000|
|BCH|BitcoinCash|16.670.000|$6.902.0000.000|

The following REST calls are supported:
|method|url|returns|
|------|---|-------|
|GET|/api/currencies|all currencies|
|GET|/api/currencies/{id}|return currency with id|
|POST|/api/currencies|add new currency|
|DELETE|api/currencies/{id}|removes a currecy with id|
|PUT|api/currencies/{id}|updates a currecy with id|

Sorting and paging is also support, for example 
`GET http://localhost:8080/api/currencies?sort=ticker&page=3&size=10`
The paramater sort accepts:
* name
* ticker
* number_of_coins
* market_cap

## Logging
The file logback-spring.xml in src/main/resources can be adjusting to change logging preferences

## Tests
JUnit tests covers the REST calls.

## Webgui
A bare webapplication (Angular V1) is served at http://localhost:8080 for additional testing purpose.
