package clientsrc;

import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import java.util.ArrayList;

// import serversrc.Player;
// import serversrc.Token;
// import serversrc.Town;

public class Route extends Image {

    // route needs to have a starting town and an ending town
    // routes carry one transportation counter maximum, but don't need to have one

    Town aStartingTown;
    Town aEndTown;
    TokenImage aToken; // TODO: there could be multiple tokens, list ?
    // road or river
    boolean isRiver = false;
    // upstream
    boolean isUpstream;
    private static ArrayList<Route> allRoutes = new ArrayList<>();

    public Route(Town pStartingTown, Town pEndTown, int minX, int maxX, int minY, int maxY, MinuetoImage image) throws MinuetoFileException {
        super(minX, maxX, minY, maxY, new MinuetoImageFile("images/black-square.png"));
        this.aStartingTown = pStartingTown;
        this.aEndTown = pEndTown;
        this.aToken = null;
        allRoutes.add(this);
    }

    // overload if it's a river
    // n = 0 means it's downstream, n = 1 means it's upstream
    public Route(Town pStartingTown, Town pEndTown, int n, int minX, int maxX, int minY, int maxY, MinuetoImage image) {
        super(minX, maxX, minY, maxY, image);
        this.isRiver = true;
        if (n == 1) {
            this.isUpstream = true;
        } else if (n == 0) {
            this.isUpstream = false;
        }
        // if n is not 1 or 0
        else {
            throw new IllegalArgumentException();
        }
        this.aStartingTown = pStartingTown;
        this.aEndTown = pEndTown;
        this.aToken = null;
        allRoutes.add(this);
    }

    public boolean getisRiver() {
        return isRiver;
    }

    public static ArrayList<Route> getAllRoutes(){
        return allRoutes;
    }

    // sets Upstream with a boolean
    public void setUpstream(boolean b) {
        isUpstream = b;
    }

    public Town getSource() {
        return this.aStartingTown;
    }

    public Town getDest() {
        return this.aEndTown;
    }

    /**
     * Place a token on a valid route.
     * 
     * Ensure that the route isn't already occupied and that the token exists
     * 
     * @param token
     */
    public void placeToken(Player player, TokenImage token) {
        assert token != null;

        if (this.aToken == null) {
            throw new IllegalArgumentException();
        } else {
            // remove token from the players inventory
            player.consumeToken(token);
            // update token field
            this.aToken = token;
            // update image
            this.setMImage(token);
        }
    }

    public void clearToken() {
        // update token field
        this.aToken = null;
        // update image
        try {
            this.setMImage(new MinuetoImageFile("images/black-square.png"));
        } catch (MinuetoFileException e) {
            e.printStackTrace();
        }
    }
}
