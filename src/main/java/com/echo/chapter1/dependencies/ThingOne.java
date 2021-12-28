package com.echo.chapter1.dependencies;

public class ThingOne {
    private ThingTwo thingTwo;
    private ThingThree thingThree;

    public ThingOne(ThingTwo thingTwo,ThingThree thingThree){
        this.thingTwo = thingTwo;
        this.thingThree = thingThree;
    }
}
