package serversrc;
/*
Instance of Game represent a single elfen game.

min 2 players
max 6 players
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.HashMap;

import networksrc.*;

public class ServerGame {

    private static final ActionManager ACK_MANAGER = ActionManager.getInstance();

    private ArrayList<Player> players;
    private int numberOfPlayers;
    public static ArrayList<Town> towns;
    public static ArrayList<Route> routes;
    public int currentRound;
    private int currentPhase;
    public int gameRoundsLimit;
    public boolean destinationTownEnabled;
    public boolean witchEnabled;
    public Mode mode;
    public TownGoldOption townGoldOption;
    public ArrayList<AbstractCard> faceDownCardPile;
    public ArrayList<AbstractCard> faceUpCardPile;
    public ArrayList<GoldCard> goldCardPile;
    public Auction auction;
    public ArrayList<Token> auctionTokenList;
    public ArrayList<Token> faceUpTokenPile;
    public TokenStack faceDownTokenStack;
    private String gameID;
    public TownGraph aTownGraph;
    public CardStack aCardStack;
    private List<AbstractCard> disposedCardPile;
    private Player startingPlayer;
    private int doingPhase3 = 1;
    

    /**
     * CONSTRUCTOR : creates an instance of Game object
     */
    public ServerGame(int numberOfPlayers, int gameRoundsLimit, boolean destinationTownEnabled, boolean witchEnabled,
            Mode mode, TownGoldOption townGoldOption, String gameID) {

        this.players = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        this.gameRoundsLimit = gameRoundsLimit;
        this.destinationTownEnabled = destinationTownEnabled;
        this.witchEnabled = witchEnabled;
        this.mode = mode;
        this.townGoldOption = townGoldOption;
        this.currentRound = 1;
        this.gameID = gameID;

        this.startingPlayer = null;

        this.faceDownCardPile = new ArrayList<>();
        this.currentPhase = 0;

        towns = new ArrayList<>();
        routes = new ArrayList<>();
        faceDownCardPile = new ArrayList<>();
        faceUpCardPile = new ArrayList<>();
        disposedCardPile = new ArrayList<>();

        // if the variant 1 is on, give player a random dest town.
        if (destinationTownEnabled) {
            int index = 0;
            for (Player p : players) {
                // set target town
                p.setTargetTown(ServerGame.getTowns().get(index));
                // increment index
                index++;
                // update client on target town
                ActionManager.getInstance().sendToSender(new UpdateDestinationTownACK(p.getTargetTown().getTownName()),
                        p.getName());
            }

        }

        // TODO: initialize faceDownCardPile, goldCardPile and auction depending on the
        // mode

        // TODO: initialize faceDownCardPile, faceUpCardPile, goldCardPile and auction
        // depending on the mode

        Town esselen = new Town("Esselen", 38, 103, 99, 152, 4);
        Town yttar = new Town("Yttar", 35, 98, 222, 274, 4);
        Town wylhien = new Town("Wylhien", 187, 234, 30, 75, 3);
        Town parundia = new Town("Parundia", 172, 241, 172, 227, 4);
        Town jaccaranda = new Town("Jaccaranda", 312, 381, 61, 119, 5);
        Town albaran = new Town("AlBaran", 280, 343, 227, 283, 7);
        Town thortmanni = new Town("Throtmanni", 451, 518, 129, 188, 3);
        Town rivinia = new Town("Rivinia", 555, 621, 205, 256, 3);
        Town tichih = new Town("Tichih", 604, 662, 79, 135, 3);
        Town ergeren = new Town("ErgEren", 716, 776, 210, 259, 5);
        Town grangor = new Town("Grangor", 49, 112, 366, 411, 5);
        Town mahdavikia = new Town("MahDavikia", 57, 136, 482, 533, 5);
        Town kihromah = new Town("Kihromah", 164, 223, 314, 367, 6);
        Town ixara = new Town("Ixara", 257, 322, 489, 534, 3);
        Town dagamura = new Town("DagAmura", 281, 339, 345, 394, 4);
        Town lapphalya = new Town("Lapphalya", 415, 482, 383, 437, 2);
        Town feodori = new Town("Feodori", 411, 472, 259, 317, 4);
        Town virst = new Town("Virst", 478, 536, 491, 541, 3);
        Town elvenhold = new Town("Elvenhold", 575, 666, 290, 370, 0);
        Town beata = new Town("Beata", 724, 779, 407, 456, 2);
        Town strykhaven = new Town("Strkhaven", 616, 679, 463, 502, 4);

        towns.add(esselen);
        towns.add(yttar);
        towns.add(wylhien);
        towns.add(parundia);
        towns.add(jaccaranda);
        towns.add(albaran);
        towns.add(thortmanni);
        towns.add(rivinia);
        towns.add(tichih);
        towns.add(ergeren);
        towns.add(grangor);
        towns.add(mahdavikia);
        towns.add(kihromah);
        towns.add(ixara);
        towns.add(dagamura);
        towns.add(lapphalya);
        towns.add(feodori);
        towns.add(virst);
        towns.add(elvenhold);
        towns.add(beata);
        towns.add(strykhaven);

        if (townGoldOption == TownGoldOption.YESRANDOM) {
            for (Town t : towns) {
                Random rand = new Random();
                int townGoldVal = rand.nextInt(7) + 1;
                t.setGoldValue(townGoldVal);

            }
        }

        // shuffle towns list
        Collections.shuffle(towns); // to be used for destination town

        // int[] city = new int[]{minX, maxX, minY, maxY};

        int[] essWyl1 = new int[] { 92, 109, 59, 75 };
        Route esselenWylhien = new Route(esselen, wylhien, RouteType.PLAIN, essWyl1);

        int[] essWyl2 = new int[] { 151, 166, 106, 121 };
        Route esselenWylhien2 = new Route(esselen, wylhien, RouteType.RIVER, essWyl2); // river

        int[] essYtt = new int[] { 40, 55, 193, 208 };
        Route esselenYttar = new Route(esselen, yttar, RouteType.WOOD, essYtt);

        int[] essPar = new int[] { 124, 139, 167, 182 };
        Route esselenParundia = new Route(esselen, parundia, RouteType.WOOD, essPar);

        int[] wylJac = new int[] { 281, 296, 61, 76 };
        Route WylhienJaccaranda = new Route(wylhien, jaccaranda, RouteType.MOUNTAIN, wylJac);

        int[] wylPar = new int[] { 172, 187, 131, 146 };
        Route WylhienParundia = new Route(wylhien, parundia, RouteType.PLAIN, wylPar);

        int[] wylAlb = new int[] { 249, 264, 135, 150 };
        Route WylhienAlbaran = new Route(wylhien, albaran, RouteType.DESERT, wylAlb);

        int[] yttPar = new int[] { 121, 136, 236, 251 };
        Route yttarParundia = new Route(yttar, parundia, RouteType.LAKE, yttPar); // lake

        int[] parGra = new int[] { 126, 141, 287, 302 };
        Route parundiaGrangor = new Route(parundia, grangor, RouteType.LAKE, parGra); // lake

        int[] parAlb = new int[] { 261, 276, 213, 228 };
        Route parundiaAlbaran = new Route(parundia, albaran, RouteType.DESERT, parAlb);

        int[] jacThr = new int[] { 384, 399, 135, 150 };
        Route jaccarandaThrotmanni = new Route(jaccaranda, thortmanni, RouteType.MOUNTAIN, jacThr);

        int[] jacTic = new int[] { 489, 504, 84, 99 };
        Route jaccarandaTichih = new Route(jaccaranda, tichih, RouteType.MOUNTAIN, jacTic);

        int[] thrAlb = new int[] { 383, 398, 203, 218 };
        Route throtmanniAlbaran = new Route(thortmanni, albaran, RouteType.DESERT, thrAlb);

        int[] troRiv = new int[] { 534, 549, 191, 206 };
        Route throtmanniRivinia = new Route(thortmanni, rivinia, RouteType.WOOD, troRiv);

        int[] thrTic = new int[] { 557, 572, 146, 161 };
        Route throtmanniTichih = new Route(thortmanni, tichih, RouteType.PLAIN, thrTic);

        int[] thrFeo = new int[] { 444, 459, 223, 238 };
        Route throtmanniFeodori = new Route(thortmanni, feodori, RouteType.DESERT, thrFeo);

        int[] kihDag = new int[] { 243, 258, 347, 362 };
        Route kihromahDagamura = new Route(kihromah, dagamura, RouteType.WOOD, kihDag);

        int[] albDag = new int[] { 303, 318, 316, 331 };
        Route albaranDagamura = new Route(albaran, dagamura, RouteType.DESERT, albDag);

        int[] dagFeo = new int[] { 366, 381, 319, 334 };
        Route dagamuraFeodori = new Route(dagamura, feodori, RouteType.DESERT, dagFeo);

        int[] yttGra1 = new int[] { 46, 61, 323, 338 };
        Route yttarGrangor = new Route(yttar, grangor, RouteType.MOUNTAIN, yttGra1);

        int[] yttGra2 = new int[] { 80, 95, 312, 327 };
        Route yttarGrangor2 = new Route(yttar, grangor, RouteType.LAKE, yttGra2); // lake

        int[] graMah1 = new int[] { 51, 66, 454, 469 };
        Route grangorMahdavikia = new Route(grangor, mahdavikia, RouteType.MOUNTAIN, graMah1);

        int[] graMah2 = new int[] { 88, 103, 443, 458 };
        Route grangorMahdavikia2 = new Route(grangor, mahdavikia, RouteType.LAKE, graMah2); // river

        int[] mahIxa1 = new int[] { 167, 182, 514, 529 };
        Route mahdavikiaIxara = new Route(mahdavikia, ixara, RouteType.RIVER, mahIxa1); // river

        int[] mahIxa2 = new int[] { 246, 261, 555, 570 };
        Route mahdavikiaIxara2 = new Route(mahdavikia, ixara, RouteType.MOUNTAIN, mahIxa2);

        int[] dagLap = new int[] { 355, 370, 405, 420 };
        Route dagamuraLapphalya = new Route(dagamura, lapphalya, RouteType.WOOD, dagLap);

        int[] ixaLap = new int[] { 370, 385, 466, 481 };
        Route ixaraLapphalya = new Route(ixara, lapphalya, RouteType.WOOD, ixaLap);

        int[] ixaDag = new int[] { 285, 300, 446, 461 };
        Route ixaraDagamura = new Route(ixara, dagamura, RouteType.WOOD, ixaDag);

        int[] ixaVir1 = new int[] { 380, 395, 549, 564 };
        Route ixaraVirst = new Route(ixara, virst, RouteType.PLAIN, ixaVir1);

        int[] ixaVir2 = new int[] { 439, 454, 553, 568 };
        Route ixaraVirst2 = new Route(ixara, virst, RouteType.RIVER, ixaVir2); // river

        int[] virLap = new int[] { 453, 468, 472, 487 };
        Route virstLapphalya = new Route(virst, lapphalya, RouteType.PLAIN, virLap);

        int[] virStr1 = new int[] { 586, 601, 561, 576 };
        Route virstStrykhaven = new Route(virst, strykhaven, RouteType.MOUNTAIN, virStr1);

        int[] virStr2 = new int[] { 579, 594, 486, 501 };
        Route virstStrykhaven2 = new Route(virst, strykhaven, RouteType.LAKE, virStr2); // lake

        int[] virElv = new int[] { 565, 580, 436, 451 };
        Route virstElvenhold = new Route(virst, elvenhold, RouteType.LAKE, virElv); // lake

        int[] lapElv = new int[] { 528, 543, 394, 409 };
        Route lapphalyaElvenhold = new Route(lapphalya, elvenhold, RouteType.PLAIN, lapElv);

        int[] beaStr = new int[] { 730, 745, 493, 508 };
        Route beataStrykhaven = new Route(beata, strykhaven, RouteType.PLAIN, beaStr);

        int[] beaElv1 = new int[] { 700, 715, 400, 415 };
        Route beataElvenhold = new Route(beata, elvenhold, RouteType.PLAIN, beaElv1);

        int[] beaElv2 = new int[] { 728, 743, 377, 392 };
        Route beataElvenhold2 = new Route(beata, elvenhold, RouteType.LAKE, beaElv2); // lake

        int[] elvStr = new int[] { 622, 637, 427, 442 };
        Route elvenholdStrykhaven = new Route(elvenhold, strykhaven, RouteType.LAKE, elvStr); // lake

        int[] elvRiv = new int[] { 637, 652, 257, 272 };
        Route elvenholdRivinia = new Route(rivinia, elvenhold, RouteType.RIVER, elvRiv); // river

        int[] rivTic = new int[] { 621, 636, 176, 191 };
        Route riviniaTichih = new Route(tichih, rivinia, RouteType.RIVER, rivTic); // river

        int[] ticErg = new int[] { 689, 704, 178, 193 };
        Route tichihErgeren = new Route(tichih, ergeren, RouteType.WOOD, ticErg);

        int[] elvErg = new int[] { 719, 734, 296, 311 };
        Route elvenholdErgeren = new Route(elvenhold, ergeren, RouteType.WOOD, elvErg);

        int[] feoRiv = new int[] { 490, 505, 259, 274 };
        Route feodoriRivinia = new Route(feodori, rivinia, RouteType.WOOD, feoRiv);

        int[] lapRiv = new int[] { 511, 526, 316, 331 };
        Route lapphalyaRivinia = new Route(lapphalya, rivinia, RouteType.WOOD, lapRiv);

        int[] feoLap = new int[] { 451, 466, 344, 359 };
        Route feodoriLapphalya = new Route(feodori, lapphalya, RouteType.WOOD, feoLap);

        int[] feoAlb = new int[] { 376, 391, 259, 274 };
        Route feodoriAlbaran = new Route(feodori, albaran, RouteType.WOOD, feoAlb);

        routes.add(parundiaGrangor);
        routes.add(WylhienAlbaran);
        routes.add(feodoriAlbaran);
        routes.add(feodoriLapphalya);
        routes.add(lapphalyaRivinia);
        routes.add(feodoriRivinia);
        routes.add(elvenholdErgeren);
        routes.add(beataElvenhold2);
        routes.add(virstStrykhaven2);
        routes.add(virstElvenhold);
        routes.add(yttarGrangor2);
        routes.add(grangorMahdavikia2);
        routes.add(mahdavikiaIxara2);
        routes.add(ixaraVirst2);
        routes.add(esselenWylhien2);
        routes.add(virstLapphalya);
        routes.add(virstStrykhaven);
        routes.add(esselenParundia);
        routes.add(esselenYttar);
        routes.add(esselenWylhien);
        routes.add(WylhienParundia);
        routes.add(WylhienJaccaranda);
        routes.add(yttarGrangor);
        routes.add(yttarParundia);
        routes.add(parundiaAlbaran);
        routes.add(albaranDagamura);
        routes.add(throtmanniAlbaran);
        routes.add(throtmanniFeodori);
        routes.add(throtmanniRivinia);
        routes.add(throtmanniTichih);
        routes.add(tichihErgeren);
        routes.add(riviniaTichih);
        routes.add(elvenholdRivinia);
        routes.add(elvenholdStrykhaven);
        routes.add(lapphalyaElvenhold);
        routes.add(beataElvenhold);
        routes.add(beataStrykhaven);
        routes.add(mahdavikiaIxara);
        routes.add(dagamuraFeodori);
        routes.add(dagamuraLapphalya);
        routes.add(ixaraDagamura);
        routes.add(ixaraLapphalya);
        routes.add(ixaraVirst);
        routes.add(jaccarandaThrotmanni);
        routes.add(jaccarandaTichih);
        routes.add(kihromahDagamura);
        routes.add(grangorMahdavikia);

        // initialize town graph
        this.aTownGraph = new TownGraph();
        this.aTownGraph.addEdges(routes);
        this.faceUpTokenPile = new ArrayList<>();

        // add all counters ingame to faceDownTokenPile
        // depending on mode, tokens are different
        if (this.mode == Mode.ELFENLAND) {
            // create tokens and add to list
            this.faceDownTokenStack = TokenStack.getFullTokenStackEL();
        } else if (this.mode == Mode.ELFENGOLD) {
            // TODO
            this.faceDownTokenStack = TokenStack.getFullTokenStackEG();
        }

        // initialize TravelCard objects
        if (this.mode == Mode.ELFENLAND) {
            //
            for (int j = 0; j < 6; j++) {
                for (int i = 0; i < 10; i++) {
                    AbstractCard newCard = new TravelCard(CardType.values()[j]);
                    faceDownCardPile.add(newCard);
                }
            }

            // initialize raft cards
            for (int i = 0; i < 12; i++) {
                AbstractCard newCard = new TravelCard(CardType.RAFT);
                faceDownCardPile.add(newCard);
            }

        }

        this.aCardStack = new CardStack(faceDownCardPile);
    }

    /**
     * Adds a player to the players arraylist. If the max number of players has
     * already been reached, throw an error
     * 
     * @param player player to add to the game
     */
    public void addPlayer(Player player) throws IndexOutOfBoundsException {
        if (players.size() <= numberOfPlayers) {
            players.add(player);
            // give player an obstacle
            Token aObstacle = new Obstacle();
            player.addToken(aObstacle);

        } else {
            throw new IndexOutOfBoundsException("The max number of players has already been reached.");
        }
    }

    // GETTER for number of players in the game instance
    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public static ArrayList<Route> getAllRoutes() {
        return routes;
    }

    public int getGameRoundsLimit() {
        return gameRoundsLimit;
    }

    public boolean isDestinationTownEnabled() {
        return destinationTownEnabled;
    }

    public boolean isWitchEnabled() {
        return witchEnabled;
    }

    public Mode getMode() {
        return mode;
    }

    public TownGoldOption getTownGoldOption() {
        return townGoldOption;
    }

    public static ArrayList<Town> getTowns() {
        return towns;
    }

    public static boolean notClickingOnATown(int x, int y) {
        for (Town t : towns) {
            if (t.minX < x && t.minY < y && t.maxX > x && t.maxY > y) {
                return false;
            }
        }

        return false;
    }

    public ArrayList<Player> getAllPlayers() {
        return players;
    }

    /**
     * 
     * @return Player object referencing the player with isTurn = true
     */
    public Player getCurrentPlayer() {
        for (Player p : this.players) {
            if (p.getIsTurn()) {
                return p;
            }
        }
        // no player's turn, shouldn't happen
        return null;
    }

    // TODO
    public void updateFaceUpToken(Token pToken) {

    }

    public String getGameID() {
        return gameID;
    }

    public void nextPlayer() {
        // if we're in auction phase
        if (currentPhase == 10) {
            // if current player is last player, remove him from biddersList
            if (auction.getBiddersList().size() == 1) {
                auction.getBiddersList().remove(0);
            }
            // if everyone else passed last player needs to pass too
            if (auction.getBiddersList().isEmpty()) {
                // everyone passed
                // note that we give the token to the player inside getWinner();
                Player winner = auction.getWinner();
                // returns null because no one bid
                if (winner == null) {
                    // return token to face down token stack
                    Token t = auction.getToken();
                    this.faceDownTokenStack.addToken(t);
                }
                // send to Client the winner
                else {
                    ACK_MANAGER.sendToSender(new AuctionWinnerACK(auction.getToken().toString()), winner.getName());
                }
                // end of auction
                if (auctionTokenList.isEmpty()) {
                    nextPhase();
                    return;
                }
                // otherwise prepare next token to be auctioned
                else {
                    auction.setToken(auctionTokenList.remove(0));
                    // send Action ACK to all players
                    ACK_MANAGER.sentToAllPlayersInGame(new AuctionACK(this.auction.getToken().toString()), this);
                    // reset auction biddersList
                    auction.setBiddersList(players);
                }
            }
            // last passed player: LastPassedPlayer and it's index : indLastPassedPlayer
            // go to next player in the biddersList
            this.auction.getLastPassedPlayer()
                    .passTurn(this.auction.getBiddersList().get(auction.getIndLastPassedPlayer()));

        } else if (currentPhase == 3) {
            // go to next player
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsTurn()) {
                    // if it's last player in list, go back to start of list
                    if (i == players.size() - 1) {
                        players.get(i).passTurn(players.get(0));
                    } else {
                        players.get(i).passTurn(players.get(i + 1));
                    }
                    break;
                }
            }
            if (doingPhase3 == 3) {
                if (didAllPlayersPassTurn() && currentPhase != 10) {
                    this.doingPhase3++;
                    nextPhase();
                    return;
                }
            } else {
                if (didAllPlayersPassTurn() && currentPhase != 10) {
                    // reset turn passed for all players
                    for (Player p : players) {
                        p.resetTurnPassed();
                    }
                    doingPhase3++;
                }
            }
        }
        // change next player
        else {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsTurn()) {
                    // if it's last player in list, go back to start of list
                    if (i == players.size() - 1) {
                        players.get(i).passTurn(players.get(0));
                    } else {
                        players.get(i).passTurn(players.get(i + 1));
                    }
                    break;
                }
            }
        }
        // can't be in auction phase
        if (didAllPlayersPassTurn() && currentPhase != 10) {
            nextPhase();
        }
    }

    public void nextPhase() {
        // make first player as starting player in server side only (can be changed to
        // get random player)
        if (this.startingPlayer == null) {
            this.startingPlayer = players.get(0);
            this.startingPlayer.setTurn(true);
            // TODO: might need to send ACK to client or not
        }

        // reset turn passed for all players
        for (Player p : players) {
            p.resetTurnPassed();
        }

        // go to next round if current phase is 7
        if (currentPhase == 7) {
            currentPhase = 1;
        } else {
            this.currentPhase++;
        }
        // do the phase
        switch (this.currentPhase) {
            case 1:
                phaseOne();
                break;
            case 2:
                phaseTwo();
                break;
            case 3:
                phaseThree();
                break;
            case 4:
                this.doingPhase3 = 1;
                phaseFour();
                break;
            case 5:
                phaseFive();
                break;
            case 6:
                phaseSix();
                break;
            case 7:
                phaseSix2();
                break;
        }
    }

    public HashMap<String, List<String>> getTokenInventoryMap() {
        HashMap<String, List<String>> playerTokens = new HashMap<>();
        for (Player p : players) {
            List<String> tokenStrings = p.getTokensInHand().stream().map((token) -> token.toString())
                    .collect(Collectors.toList());
            playerTokens.put(p.getName(), tokenStrings);
        }
        return playerTokens;
    }

    public void phaseOne() {
        // shuffle
        aCardStack.shuffle();
        HashMap<String, List<String>> cards = new HashMap<>();

        for (Player p : players) {
            p.getCards().clear();
            for (int i = 0; i < 8; i++) {
                p.addCard(aCardStack.pop());
            }
            List<String> cardsAdded = p.getCards().stream().map(
                    (card) -> card.getCardType().toString()).collect(Collectors.toList());
            cards.put(p.getName(), cardsAdded);
        }

        ACK_MANAGER.sentToAllPlayersInGame(new DealTravelCardsACK(cards), this);

        nextPhase();
    }

    public void phaseTwo() {
        faceDownTokenStack.shuffle();

        for (Player p : players) {
            Token tokenToAdd = faceDownTokenStack.pop();
            p.addToken(tokenToAdd);
        }
        ACK_MANAGER.sentToAllPlayersInGame(new DealTokenACK(this.getTokenInventoryMap()), this);

        nextPhase();

    }

    public void phaseThree() {
        if (doingPhase3 > 3)
        {
            return;
        }
        int numOfTokens = faceUpTokenPile.size();
        for (int i = 0; i < 5 - numOfTokens; i++)
            faceUpTokenPile.add(faceDownTokenStack.pop());
        final List<String> faceUpCopy = faceUpTokenPile.stream().map((token) -> token.toString())
                .collect(Collectors.toList());
        final String currentPlayerName = this.getCurrentPlayer().getName();
        // displays tokens to client
        ACK_MANAGER.sendToSender(new DisplayPhaseThreeACK(faceUpCopy), currentPlayerName);
    }

    // for planning travel routes phase (4)
    public void playerPlaceCounter(Player p, Route r, Token tok) {
        
        // remove token from player's hand
        p.consumeToken(tok);
        // add token to route r
        // tok.setRoute(r); done inside r.placetoken
        r.placeToken(tok);
    }

    public void winner(Player winner) {
        // ...
        ACK_MANAGER.sentToAllPlayersInGame(new WinnerACK(winner.getName()), this);
    }

    // new version of phaseFour
    // pre: currentPhase should be 4 right now
    public void phaseFour() {
        // server sends message ACK to client to let it know if the phase
        ACK_MANAGER.sentToAllPlayersInGame(new PlaceCounterACK(), this);
    }

    public void phaseFive() {
        // server sends message ACK to client to let it know it's phase 5, moving boot
        // phase
        ACK_MANAGER.sentToAllPlayersInGame(new MovingBootACK(), this);
    }

    public Auction getAuction() {
        return this.auction;
    }

    // TODO: auction phase and the helper functions/messages
    public void auctionPhase() {
        this.auction = new Auction(players);
        // set current phase: this.currentPhase = 10; or it's done before this ?
        // get a list of tokens with size 2 times the amount of players
        // TODO: player can pass or bid in Action network classes AuctionBidAction and
        // PassTurnAction
        // initialize auctionTokenList
        this.auctionTokenList = new ArrayList<>();
        for (int i = 0; i < (players.size() * 2); i++) {
            this.auctionTokenList.add(faceDownTokenStack.pop());
        }
        // set auction'd token
        this.auction.setToken(auctionTokenList.remove(0));
        // sends ACK to client letting them know of the token to be auctioned
        ACK_MANAGER.sentToAllPlayersInGame(new AuctionACK(this.auction.getToken().toString()), this);
    }

    // @pre we're in phase 7 (just finished phase 6 choosing token to keep)
    // this is basically phase 6.2 to complete finalizing end of round
    public void phaseSix2() {
        // change starting player by index in list
        int startingPlayerIndex = players.indexOf(startingPlayer);
        // if starting player is last in list, go back to first player in list
        if (startingPlayerIndex == players.size() - 1) {
            this.startingPlayer = players.get(0);
        } else {
            this.startingPlayer = players.get(startingPlayerIndex + 1);
        }
        // each player turns in all their transportation counters EXCEPT ONE THAT THEY
        // CHOOSE TO KEEP
        for (Player p : players) {
            Token tempTok = p.popTokenToKeep();
            List<Token> removedTokens = p.removeAllTokens();
            if (tempTok != null) {
                removedTokens.remove(tempTok); // is Token == Token overwritten by name yet ???
                p.addToken(tempTok);
            }
            // add these back to token stack
            faceDownTokenStack.addTokens(removedTokens);
            // clear token hand
            p.clearTokenHand();
        }

        // remove transportation counters from board (note this doesn't add the tokens
        // that are face up (aka up for grabs during drawing counter phase))
        for (Route r : routes) {
            // remove token deletes obstacle from game basically
            Token tok = r.removeToken();
            // check if not null
            if (tok != null) {
                // check if it's face up ?

                // reset the route field in token
                tok.resetRoute();
                // add to the tokenStack
                faceDownTokenStack.addToken(tok);
            }
        }
        faceDownTokenStack.shuffle();

        // send ACK to client for update
        // tell client their new token hand
        // and tell client to remove tokens from map
        for (Player p : players) {
            String tok = "none";
            // if tokens in hand is not empty, then there can only be one token left
            if (!p.getTokensInHand().isEmpty()) {
                tok = p.getTokensInHand().get(0).toString();
            }
            ACK_MANAGER.sendToSender(new AfterPhase6TokensACK(p.getName(), tok, this.currentRound), p.getName());
        }

        // go to next phase
        nextPhase();
    }

    // @pre we're in phase 6 (just finished phase 5 move boot)
    // finish phase
    public void phaseSix() {
        // ending game...
        if (currentRound == gameRoundsLimit) {
            // initialize score in each player's field
            for (Player p : players) {
                p.initScore();
            }

            // player with highest score wins
            // list of players with equal highest score
            List<Player> winningPlayers = new ArrayList<>();

            // if variant 2 dest town is enabled, set the scores according to rules of
            // variant 2 for each player
            if (destinationTownEnabled) {

                for (Player p : players) {
                    // find shortest path to their dest town and get int distance away (use dikstras
                    // algo)
                    int distanceAway = aTownGraph.getDistanceAway(p.getTown(), p.getTargetTown());
                    // reduce each player's score by it
                    // should be zero if player already at target town
                    // deducts score for each player
                    p.deductScore(distanceAway);
                }

            }

            int highestScore = getHighestScore();

            for (Player p : players) {
                if (p.getScore() == highestScore) {
                    winningPlayers.add(p);
                }
            }

            // if only one winning player vs multiple winning player, so the one with
            // highest number of cards in hand wins
            if (winningPlayers.size() == 1) {
                // TODO player wins
                winner(winningPlayers.get(0));
            }

            else {
                int highestNumberOfCards = 0;
                Player playerWinner = null;
                // find player with highest number of hands
                for (Player p : winningPlayers) {
                    if (highestNumberOfCards < p.getNberCards()) {
                        highestNumberOfCards = p.getNberCards();
                        playerWinner = p;
                    }
                }
                winner(playerWinner);
            }
            return;
        }

        // server sends message ACK to client to let it know it's phase 6, choose token
        // to keep
        ACK_MANAGER.sentToAllPlayersInGame(new ChoosingTokenToKeepACK(), this);

        // update round
        currentRound++;

    }

    // method that checks if all players passed turn, to know if we move on to next
    // phase/round
    public boolean didAllPlayersPassTurn() {
        // checks if all players has turnPassed as true
        for (Player p : this.players) {
            // if one player doesn't have turnPassed as true, return false
            if (!p.getTurnPassed()) {
                return false;
            }
        }
        return true;
    }

    // gets highest score from all players
    public int getHighestScore() {
        int output = 0;
        for (Player p : players) {
            if (output < p.getScore()) {
                output = p.getScore();
            }
        }
        return output;
    }

    /*
     * Notes for moving boot:
     * - It's currently the move boot phase of the game (phase 5)
     * - It's currently player's turn to move boot (Player.getIsTurn())
     * - Player (from client) sends coordinate where they clicked
     * - server receives coordinate (or receive route clicked ?)
     * - server makes sure it's a valid coordinates
     * - server checks wether or not that town is adjacent to player's town (is
     * there a route) (send message to client if not valid)
     *
     * ^^^^^^^^^^^^^^^^^^^^^^^^^ (this part might not be necessary for m7)
     * - server checks wether the player has cards required to move to that town
     * - if it doesn't then return message to client
     * - if it does then do the move and take away player's cards
     * - send meesage to client to move boot (update state on all player's screens)
     *
     * ^^^^^^^^^^^^^^^^^^^^^^^^^
     * - Player can move as many time as he wishes, (until no more moves available
     * click on end turn)
     * - goes to next player's turn
     * - if everyone passed turn (keep track of this somehow) go to next phase of
     * the game (not necessary for m7?)
     */

    // @pre: current phase == 5 and it's player's turn and town is adjacent
    public void playerMovedBoot(Player p, Route r) {
        // check if the route is valid (i.e. it's adjacent to player's town)
        // if it's valid, move boot
        if (r.getSource() == p.getTown() || r.getDest() == p.getTown()) {
            // remove the cards from the player
            System.out.println("removing player's cards on server");
            p.getCards().removeAll(r.getRequiredCards(p.getTown()));
            // get the town player's trying to go to
            Town dstTown;
            if (r.getSource().equal(p.getTown())) {
                dstTown = r.getDest();
            } else {
                dstTown = r.getSource();
            }
            // update player's town location done in Player.moveBoot(Town t)
            System.out.println("trying to moveboot to town: "+ dstTown.getTownName());
            p.moveBoot(dstTown);
            // update the town's player list is done in p.moveBoot(dstTown)

        }
    }

    public int getCurrentPhase() {
        return this.currentPhase;
    }

    public TownGraph getTownGraph() {
        return this.aTownGraph;
    }

    public Town getTownByName(String townName) {
        for (Town t : towns) {
            if (t.getTownName().equalsIgnoreCase(townName)) {
                return t;
            }
        }
        return null;
    }

    /*
     * Operation: Game::loadGame(savedGame: Game)
     * Scope: Player;
     * Messages: Player::{gameSessionCreationConfirmation;
     * gameSessionCreationFailed_e}
     * Post: Upon success, sends a confirmation message to the player that their
     * gameState has been saved. Otherwise, sends a “gameSessionCreationFailed_e”
     * message.
     */

    /*
     * Operation: Game::availableBootColors(Set{color})
     * Scope: Game; Player;
     * Messages: Player::{availableBootColors}
     * Post: Sends the player a set of boot colors available for the player to
     * choose from.
     * 
     * Operation: Game::bootColorConfirmation(bootColor: color)
     * Scope: Player; Boot;
     * New: newBoot: Boot;
     * Messages: Player::{gameState; bootColorInvalid_e};
     * Post: Sends a new game state to the player that they are allocated a boot
     * with color of their choice if their choice of color is available. If the
     * chosen colour is taken, sends the player a message, which informs them to
     * pick another boot color.
     * 
     * Operation: Game::displayFaceUpTokens(tokensToDisplay: Set{Token})
     * Scope: GUI; Game; Token;
     * Messages: GUI::{displayFaceUpTokens}
     * Post: Sends all face up tokens available for display to the GUI.
     * 
     * Operation: Game::isYourTurn(player: Player)
     * Scope: Player; Game; Auction;
     * Messages: Player::{gameState}
     * Post: Sends a new game state and notifies the player it is now their turn.
     * 
     * Operation: Game::displayFaceUpCards(cardsToDisplay: Set{Card})
     * Scope: GUI; Game; Card;
     * Messages: GUI::{displayFaceUpCard}
     * Post: Sends all face up cards available for display to the GUI.
     * 
     * Operation: Game::displayAvailableBoardMovements()
     * Scope: GUI; Game; Road; Card;
     * Messages: GUI::{displayAvailableBoardMovements}
     * Post: Prompts the player to select a card to sacrifice and highlights all
     * available routes.
     * 
     * Operation: Game::promptForCardSacrifice(numToSacrifice: int)
     * Scope: GUI; Game; Card;
     * Messages: GUI::{promptForCardSacrifice}
     * Post: Sends a new game state to the player.
     * 
     * Operation: Game::displayBlockedRoutes()
     * Scope: GUI; Game; Road; Town; Card;
     * Messages: GUI::{displayBlockedRoutes}
     * Post: Highlight all blocked routes on the map.
     * 
     * Operation: Game::promptForObstacleToCrossByWitch()
     * Scope: GUI; Game; Road;
     * Messages: GUI::{displayBlockedRoutes}
     * Post: Highlight all blocked routes on the map.
     * 
     * Operation: Game::displayUnmarkedRoutes()
     * Scope: GUI, Game;
     * Messages: GUI::{displayUnmarkedRoutes}
     * Post: The effect of playTravelCard operation is to declare that the next step
     * is to place a travel counter on the map. All unmarked routes will be
     * highlighted.
     * 
     * Operation: Game::displayMarkedRoutes()
     * Scope: GUI; Game; Token;
     * Messages: GUI::{displayMarkedRoutes}
     * Post: The effect of playObstacle operation is to declare that the next step
     * is to place an obstacle on the map. All marked routes will be highlighted.
     * 
     * Operation: Game::displayMarkedWater()
     * Scope: GUI; Game; Token;
     * Messages: GUI::{displayMarkedWater}
     * Post: The effect of playSeaMonster operation is to declare that the next step
     * is to place a sea monster on the map. All marked water will be highlighted.
     * 
     * Operation: Game::promptForCounterSacrifice()
     * Scope: GUI; Game; Token;
     * Messages: GUI::{promptForCounterSacrifice}
     * Post: The effect of playDoubleTransportSpell operation is to declare that the
     * next step is to place a double transportation counter on the map. The player
     * will be prompted for which counter they would like to sacrifice.
     * 
     * Operation: Game::promptForTwoCounterSwap()
     * Scope: Game; GUI; Road;
     * Messages: GUI::{promptForTwoCounterSwap}
     * Post: The effect of playExchangeSpell operation is to declare that the next
     * step is to place an exchange spell on the map. Prompts the player to select 2
     * counters placed on the map and confirms the swap.
     * 
     * Operation: Game::displayRoundNumberAndStartingPlayer(roundNumber: int,
     * player: Player)
     * Scope: Game, GUI
     * Messages: GUI::{displayFinalResults}
     * Post: Displays the current round number and the starting player for this
     * round.
     * 
     * Operation: Game::displayFinalResults()
     * Scope: Game; GUI
     * Messages: GUI::{displayFinalResults}
     * Post: Displays the final tallies for each of the players in the lobby.
     * 
     * Operation: Game::displayGameWinner(player: Player)
     * Scope: Game, GUI
     * Messages: GUI::{displayGameWinner}
     * Post: Display the game winner to all players.
     * 
     * Operation: Game::gameState()
     * Scope: Game; Player;
     * Messages: Player::{gameState}
     * Post: Sends a new game state to the player.
     * 
     * Operation: Game::promptToKeepTransportationCounter()
     * Scope: GUI; Game; Token;
     * Messages: GUI::{promptToKeepTransportationCounter}
     * Post: Prompts which transportation counter the player would like to keep in
     * their hand.
     * 
     * Operation: Game::displayLatestAuctionInfo(auction: Auction)
     * Scope: GUI; Auction; Token;
     * Messages: GUI::{displayLatestAuctionInfo}
     * Post: Displays the latest auction information to the player before they make
     * their bid.
     * 
     * Operation: Game::displayBidWinner()
     * Scope: GUI; Auction; Token;
     * Messages: GUI::{displayBidWinner}
     * Post: Displays the bid winner.
     * 
     * Operation: Game::displayNoBidsMade()
     * Scope: GUI; Auction;
     * Messages: GUI::{displayNoBidsMade}
     * Post: If no players have made a bid on a particular auctioned counter, then
     * display to all players that no players have made a bid.
     * 
     */

}
