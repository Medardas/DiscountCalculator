# Discount Calculator

Application goal is to sequentially calculate discounts for a given list of orders.
It was written for maximum scalability, readability and maintainability. This results in code being easily readable without too much comments and discount rules being almost plugable.

#### Usage
##### Requirement:
* JRE 11

Package is available in `target` directory. It can be run without arguments or with single argument representing location of input file:
```
java -jar discount-calculator-01.jar <optional_file_location>
```
If no input provided default "input.txt" from task description is used which is packaged within the application.

#### Testing
##### Requirements:
* Maven

To run unit tests type following:
```
mvn test
```

#### Building
Building should not be necessary because package and test classes are already commited.
##### Requirements:
* Maven
* JDK 11

Following command produces the package in `target` folder:
```
maven clean package
```

#### Overall Design

Application is separated into 4 main service units which are responsible for single functional part of the application:
 * CourierService - responsible for creation of couriers. Since limited number is given in the task description they were simply hardcoded.
 * DiscountService - brings out necessary methods to utilise discount interface.
 * FileLineReader - simply reads lines from text files as separate String objects to be processed.
 * OrderCreatorService - creates orders model to work with throughout the application.

##### Discount design

For discount design **behavioral** `chain of responsabilty` design patter was chosen. It allows to implement several discount rules which can or can not be dependable on each other.
Implementation allows set of rules `IDiscountRule` to be defined in their own classes and be plugged into `DiscountContext` through chain object.   
The `DiscountCOntext` also class holds orders with applied discounts while rules themselves can be defined in any way required. Even discounts addition can be implemented as long as chain itself is logical and next rule is set.
<br>Logical order of the rules chaining is when rules with least dependencies are added first while creating chain with `RuleChainFactory`, for example, since all rules depend on accumulated discount rule, they should go after `RuleAccumulatedDiscount`  
```java
return RuleChainFactory.getInstance().chainRules(accumulatedDiscount,
                thirdPackageToLPFree,
                lowestSPackagePrice);
```
Ideally you should define rules chain through configuration but to not overengineer homework task their creation was implemented in main class.

`discount` package has 100% line test coverage. 
  