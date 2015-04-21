# test-beangen
Supports unit testing by dynamically creating bean objects and populating their fields to default values.

##Getting Started
The easiest way to use the framework is to create a new BeanPropertyGenerator object and call the .get(<Class>) method while passing in the class that you would like to be fully populated. The bean generator will then attempt to instantiate the class and traverse the entire object graph and recursively initialize all of the fields. It will then return an object with all of its fields initialized according to the default values or Rules (if any have been specified)

Example:
BeanPropertyGenerator beanPropertyGenerator = new BeanPropertyGenerator();
SomeObject someObject = beanPropertyGenerator.get(SomeObject.class);

##Default Values
By default, com.gregmarut.support.beangenerator.DefaultValues simply contains a Map of Class and Object which allows the framework to know what the default values should be considering the object type. If the map contains an object for a class, whenever that class is found, it is used instead of attempting to recursively initialize the object.  

##Configuration
BeanPropertyGenerator has a Properties object which stores the instructions for how fields are populated. These can be completely customized by either changing or setting new default values for a class, map concrete classes to interfaces, or set Rule objects that allow for unique values to be set based on attributes of the field name and type.

##Examples
See the unit tests for more examples on how to use and configure the BeanPropertyGenerator to help automate your unit testing needs.
