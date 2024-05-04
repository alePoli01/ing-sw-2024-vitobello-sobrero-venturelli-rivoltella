package it.polimi.GC13.model;

import junit.framework.TestCase;

public class CoordinatesTest extends TestCase {
    public void testEvenVerifierOnZero() {
        //test if EvenVerifier accepts (0,0)
        int x=0;
        int y=0;
        var coordinates= new Coordinates(x,y);

        try {
            coordinates.evenVerifier();
        }catch(ForbiddenCoordinatesException fc){
            fail("coordinates: ("+x+","+y+") should not throw exception");
        }
    }
    public void testEvenVerifierOnNegatives(){
        //test if EvenVerifier can accept negative values (-1,-4)F and (-1,-3)T
        int x=-1;
        int y=-4;
        var coordinates= new Coordinates(x,y);
        try {
            coordinates.evenVerifier();
            fail("coordinates: ("+x+","+y+") should throw exception");
        }catch(ForbiddenCoordinatesException fc){
            assertEquals("("+x+"," +y+ ") are not valid coordinates",fc.getMessage());
        }
        y=-3;
        coordinates.setY(y);
        try {
            coordinates.evenVerifier();
        }catch(ForbiddenCoordinatesException fc){
            fail("coordinates: ("+x+","+y+") should not throw exception");
        }
    }

}