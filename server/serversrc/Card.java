/*
Interface representing a Card.
 */

package serversrc;

public class Card extends AbstractCard{

    private CardType aCardType;

    public Card(CardType pCardType){
        this.aCardType = pCardType;
    }

    @Override
    public boolean equals(Object o){
        // if compared with itself then true
        if (o == this) {
            return true;
        }
        // check if o is instance of Card
        if (!(o instanceof Card)){
            return false;
        }

        // typecast o to Card to compare
        Card c = (Card) o;

        // Compare them by name
        return c.getName().equalsIgnoreCase(this.getName());
    }

    private String getName(){
        return this.aCardType.name();
    }
}
