CDI-Config
==========

CDI Config module that allows you to inject property files values directly into your Java classes.

Exemplary usage
---------------

By adding the product of this project (a regular `*.jar` file) into your `WEB-INF/lib` directory, you can use following 
syntax for injecting property files entries into your Java code:

    public MyClass {
    
        @Inject
        @ConfigurationValue("keyValue")
        Double myDoubleProp;
        
        ...
    }
    
It will scan **all `*.properties` files** directly in your classpath root directory and look for a property with 
key: `keyValue`.

Required dependency
-------------------

You can specify that the property must be defined in order to satisfy the dependency using `required` 
attribute (`true` by default):

    @Inject
    @ConfigurationValue(value="keyValue", required=false)
    String urlPath;

Default property key
--------------------

If you won't specify the property key, it will try to find a key which is a fully qualified field name. If 
this doesn't work out - it fallback to the injected field name. E.g. for such definition:

    package eu.awaketech;
    
    public MyClass {
    
        @Inject
        @ConfigurationValue
        String myName;
    }
    
it will try to find property with `eu.awaketech.MyClass.myName` key. If it won't be found, it'll try with `myName`. 
If this also fails - because of default `required=true`, it will throw an unmet CD Idependency exception. 

Requirements
============

This module was written in Java SE 7 and uses its features, so it's required to work on or using it. 
It also uses Maven 3 and TestNG with Arquillian for unit and integration tests purpose.
