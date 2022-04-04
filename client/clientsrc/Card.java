/*
Interface representing a Card.
 */

package clientsrc;

import org.minueto.image.MinuetoImage;

// import serversrc.Card;

public abstract class Card{

    private String aName;

    public Card(){ 
        
    }

    public Card(String name) {
        // name of card
        aName = name;
    }

    // @Override
    // public boolean equals(Object o){
    //     // if compared with itself then true
    //     if (o == this) {
    //         return true;
    //     }
    //     // check if o is instance of Card
    //     if (!(o instanceof Card)){
    //         return false;
    //     }

    //     // typecast o to Card to compare
    //     Card c = (Card) o;

    //     // Compare them by name
    //     return c.getName().equalsIgnoreCase(this.aName);
    // }

    public String getName(){
        return this.aName;
    }
}