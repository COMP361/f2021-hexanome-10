package clientsrc;

// minueto
import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoFileException;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.image.*;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

import networksrc.ActionManager;
import networksrc.ChooseBootColorAction;
import networksrc.ChooseTokenToKeepAction;
import networksrc.Client;
import networksrc.CreateNewGameAction;
import networksrc.GetAvailableColorsAction;
import networksrc.GetAvailableSessionsAction;
import networksrc.LaunchGameAction;
import networksrc.LoginAction;
import networksrc.MoveBootAction;
import networksrc.PassTurnAction;
import networksrc.PlaceCounterAction;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import com.google.common.collect.ImmutableList;

public class ClientMain {

    // fields
    public static Client currentClient;
    public static User currentUser;
    public static LobbyServiceGameSession currentSession;
    public static Game currentGame;
    public static ClientPlayer currentPlayer;

    public static GUI gui;
    static MinuetoEventQueue entryScreenQueue, loginScreenQueue, moveBootQueue, lobbyScreenQueue, createGameQueue,
            savedGamesScreenQueue,
            elfenlandLobbyQueue, elfenlandQueue, chooseBootQueue, placeCounterQueue;
    public final static MinuetoFont fontArial20 = new MinuetoFont("Arial", 19, false, false);
    // make images
    public static MinuetoImage elfenlandImage;
    public static MinuetoImage elfengoldImage;

    // create a list of the players
    public static List<ClientPlayer> players;

    // TODO: fix this List<MinuetoImage> bootImages = getBootImages(bootFileNames);
    static MinuetoImage playScreenImage;
    public static MinuetoImage loginScreenImage;
    public static MinuetoImage whiteBoxImage;
    private static MinuetoImage lobbyBackground;
    private static MinuetoImage saveGameBackground;
    static MinuetoImage createGameBackground;
    static MinuetoImage createGameBackgroundElfengold;
    static MinuetoImage elfenlandSelected;
    static MinuetoImage elfenGoldSelected;
    private static MinuetoImage chooseBootBackground;
    private static MinuetoImage redBoppel;
    private static MinuetoImage blueBoppel;
    private static MinuetoImage greenBoppel;
    private static MinuetoImage blackBoppel;
    private static MinuetoImage yellowBoppel;
    private static MinuetoImage purpleBoppel;
    private static MinuetoImage lobbyElfenlandBackground;
    private static MinuetoImage lobbyElfengoldBackground;
    private static MinuetoImage roundNumberImage;
    static MinuetoImage readyGreen;
    static MinuetoImage readyWhite;
    static MinuetoImage startButton;
    public static MinuetoImage waitingForLaunch;
    public static MinuetoFont fontArial22Bold;
    static MinuetoRectangle modeDropdownRectangle;
    static MinuetoRectangle destinationTownDropdownRectangle;
    static MinuetoRectangle sizeDropdownRectangle;
    static MinuetoRectangle townGoldDropdownRectangle;
    static MinuetoRectangle witchDropdownRectangle;
    static MinuetoRectangle roundsDropdownRectangle;
    static MinuetoText modeElfenlandText;
    static MinuetoText modeElfengoldText;
    static MinuetoText destinationTownNoText;
    static MinuetoText destinationTownYesText;
    static MinuetoText rounds3Text;
    static MinuetoText rounds4Text;
    static MinuetoText size2Text;
    static MinuetoText size3Text;
    static MinuetoText size4Text;
    static MinuetoText size5Text;
    static MinuetoText size6Text;
    static MinuetoText townGoldNoText;
    static MinuetoText townGoldYesText;
    static MinuetoText townGoldYesRandText;
    static MinuetoText witchNoText;
    static MinuetoText witchYesText;
    static MinuetoRectangle nameTextField;
    static MinuetoImage winnerScreen;
    static MinuetoImage loserScreen;
    static MinuetoImage soundOnButton;
    static MinuetoImage soundOffButton;
    static boolean played;
    // currentGame.getNumberOfPlayers()
    static int numberPlayers = 2;

    public static final ActionManager ACTION_MANAGER = ActionManager.getInstance();

    static MinuetoMouseHandler entryScreenMouseHandler = new MinuetoMouseHandler() {
        @Override
        public void handleMousePress(int x, int y, int button) {
            // click on Play
            if (x <= 665 && x >= 360 && y >= 345 && y <= 445) {
                gui.currentBackground = GUI.Screen.LOGIN;
            }

            // click on Quit
            if (x <= 665 && x >= 360 && y >= 530 && y <= 640) {
                System.exit(0);
            }

            // click on mute/unmute
            if (x > 1000 && y > 740) {
                if (soundOn) {
                    soundOn = false;
                    pauseSound();
                    gui.window.draw(soundOffButton, 1000, 745);

                } else {
                    soundOn = true;
                    resumeSound();
                    gui.window.draw(soundOnButton, 1000, 745);
                }
            }
        }

        @Override
        public void handleMouseRelease(int i, int i1, int i2) {
            // Do nothing
        }

        @Override
        public void handleMouseMove(int i, int i1) {
            // Do nothing
        }
    };
    static MinuetoMouseHandler loginScreenMouseHandler = new MinuetoMouseHandler() {

        @Override
        public void handleMousePress(int x, int y, int button) {

            // CLICK INSIDE THE USERNAME BOX
            if (x <= 630 && x >= 160 && y >= 350 && y <= 400) {
                passWordSel = false;
                userNameSel = true;
            }
            // CLICK INSIDE THE PASSWORD BOX
            else if (x <= 630 && x >= 160 && y >= 440 && y <= 495) {
                userNameSel = false;
                passWordSel = true;
            } else {
                userNameSel = false;
                passWordSel = false;
            }

            // CLICK ON THE LOGIN BOX AREA
            if (x <= 235 && x >= 165 && y >= 525 && y <= 550) {
                if (passString.length() == 0 || userString.length() == 0) {

                    if (passString.length() == 0) {
                        // no password
                        String passFail = "Please enter a password";
                        MinuetoImage passwordFailed = new MinuetoText(passFail, fontArial20, MinuetoColor.RED);
                        loginScreenImage.draw(passwordFailed, 200, 450);
                    }
                    if (userString.length() == 0) {
                        // no username
                        String usernameFail = "Please enter a username";
                        MinuetoImage usernameFailed = new MinuetoText(usernameFail, fontArial20, MinuetoColor.RED);
                        loginScreenImage.draw(usernameFailed, 200, 360);
                    }
                }

                else {
                    // login
                    try {
                        if (currentClient == null) {
                            // client-server connection
                            Client client = new Client("elfenland.simui.com", 13645, userString);
                            client.start();
                            currentClient = client;
                        } else if (clientNeedsNewName) {
                            // here if the username provided does not exist
                            // associate the client with a new name on the server
                            currentClient.setName(userString);
                        }

                        // NOTE: we skip the above if-else when the password provided is wrong

                        // send login info to the server
                        ACTION_MANAGER.sendAction(new LoginAction(userString, passString));

                        // NOTE: commented out the code to create a new user
                        /*
                         * // user doesn't exist. create and login
                         * User newUser = REGISTRATOR.createNewUser(userString, passString);
                         * System.out.println("New User");
                         * currentUser = newUser;
                         */

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error: failed to login a user.");
                    }
                }
            }

            // click on mute/unmute
            if (x > 1000 && y > 740) {
                if (soundOn) {
                    soundOn = false;
                    pauseSound();
                    gui.window.draw(soundOffButton, 1000, 745);

                } else {
                    soundOn = true;
                    resumeSound();
                    gui.window.draw(soundOnButton, 1000, 745);

                }
            }
        }

        @Override
        public void handleMouseRelease(int i, int i1, int i2) {
            // Do nothing
        }

        @Override
        public void handleMouseMove(int i, int i1) {
            // Do nothing
        }
    };
    static MinuetoKeyboardHandler loginScreenKeyboardHandler = new MinuetoKeyboardHandler() {
        private boolean shift = false;

        @Override
        public void handleKeyPress(int i) {
            // press on enter key takes you to the next screen
            if (i == MinuetoKeyboard.KEY_ENTER) {
                if (passString.length() == 0 || userString.length() == 0) {

                    if (passString.length() == 0) {
                        // no password
                        String passFail = "Please enter a password";
                        MinuetoImage passwordFailed = new MinuetoText(passFail, fontArial20, MinuetoColor.RED);
                        loginScreenImage.draw(passwordFailed, 200, 450);
                    }
                    if (userString.length() == 0) {
                        // no username
                        String usernameFail = "Please enter a username";
                        MinuetoImage usernameFailed = new MinuetoText(usernameFail, fontArial20, MinuetoColor.RED);
                        loginScreenImage.draw(usernameFailed, 200, 360);
                    }
                }

                else {
                    // login
                    try {
                        if (currentClient == null) {
                            // client-server connection
                            Client client = new Client("elfenland.simui.com", 13645, userString);
                            client.start();
                            currentClient = client;
                        } else if (clientNeedsNewName) {
                            // here if the username provided does not exist
                            // associate the client with a new name on the server
                            currentClient.setName(userString);
                        }

                        // NOTE: we skip the above if-else when the password provided is wrong

                        // send login info to the server
                        ACTION_MANAGER.sendAction(new LoginAction(userString, passString));

                        // NOTE: commented out the code to create a new user
                        /*
                         * // user doesn't exist. create and login
                         * User newUser = REGISTRATOR.createNewUser(userString, passString);
                         * System.out.println("New User");
                         * currentUser = newUser;
                         */

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error: failed to login a user.");
                    }
                }
            }
            // TAB used to switch boxes
            else if (i == 9 && userNameSel) {
                userNameSel = false;
                passWordSel = true;
            } else if (i == MinuetoKeyboard.KEY_SHIFT) {
                shift = true;
            }
            // delete a char
            else if (i == MinuetoKeyboard.KEY_DELETE) {
                if (userNameSel && userString.length() > 0) {
                    userString = userString.substring(0, userString.length() - 1);
                } else if (passWordSel && passString.length() > 0) {
                    passString = passString.substring(0, passString.length() - 1);
                } else {
                    return;
                }
            }
            // uppercase or not a letter
            else if (shift || i < 65 || i > 90) {
                if (userNameSel) {
                    userString = userString + (char) i;
                } else if (passWordSel) {
                    passString = passString + (char) i;
                }
            }
            // lowercase letters
            else {
                if (userNameSel) {
                    if (i == 45) {
                        userString = userString + "_";
                    } else {
                        userString = userString + (char) (i + 32);
                    }
                } else if (passWordSel) {
                    if (i == 45) {
                        passString = passString + "_";
                    } else {
                        passString = passString + (char) (i + 32);
                    }
                }
            }
            // cover the last entry, draw username
            loginScreenImage.draw(whiteBoxImage, 160, 350);
            MinuetoImage username = new MinuetoText(userString, fontArial20, MinuetoColor.BLACK);
            loginScreenImage.draw(username, 200, 360);

            // cover the last entry, draw password
            loginScreenImage.draw(whiteBoxImage, 160, 440);

            // drawing "*" instead of showing password
            String hiddenPassword = "";
            for (int k = 0; k < passString.length(); k++) {
                hiddenPassword = hiddenPassword + "*";
            }

            // create MinuetoImage for hiddenPassword
            MinuetoImage hiddenPasswordImage = new MinuetoText(hiddenPassword, fontArial20, MinuetoColor.BLACK);
            loginScreenImage.draw(hiddenPasswordImage, 200, 450);
        }

        @Override
        public void handleKeyRelease(int i) {
            if (i == MinuetoKeyboard.KEY_SHIFT) {
                shift = false;
            }
        }

        @Override
        public void handleKeyType(char c) {
            // do nothing
        }
    };

    static void openPlayerInventory(ClientPlayer p) {

        JPanel inventory = new JPanel();
        inventory.setLayout(new BoxLayout(inventory, BoxLayout.Y_AXIS));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));

        JPanel tokenPanel = new JPanel();
        tokenPanel.setLayout(new BoxLayout(tokenPanel, BoxLayout.X_AXIS));

        // JPanel goldPanel = new JPanel();
        // goldPanel.setLayout(new BoxLayout(goldPanel, BoxLayout.X_AXIS));

        // can substitute 'Opponent's' for the actual name of the opponent
        String playerName = p.getName();
        JFrame opponentFrame = new JFrame(playerName + "'s Inventory");

        JLabel travelCardText = new JLabel("Travel Cards:");
        JLabel tokenText = new JLabel("Tokens:");
        travelCardText.setText("Travel Cards:     ");
        tokenText.setText("Tokens:     ");

        cardPanel.add(travelCardText);
        tokenPanel.add(tokenText);

        try {
            p.getTokensInHand().get(p.getTokensInHand().size() - 1).setFaceDown();
        } catch (MinuetoFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (CardSprite tCard : p.getCardsInHand()) {
            ImageIcon imageIcon = new ImageIcon(tCard.getFileAddress());
            Image scaledImage = imageIcon.getImage()
                    .getScaledInstance(72, 111, java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);
            JLabel pic = new JLabel(imageIcon);
            cardPanel.add(pic);
        }
        for (TokenSprite tCounterImage : p.getTokensInHand()) {
            if (tCounterImage.isTokenFaceDown() == true) {
                JLabel pic = new JLabel(new ImageIcon("images/elfenroads-sprites/M08small.png"));
                tokenPanel.add(pic);
            } else {
                JLabel pic = new JLabel(new ImageIcon(tCounterImage.getFileAddress()));
                tokenPanel.add(pic);
            }
        }

        inventory.add(Box.createVerticalStrut(30));
        inventory.add(cardPanel);
        inventory.add(Box.createVerticalStrut(10));
        inventory.add(tokenPanel);

        if (currentGame.getMode() == Mode.ELFENGOLD) {
            JPanel goldPanel = new JPanel();
            goldPanel.setLayout(new BoxLayout(goldPanel, BoxLayout.X_AXIS));
            JLabel goldText = new JLabel(playerName + " has " + p.getGoldAmount() + " gold.");
            goldPanel.add(goldText);
            inventory.add(Box.createVerticalStrut(10));
            inventory.add(goldPanel);
        }

        // if there are destination towns, add the destination town of the player to
        // their information
        if (currentGame.isDestinationTownEnabled() == true) {
            JPanel destTownPanel = new JPanel();
            destTownPanel.setLayout(new BoxLayout(destTownPanel, BoxLayout.X_AXIS));

            JLabel targetTownText = new JLabel(
                    "This player must reach " + p.getTargetDestinationTown().getTownName() + " as their final town!");
            destTownPanel.add(targetTownText);

            inventory.add(Box.createVerticalStrut(10));
            inventory.add(destTownPanel);
        }

        opponentFrame.add(inventory);

        // set the location of the window
        opponentFrame.setLocation(300, 200);
        opponentFrame.setSize(new Dimension(700, 300));

        opponentFrame.setVisible(true);

        try {
            p.getTokensInHand().get(p.getTokensInHand().size() - 1).setFaceUp();
        } catch (MinuetoFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static void openRouteInformation(ClientRoute r) {

        JFrame routeOverview = new JFrame("Route");

        JPanel routeInformation = new JPanel();
        routeInformation.setLayout(new BoxLayout(routeInformation, BoxLayout.Y_AXIS));

        JPanel connects = new JPanel();
        connects.setLayout(new BoxLayout(connects, BoxLayout.Y_AXIS));

        JPanel requirements = new JPanel();
        requirements.setLayout(new BoxLayout(requirements, BoxLayout.Y_AXIS));

        String connectsString = "This route connects " + r.getSourceTownString() + " and " + r.getDestTownString();
        JLabel connectsText = new JLabel(connectsString);
        connectsText.setText(connectsString);

        String requirementsString = r.formatRequirements();
        JLabel requirementsText = new JLabel(requirementsString);
        requirementsText.setText(requirementsString);

        connects.add(connectsText);
        requirements.add(requirementsText);

        routeInformation.add(Box.createVerticalStrut(30));
        routeInformation.add(connects);
        routeInformation.add(Box.createVerticalStrut(10));
        routeInformation.add(requirements);

        if (!r.getTokenOnRoute().isEmpty()) {
            JPanel tokenOnRoute = new JPanel();
            tokenOnRoute.setLayout(new BoxLayout(tokenOnRoute, BoxLayout.Y_AXIS));
            String temp = "";
            for (int i = 0; i < r.getTokenOnRoute().size(); i++) {
                temp += r.getTokenOnRoute().get(i).getTokenName().toLowerCase();
                if (i == r.getTokenOnRoute().size() - 2) {
                    temp += " and ";
                } else {
                    temp += ", ";
                }
            }
            String tokenOnRouteString = "This route currently has a " + temp + "token on it";
            JLabel tokenOnRouteText = new JLabel(tokenOnRouteString);
            tokenOnRouteText.setText(tokenOnRouteString);
            tokenOnRoute.add(tokenOnRouteText);
            routeInformation.add(Box.createVerticalStrut(10));
            routeInformation.add(tokenOnRoute);
        } else {
            JPanel tokenOnRoute = new JPanel();
            tokenOnRoute.setLayout(new BoxLayout(tokenOnRoute, BoxLayout.Y_AXIS));
            String tokenOnRouteString = "This route currently has no tokens on it";
            JLabel tokenOnRouteText = new JLabel(tokenOnRouteString);
            tokenOnRouteText.setText(tokenOnRouteString);
            tokenOnRoute.add(tokenOnRouteText);
            routeInformation.add(Box.createVerticalStrut(10));
            routeInformation.add(tokenOnRoute);
        }

        routeOverview.add(routeInformation);

        routeOverview.setLocation(300, 200);
        routeOverview.setSize(new Dimension(700, 300));

        routeOverview.setVisible(true);

    }

    static void openTownInformation(ClientTown t) {

        JPanel townInformation = new JPanel();
        townInformation.setLayout(new BoxLayout(townInformation, BoxLayout.Y_AXIS));

        JPanel nameOfTown = new JPanel();
        nameOfTown.setLayout(new BoxLayout(nameOfTown, BoxLayout.Y_AXIS));

        JPanel playerBeen = new JPanel();
        playerBeen.setLayout(new BoxLayout(playerBeen, BoxLayout.Y_AXIS));

        JPanel otherPlayersBeen = new JPanel();
        otherPlayersBeen.setLayout(new BoxLayout(otherPlayersBeen, BoxLayout.Y_AXIS));

        String townName = t.getTownName();
        JFrame townOverview = new JFrame(townName);

        JLabel currentlyLookingAtText = new JLabel("You are currently looking at " + townName);
        currentlyLookingAtText.setText("You are currently looking at " + townName);

        JLabel hasBeenText;

        ArrayList<ClientPlayer> playersThatPassed = t.playersThatPassed;

        HashSet<ClientPlayer> playersPassedNoDups = new HashSet<>();
        for (ClientPlayer p : playersThatPassed) {
            playersPassedNoDups.add(p);
        }

        String text = "The following players have passed: ";
        int count = 0;
        for (ClientPlayer p : playersPassedNoDups) {
            if (count != 0 && count != playersPassedNoDups.size()) {
                text += " and ";
            }
            text += p.getName();
            count++;
        }
        JLabel otherPlayersBeenText = new JLabel(text);

        if (t.playersThatPassed.contains(currentPlayer)) {
            hasBeenText = new JLabel("You have been to this town already");
            hasBeenText.setText("You have been to this town already");
        } else {
            hasBeenText = new JLabel("You have not been to this town yet");
            hasBeenText.setText("You have not been to this town yet");
        }
        nameOfTown.add(currentlyLookingAtText);
        playerBeen.add(hasBeenText);
        otherPlayersBeen.add(otherPlayersBeenText);

        townInformation.add(Box.createVerticalStrut(30));
        townInformation.add(nameOfTown);
        townInformation.add(Box.createVerticalStrut(10));
        townInformation.add(playerBeen);
        townInformation.add(Box.createVerticalStrut(10));
        townInformation.add(otherPlayersBeen);

        if (currentGame.getMode() == Mode.ELFENGOLD) {
            JPanel goldVal = new JPanel();
            goldVal.setLayout(new BoxLayout(goldVal, BoxLayout.Y_AXIS));

            JLabel goldValueText = new JLabel("This town has a gold value of " + t.getGoldValue());
            goldValueText.setText("This town has a gold value of " + t.getGoldValue());

            goldVal.add(goldValueText);

            townInformation.add(Box.createVerticalStrut(10));
            townInformation.add(goldVal);
        }

        townOverview.add(townInformation);

        townOverview.setLocation(300, 200);
        townOverview.setSize(new Dimension(700, 300));

        townOverview.setVisible(true);

    }

    static MinuetoMouseHandler elfenlandMouseHandler = new MinuetoMouseHandler() {

        // @Override
        // public void handleMouseMove() {
        //
        // }

        @Override
        public void handleMousePress(int x, int y, int button) {
            System.out.println("This is x: " + x + ". This is y: " + y);

            // if we left click
            if (button == 1) {
                // CLICKING ON THE OPPONENTS PROFILE

                for (ClientTown t : Game.getTowns()) {
                    if (x > t.minX && x < t.maxX && y > t.minY && y < t.minX) {
                        System.out.println("You just clicked on " + t.getTownName());
                    }
                }

                if (numberPlayers == 2) {
                    if (x > 856 && x < 984 && y > 105 && y < 132) {
                        // CLICKING ON PLAYER 1
                        openPlayerInventory(players.get(0));
                    }
                } else if (numberPlayers == 3) {
                    if (x > 856 && x < 984 && y > 105 && y < 132) {
                        // CLICKING ON PLAYER 1
                        openPlayerInventory(players.get(0));
                    } else if (x > 856 && x < 984 && y > 197 && y < 225) {
                        // CLICKING ON PLAYER 2
                        openPlayerInventory(players.get(1));
                    }
                } else if (numberPlayers == 4) {
                    if (x > 856 && x < 984 && y > 105 && y < 132) {
                        // CLICKING ON PLAYER 1
                        openPlayerInventory(players.get(0));
                    } else if (x > 856 && x < 984 && y > 197 && y < 225) {
                        // CLICKING ON PLAYER 2
                        openPlayerInventory(players.get(1));
                    } else if (x > 856 && x < 984 && y > 290 && y < 317) {
                        // CLICKING ON PLAYER 3
                        openPlayerInventory(players.get(2));
                    }
                } else if (numberPlayers == 5) {
                    if (x > 856 && x < 984 && y > 105 && y < 132) {
                        // CLICKING ON PLAYER 1
                        openPlayerInventory(players.get(0));
                    } else if (x > 856 && x < 984 && y > 197 && y < 225) {
                        // CLICKING ON PLAYER 2
                        openPlayerInventory(players.get(1));
                    } else if (x > 856 && x < 984 && y > 290 && y < 317) {
                        // CLICKING ON PLAYER 3
                        openPlayerInventory(players.get(2));
                    } else if (x > 856 && x < 984 && y > 382 && y < 409) {
                        // CLICKING ON PLAYER 4
                        openPlayerInventory(players.get(3));
                    }
                } else if (numberPlayers == 6) {
                    if (x > 856 && x < 984 && y > 105 && y < 132) {
                        // CLICKING ON PLAYER 1
                        openPlayerInventory(players.get(0));
                    } else if (x > 856 && x < 984 && y > 197 && y < 225) {
                        // CLICKING ON PLAYER 2
                        openPlayerInventory(players.get(1));
                    } else if (x > 856 && x < 984 && y > 290 && y < 317) {
                        // CLICKING ON PLAYER 3
                        openPlayerInventory(players.get(2));
                    } else if (x > 856 && x < 984 && y > 382 && y < 409) {
                        // CLICKING ON PLAYER 4
                        openPlayerInventory(players.get(3));
                    } else if (x > 856 && x < 984 && y > 474 && y < 499) {
                        // CLICKING ON PLAYER 5
                        openPlayerInventory(players.get(4));
                    }
                }

                // IF THE TURN IS PASSABLE -> PASS TURN WHEN WE CLICK HERE
                if (x > 20 && x < 130 && y > 637 && y < 712) {
                    ActionManager.getInstance().sendAction(new PassTurnAction(currentPlayer.getName()));
                }

                // IF PLAYERS TURN TO PICK A ROUTE TO MOVE TO
                if (currentGame.getCurrentPhase() == 5) {
                    for (ClientRoute r : Game.getAllRoutes()) {
                        if (x > r.getMinX() && x < r.getMaxX() && y > r.getMinY() && y < r.getMaxY()) {
                            System.out.println("You have selected the route from " + r.getDestTownString() + " to "
                                    + r.getSourceTownString());
                            pickedRoute = r;
                            // send message to server on pickedRoute
                            if (pickedRoute != null) {
                                ACTION_MANAGER.sendAction(new MoveBootAction(currentPlayer.getName(),
                                        pickedRoute.getHitbox(), r.isRiver));
                            }
                            break;
                        }
                    }
                }
                // place counter on routes phase
                if (currentGame.getCurrentPhase() == 4) {
                    List<TokenSprite> listOfTokens = ClientMain.currentPlayer.getTokensInHand();
                    // testingggg
                    if (pickedRoute != null) {
                        System.out.println("TESTING LINE 616, ROUTE NOT NULL: " + pickedRoute.getDestTownString()
                                + " to " + pickedRoute.getSourceTownString());
                    }

                    // TESTINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
                    System.out.println("Clicked on coordinates: \nx: " + x + "\ny: " + y);

                    for (ClientRoute r : Game.getAllRoutes()) {
                        // // TESTINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
                        // System.out.println("Looking at route: " + r.getDestTownString() + " to "
                        // + r.getSourceTownString() + "with hitbox: \nmax x: " + r.getMaxX() + "\nmin
                        // x: " + r.getMinX()
                        // + "\nmax y: " + r.getMaxY() + "\nmin y: " + r.getMinY());
                        if (x <= r.getMaxX() && x >= r.getMinX() && y <= r.getMaxY() && y >= r.getMinY()) {
                            // pick route
                            System.out.println("You have selected the route from " + r.getDestTownString() + " to "
                                    + r.getSourceTownString());
                            pickedRoute = r;
                            if (pickedRoute != null && pickedTok != null) {
                                System.out.println("You have selected the token: " + pickedTok.getTokenName());
                                System.out.println("Sending placeCounterAction to server with counter: "
                                        + pickedTok.getTokenName() + "\nSelected the route from "
                                        + pickedRoute.getDestTownString() + " to " + pickedRoute.getSourceTownString());
                                ActionManager.getInstance()
                                        .sendAction(new PlaceCounterAction(currentPlayer.getName(),
                                                pickedRoute.getHitbox(), pickedRoute.isRiver,
                                                pickedTok.getTokenName()));
                            }
                            break;
                        }
                    }

                    // testingggg
                    if (pickedTok != null) {
                        System.out.println("TESTING LINE 626, TOKEN NOT NULL:" + pickedTok.getTokenName());
                    }
                    if (listOfTokens.size() == 0) {
                        // do nothin :)
                    } else if (listOfTokens.size() == 1) {
                        if (x >= 642 && x <= 694 && y >= 640 && y <= 692) {
                            pickedTok = listOfTokens.get(0);
                        }
                    } else if (listOfTokens.size() == 2) {
                        if (x >= 587 && x <= 639 && y >= 640 && y <= 692) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 695 && x <= 747 && y >= 640 && y <= 692) {
                            pickedTok = listOfTokens.get(1);
                        }
                    } else if (listOfTokens.size() == 3) {
                        if (x >= 615 && x <= 667 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 709 && x <= 761 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(1);
                        }
                        if (x >= 663 && x <= 715 && y >= 696 && y <= 748) {
                            pickedTok = listOfTokens.get(2);
                        }
                    } else if (listOfTokens.size() == 4) {
                        if (x >= 615 && x <= 667 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 709 && x <= 761 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(1);
                        }
                        if (x >= 615 && x <= 667 && y >= 698 && y <= 748) {
                            pickedTok = listOfTokens.get(2);
                        }
                        if (x >= 709 && x <= 761 && y >= 698 && y <= 748) {
                            pickedTok = listOfTokens.get(3);
                        }
                    } else if (listOfTokens.size() == 5) {
                        if (x >= 592 && x <= 664 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 663 && x <= 715 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(1);
                        }
                        if (x >= 734 && x <= 786 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(2);
                        }
                        if (x >= 615 && x <= 667 && y >= 698 && y <= 750) {
                            pickedTok = listOfTokens.get(3);
                        }
                        if (x >= 709 && x <= 761 && y >= 698 && y <= 750) {
                            pickedTok = listOfTokens.get(4);
                        }
                    }
                    // testingggg
                    if (pickedTok != null) {
                        System.out.println("pickedTok is: " + pickedTok.getTokenName());
                    }
                }
                if (currentGame.getCurrentPhase() == 6) {
                    List<TokenSprite> listOfTokens = ClientMain.currentPlayer.getTokensInHand();
                    // testingggg
                    if (pickedTok != null) {
                        System.out.println("TESTING LINE 626, TOKEN NOT NULL:" + pickedTok.getTokenName());
                    }
                    if (listOfTokens.size() == 0) {
                        // do nothin :)
                    } else if (listOfTokens.size() == 1) {
                        if (x >= 642 && x <= 694 && y >= 640 && y <= 692) {
                            pickedTok = listOfTokens.get(0);
                        }
                    } else if (listOfTokens.size() == 2) {
                        if (x >= 587 && x <= 639 && y >= 640 && y <= 692) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 695 && x <= 747 && y >= 640 && y <= 692) {
                            pickedTok = listOfTokens.get(1);
                        }
                    } else if (listOfTokens.size() == 3) {
                        if (x >= 615 && x <= 667 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 709 && x <= 761 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(1);
                        }
                        if (x >= 663 && x <= 715 && y >= 696 && y <= 748) {
                            pickedTok = listOfTokens.get(2);
                        }
                    } else if (listOfTokens.size() == 4) {
                        if (x >= 615 && x <= 667 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 709 && x <= 761 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(1);
                        }
                        if (x >= 615 && x <= 667 && y >= 698 && y <= 748) {
                            pickedTok = listOfTokens.get(2);
                        }
                        if (x >= 709 && x <= 761 && y >= 698 && y <= 748) {
                            pickedTok = listOfTokens.get(3);
                        }
                    } else if (listOfTokens.size() == 5) {
                        if (x >= 592 && x <= 664 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(0);
                        }
                        if (x >= 663 && x <= 715 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(1);
                        }
                        if (x >= 734 && x <= 786 && y >= 636 && y <= 688) {
                            pickedTok = listOfTokens.get(2);
                        }
                        if (x >= 615 && x <= 667 && y >= 698 && y <= 750) {
                            pickedTok = listOfTokens.get(3);
                        }
                        if (x >= 709 && x <= 761 && y >= 698 && y <= 750) {
                            pickedTok = listOfTokens.get(4);
                        }
                    }
                    if (pickedTok != null) {
                        System.out.println(
                                "Sending TokenToKeepAction to server with counter: " + pickedTok.getTokenName());
                        ActionManager.getInstance().sendAction(
                                new ChooseTokenToKeepAction(currentPlayer.getName(), pickedTok.getTokenName()));
                    }
                }

            }

            // if we right click
            if (button == 3) {

                // iterate over all towns
                for (ClientTown t : Game.getTowns()) {
                    // we are clicking on a town
                    if (x < t.getMaxX() && x > t.getMinX() && y < t.getMaxY() && y > t.getMinY()) {
                        // temporary print statement to make sure we're clicking on a specific town
                        System.out.println("Clicking on " + t.getTownName());

                        // open a swing gui containing information about that town
                        openTownInformation(t);
                    }
                }
                for (ClientRoute r : Game.getAllRoutes()) {
                    if (x < r.getMaxX() && x > r.getMinX() && y < r.getMaxY() && y > r.getMinY()) {
                        System.out.println("You are clicking on a route");
                        openRouteInformation(r);
                    }
                }
            }

            // sound
            if (x > 1000 && y > 740) {
                if (soundOn) {
                    soundOn = false;
                    pauseSound();
                    gui.window.draw(soundOffButton, 1000, 745);

                } else {
                    soundOn = true;
                    resumeSound();
                    gui.window.draw(soundOnButton, 1000, 745);
                }
            }
        }

        @Override
        public void handleMouseRelease(int x, int y, int button) {
        }

        @Override
        public void handleMouseMove(int x, int y) {

        }

    };

    public static void clearPickedTok() {
        pickedTok = null;
    }

    public static void clearPickedRoute() {
        pickedRoute = null;
    }

    static MinuetoMouseHandler savedGamesMouseHandler = new MinuetoMouseHandler() {
        @Override
        public void handleMousePress(int x, int y, int button) {

            System.out.println("x:" + x + " y:" + y);

            if (x >= 30 && x <= 440 && y >= 680 && y <= 750) {
                // click on Back to Open Lobbies button
                displayAvailableGames();
            } else if (x >= 920 && x <= 990 && y >= 675 && y <= 745) {
                // click on the Refresh button
                displaySavedGames();
            } else {
                // click on a Join button
                for (AbstractMap.SimpleEntry<ImmutableList<Integer>, LobbyServiceGameSession> coords : savedGameButtonCoordinates) {
                    int maxX = (int) coords.getKey().get(0);
                    int minX = (int) coords.getKey().get(1);
                    int maxY = (int) coords.getKey().get(2);
                    int minY = (int) coords.getKey().get(3);

                    // TODO: fix this
                    /*
                     * if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                     * gameToJoin = coords.getValue();
                     * try {
                     * // get available boot colors
                     * currentSession = gameToJoin;
                     * ACTION_MANAGER.sendAction(
                     * new GetAvailableColorsAction(currentUser.getName(),
                     * gameToJoin.getSessionID()));
                     * } catch (Exception e) {
                     * e.printStackTrace();
                     * }
                     * }
                     */
                }
            }

            if (x > 1000 && y > 740) {
                // click on mute/unmute button
                if (soundOn) {
                    soundOn = false;
                    pauseSound();
                    gui.window.draw(soundOffButton, 1000, 745);

                } else {
                    soundOn = true;
                    resumeSound();
                    gui.window.draw(soundOnButton, 1000, 745);
                }
            }
        }

        @Override
        public void handleMouseRelease(int x, int y, int button) {
            // do nothing
        }

        @Override
        public void handleMouseMove(int x, int y) {
            // do nothing
        }
    };

    static MinuetoMouseHandler lobbyMouseHandler = new MinuetoMouseHandler() {
        @Override
        public void handleMousePress(int x, int y, int button) {

            System.out.println("x:" + x + " y:" + y);

            if (x >= 30 && x <= 440 && y >= 680 && y <= 750) {
                // click on Create New Lobby button
                gui.currentBackground = GUI.Screen.CREATELOBBY;
            } else if (x <= 0) {
                // click on Load Game button
                displaySavedGames();
            } else if (x >= 920 && x <= 990 && y >= 675 && y <= 745) {
                // click on the Refresh button
                displayAvailableGames();
            } else {
                // click on a Join button
                for (AbstractMap.SimpleEntry<ImmutableList<Integer>, LobbyServiceGameSession> coords : joinButtonCoordinates) {
                    int maxX = (int) coords.getKey().get(0);
                    int minX = (int) coords.getKey().get(1);
                    int maxY = (int) coords.getKey().get(2);
                    int minY = (int) coords.getKey().get(3);

                    if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                        gameToJoin = coords.getValue();
                        try {
                            // get available boot colors
                            currentSession = gameToJoin;
                            ACTION_MANAGER.sendAction(
                                    new GetAvailableColorsAction(currentUser.getName(), gameToJoin.getSessionID()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (x > 1000 && y > 740) {
                // click on mute/unmute button
                if (soundOn) {
                    soundOn = false;
                    pauseSound();
                    gui.window.draw(soundOffButton, 1000, 745);

                } else {
                    soundOn = true;
                    resumeSound();
                    gui.window.draw(soundOnButton, 1000, 745);
                }
            }
        }

        @Override
        public void handleMouseRelease(int x, int y, int button) {
            // do nothing
        }

        @Override
        public void handleMouseMove(int x, int y) {
            // do nothing
        }
    };

    /**
     * This handles the keyboard input on the game creation page (the name input
     * field)
     */
    static MinuetoKeyboardHandler gameScreenKeyboardHandler = new MinuetoKeyboardHandler() {
        private boolean shift = false;

        @Override
        public void handleKeyPress(int i) {
            if (i == MinuetoKeyboard.KEY_SHIFT) {
                // press shift
                shift = true;
            } else if (i == MinuetoKeyboard.KEY_DELETE) {
                // press delete
                if (nameSel && nameString.length() > 0) {
                    nameString = nameString.substring(0, nameString.length() - 1);
                }
            } else if (shift || i < 65 || i > 90) {
                // uppercase letters
                if (nameSel) {
                    nameString = nameString + (char) i;
                }
            } else {
                // lowercase letters
                if (nameSel) {
                    nameString = nameString + (char) (i + 32);
                }
            }

            // cover the last entry, draw name
            createGameBackground.draw(nameTextField, 168, 101);
            MinuetoImage name = new MinuetoText(nameString, fontArial20, MinuetoColor.BLACK);
            createGameBackground.draw(name, 178, 108);
        }

        @Override
        public void handleKeyRelease(int i) {
            if (i == MinuetoKeyboard.KEY_SHIFT) {
                shift = false;
            }
        }

        @Override
        public void handleKeyType(char c) {
            // do nothing
        }
    };
    static MinuetoMouseHandler gameScreenMouseHandler = new MinuetoMouseHandler() {
        @Override
        public void handleMousePress(int x, int y, int button) {
            System.out.println("This is x:" + x + "This is y:" + y);

            // new dropdowns
            // adjust lobbyservicegame to include new parameters

            if (x >= 168 && x <= 581 && y >= 102 && y <= 145) {
                // click on the Name text field - enable text input
                nameSel = true;
                // close the dropdowns
                sizeDropdownActive = false;
                destinationDropdownActive = false;
                modeDropdownActive = false;
                roundsDropdownActive = false;
                townGoldDropdownActive = false;
                witchDropdownActive = false;
            } else {
                // disable text input
                nameSel = false;
                if (x >= 533 & x <= 580 && y >= 176 && y <= 216) {
                    // click on Mode dropdown
                    modeDropdownActive = !modeDropdownActive;
                    // close the dropdowns
                    sizeDropdownActive = false;
                    destinationDropdownActive = false;
                    roundsDropdownActive = false;
                    townGoldDropdownActive = false;
                    witchDropdownActive = false;
                } else if (modeDropdownActive) {
                    // click on an option in the mode dropdown
                    if (x >= 270 && x <= 575 && y >= 223 && y <= 260) {
                        // click on Elfenland
                        modeSel = Mode.ELFENLAND;
                        // the number of rounds in elfenland is 3 or 4
                        if (numRoundsSel != 4) {
                            numRoundsSel = 3;
                        }
                        // show the elfenland create game options
                        createGameBackground.draw(elfenlandSelected, 0, 0);
                        // cover the last entry, draw name
                        createGameBackground.draw(nameTextField, 168, 101);
                        MinuetoText name = new MinuetoText(nameString, fontArial20, MinuetoColor.BLACK);
                        createGameBackground.draw(name, 178, 120);
                    } else if (x >= 270 && x <= 575 && y >= 270 && y <= 298) {
                        // click on Elfengold
                        modeSel = Mode.ELFENGOLD;
                        // the number of rounds in elfengold is 6
                        numRoundsSel = 6;
                        // show the elfengold create game options
                        createGameBackground.draw(createGameBackgroundElfengold, 0, 0);
                        // cover the last entry, draw name
                        createGameBackground.draw(nameTextField, 168, 101);
                        MinuetoText name = new MinuetoText(nameString, fontArial20, MinuetoColor.BLACK);
                        createGameBackground.draw(name, 178, 120);
                    }
                    modeDropdownActive = !modeDropdownActive;
                } else if (x >= 186 && x <= 230 && y >= 232 && y <= 278) {
                    // click on Size dropdown
                    sizeDropdownActive = !sizeDropdownActive;
                    // close the dropdowns
                    destinationDropdownActive = false;
                    modeDropdownActive = false;
                    roundsDropdownActive = false;
                    townGoldDropdownActive = false;
                    witchDropdownActive = false;
                } else if (sizeDropdownActive) {
                    // click on an option in the size dropdown
                    if (x >= 144 && x <= 230 && y >= 286 && y <= 328) {
                        // click on 2
                        numberPlayers = 2;
                    } else if (x >= 144 && x <= 230 && y >= 339 && y <= 374) {
                        // click on 3
                        numberPlayers = 3;
                    } else if (x >= 144 && x <= 230 && y >= 381 && y <= 413) {
                        // click on 4
                        numberPlayers = 4;
                    } else if (x >= 144 && x <= 230 && y >= 420 && y <= 458) {
                        // click on 5
                        numberPlayers = 5;
                    } else if (x >= 144 && x <= 230 && y >= 460 && y <= 500) {
                        // click on 6
                        numberPlayers = 6;
                    }
                    sizeDropdownActive = false;
                } else if (x >= 651 && x <= 700 && y >= 307 && y <= 347) {
                    // click on Destination Towns dropdown
                    destinationDropdownActive = !destinationDropdownActive;
                    // close the dropdowns
                    sizeDropdownActive = false;
                    modeDropdownActive = false;
                    roundsDropdownActive = false;
                    townGoldDropdownActive = false;
                    witchDropdownActive = false;
                } else if (destinationDropdownActive) {
                    // click on an option in the destination dropdown
                    if (x >= 373 && x <= 700 && y >= 347 && y <= 393) {
                        // click on No
                        destinationTownSel = false;
                    } else if (x >= 373 && x <= 700 && y >= 395 && y <= 436) {
                        // click on Yes
                        destinationTownSel = true;
                    }
                    destinationDropdownActive = false;
                } else if (modeSel.equals(Mode.ELFENLAND) && x >= 458 && x <= 511 && y >= 378 && y <= 420) {
                    // click on Rounds dropdown (for elfenland only)
                    roundsDropdownActive = !roundsDropdownActive;
                    // close the dropdowns
                    sizeDropdownActive = false;
                    destinationDropdownActive = false;
                    modeDropdownActive = false;
                    townGoldDropdownActive = false;
                    witchDropdownActive = false;
                } else if (roundsDropdownActive) {
                    // click on an option in the rounds dropdown
                    if (x >= 373 && x <= 513 && y >= 422 && y <= 468) {
                        // click on 3
                        numRoundsSel = 3;
                    } else if (x >= 373 && x <= 513 && y >= 468 && y <= 504) {
                        // click on 4
                        numRoundsSel = 4;
                    }
                    roundsDropdownActive = false;
                } else if (modeSel.equals(Mode.ELFENGOLD) && x >= 649 && x <= 700 && y >= 377 && y <= 419) {
                    // click on Town Gold Values dropdown (for elfengold only)
                    townGoldDropdownActive = !townGoldDropdownActive;
                    // close the dropdowns
                    sizeDropdownActive = false;
                    destinationDropdownActive = false;
                    modeDropdownActive = false;
                    roundsDropdownActive = false;
                    witchDropdownActive = false;
                } else if (townGoldDropdownActive) {
                    // click on an option in the town gold dropdown
                    if (x >= 376 && x <= 700 && y >= 423 && y <= 466) {
                        // click on No
                        townGoldOption = TownGoldOption.NO;
                    } else if (x >= 376 && x <= 700 && y >= 446 && y <= 510) {
                        // click on Yes: default
                        townGoldOption = TownGoldOption.YESDEFAULT;
                    } else if (x >= 376 && x <= 700 && y >= 510 && y <= 546) {
                        // click on Yes: random
                        townGoldOption = TownGoldOption.YESRANDOM;
                    }
                    townGoldDropdownActive = false;
                } else if (modeSel.equals(Mode.ELFENGOLD) && x >= 230 && x <= 281 && y >= 442 && y <= 490) {
                    // click on Witch dropdown (for elfengold only)
                    witchDropdownActive = !witchDropdownActive;
                    // close the dropdowns
                    sizeDropdownActive = false;
                    destinationDropdownActive = false;
                    modeDropdownActive = false;
                    roundsDropdownActive = false;
                    townGoldDropdownActive = false;
                } else if (witchDropdownActive) {
                    // click on an option in the witch dropdown
                    if (x >= 169 && x <= 513 && y >= 492 && y <= 535) {
                        // click on No
                        witchSel = false;
                    } else if (x >= 169 && x <= 513 && y >= 535 && y <= 580) {
                        // click on Yes
                        witchSel = true;
                    }
                    witchDropdownActive = false;
                } else if (x >= 310 && x <= 715 && y <= 675 && y >= 605) {
                    // close the dropdowns
                    sizeDropdownActive = false;
                    destinationDropdownActive = false;
                    modeDropdownActive = false;
                    roundsDropdownActive = false;
                    townGoldDropdownActive = false;
                    witchDropdownActive = false;
                    // click on the Create New Game button

                    // CHECK INPUTS HERE
                    boolean canCreate = true;
                    if (nameString.equals("")) {
                        // show error message when the name is empty
                        MinuetoText nameIsEmpty = new MinuetoText("Please enter a name.", fontArial22Bold,
                                MinuetoColor.RED);
                        createGameBackground.draw(nameIsEmpty, 178, 120);
                        canCreate = false;
                    }
                    // END OF CHECK INPUTS

                    // CREATE NEW GAME HERE
                    if (canCreate) { // if nameString is not empty, create a new game

                        try {
                            if (modeSel.equals(Mode.ELFENLAND)) {
                                // send request to the server to create an elfenland game
                                CreateNewGameAction createNewGameAction = new CreateNewGameAction(currentUser.getName(),
                                        nameString, numberPlayers, numRoundsSel, destinationTownSel, false, "elfenland",
                                        "no");
                                ClientMain.ACTION_MANAGER.sendAction(createNewGameAction);
                            } else if (modeSel.equals(Mode.ELFENGOLD)) {
                                // send request to the server to create an elfengold game
                                String townGoldOptionString = null;
                                if (townGoldOption.equals(TownGoldOption.NO)) {
                                    townGoldOptionString = "no";
                                } else if (townGoldOption.equals(TownGoldOption.YESDEFAULT)) {
                                    townGoldOptionString = "yes-default";
                                } else if (townGoldOption.equals(TownGoldOption.YESRANDOM)) {
                                    townGoldOptionString = "yes-random";
                                }
                                CreateNewGameAction createNewGameAction = new CreateNewGameAction(currentUser.getName(),
                                        nameString, numberPlayers, 6, destinationTownSel, witchSel, "elfengold",
                                        townGoldOptionString);
                                ClientMain.ACTION_MANAGER.sendAction(createNewGameAction);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (x >= 335 && x <= 690 && y <= 760 && y >= 695) {
                    // click on the Return to Open Lobbies button
                    displayAvailableGames();

                } else if (x > 1000 && y > 740) {
                    // click on mute/unmute button
                    if (soundOn) {
                        soundOn = false;
                        pauseSound();
                        gui.window.draw(soundOffButton, 1000, 745);
                    } else {
                        soundOn = true;
                        resumeSound();
                        gui.window.draw(soundOnButton, 1000, 745);
                    }
                }
            }

        }

        @Override
        public void handleMouseRelease(int x, int y, int button) {
            // do nothing
        }

        @Override
        public void handleMouseMove(int x, int y) {
            // do nothing
        }
    };

    static MinuetoMouseHandler chooseBootMouseHandler = new MinuetoMouseHandler() {

        @Override
        public void handleMousePress(int x, int y, int button) {
            System.out.println("x: " + x + " y: " + y);
            if (x >= 320 && x <= 705 && y >= 665 && y <= 740) {
                // Click on Confirm

                // check that a color was chosen
                if (colorChosen == null) {
                    // print error message
                    MinuetoText errorText = new MinuetoText("Please select a color.", fontArial22Bold,
                            MinuetoColor.RED);
                    gui.window.draw(errorText, 378, 526);
                } else {
                    if (currentUser.getName().equals(currentSession.getCreator())) {
                        // creator

                        // send action to the server
                        String senderName = currentUser.getName();
                        String color = colorChosen.name();
                        String gameID = currentSession.getSessionID();
                        ACTION_MANAGER.sendAction(new ChooseBootColorAction(senderName, color, gameID));
                    } else {
                        // not the creator

                        try {
                            // join the game
                            gameToJoin.join(colorChosen);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Game game = currentSession.getGame();
                    Mode currentMode = game.getMode();
                    // switch backgrounds depending on the game mode
                    if (currentMode.equals(Mode.ELFENLAND)) {
                        gui.currentBackground = GUI.Screen.LOBBYELFENLAND;
                        gui.window.draw(lobbyElfenlandBackground, 0, 0);
                    } else if (currentMode.equals(Mode.ELFENGOLD)) {
                        gui.currentBackground = GUI.Screen.LOBBYELFENGOLD;
                        gui.window.draw(lobbyElfengoldBackground, 0, 0);
                    }
                    // display game info
                    displayLobbyInfo();
                }
            } else {
                // Click on a Color
                for (AbstractMap.SimpleEntry<ImmutableList<Integer>, Color> coords : colorButtonCoordinates) {
                    int maxX = (int) coords.getKey().get(0);
                    int minX = (int) coords.getKey().get(1);
                    int maxY = (int) coords.getKey().get(2);
                    int minY = (int) coords.getKey().get(3);

                    if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                        // select the color
                        colorChosen = coords.getValue();
                        System.out.println("chose: " + colorChosen);
                        // display a boppel on the confirm button
                        if (colorChosen.equals(Color.BLACK)) {
                            gui.window.draw(blackBoppel, 640, 691);
                        } else if (colorChosen.equals(Color.BLUE)) {
                            gui.window.draw(blueBoppel, 640, 691);
                        } else if (colorChosen.equals(Color.YELLOW)) {
                            gui.window.draw(yellowBoppel, 640, 691);
                        } else if (colorChosen.equals(Color.GREEN)) {
                            gui.window.draw(greenBoppel, 640, 691);
                        } else if (colorChosen.equals(Color.PURPLE)) {
                            gui.window.draw(purpleBoppel, 640, 691);
                        } else if (colorChosen.equals(Color.RED)) {
                            gui.window.draw(redBoppel, 640, 691);
                        }
                    }
                }
            }
        }

        @Override
        public void handleMouseMove(int x, int y) {
            // TODO Auto-generated method stub

        }

        @Override
        public void handleMouseRelease(int x, int y, int button) {
            // TODO Auto-generated method stub

        }

    };

    // keep track of route and token
    private static ClientRoute pickedRoute = null;
    private static TokenSprite pickedTok = null;

    // not using this anymore
    static MinuetoMouseHandler placeCounterMouseHandler = new MinuetoMouseHandler() {
        @Override
        public void handleMouseMove(int arg0, int arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public void handleMousePress(int x, int y, int button) {
            // do nothing
        }

        @Override
        public void handleMouseRelease(int arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

        }

    };

    static MinuetoMouseHandler elfenLandLobbyMouseHandler = new MinuetoMouseHandler() {
        @Override
        public void handleMousePress(int x, int y, int button) {
            System.out.println("x: " + x + "y: " + y);

            if (!currentUser.getName().equals(currentSession.getCreator())) {
                /*
                 * // for users that are not the creator
                 * if (x >= 825 && x <= 1000 && y >= 675 && y <= 735) {
                 * // click on Leave
                 * REGISTRATOR.leaveGame(currentSession, currentUser);
                 * // return to lobby screen
                 * displayAvailableGames();
                 * gui.currentBackground = GUI.Screen.LOBBY;
                 * } else if (x >= 822 & x <= 998 && y <= 655 && y >= 585) {
                 * // click on Ready button: only works if you are not ready, else nothing
                 * happens
                 * if (!currentUser.isReady()) {
                 * // set user to ready
                 * currentUser.toggleReady();
                 * // draw the green ready image
                 * lobbyElfenlandBackground.draw(readyGreen, 823, 581);
                 * // TODO: display Ready next to the player's name
                 * // TODO: notify all players that this player is ready
                 * }
                 * }
                 */
            } else {
                // for the creator
                if (currentSession.isLaunchable() && x >= 825 && x <= 1000 && y >= 580 && y <= 735) {
                    // send to the server
                    ClientMain.ACTION_MANAGER
                            .sendAction(new LaunchGameAction(currentUser.getName(), currentSession.getSessionID()));
                }
            }

            if (x >= 710 && x <= 800 && y >= 700 && y <= 735) {
                // click on Send Message button
            } else if (x > 1000 && y > 740) {
                // click on mute/unmute button
                if (soundOn) {
                    soundOn = false;
                    pauseSound();
                    gui.window.draw(soundOffButton, 1000, 745);

                } else {
                    soundOn = true;
                    resumeSound();
                    gui.window.draw(soundOnButton, 1000, 745);
                }
            }
        }

        @Override
        public void handleMouseRelease(int x, int y, int button) {
            // do nothing
        }

        @Override
        public void handleMouseMove(int x, int y) {
            // do nothing
        }
    };

    // for login screen queue
    private static boolean userNameSel = false;
    private static boolean passWordSel = false;
    public static String userString = "";
    public static String passString = "";
    public static boolean clientNeedsNewName = false;

    // for mute button
    private static boolean soundOn = true;
    private static Clip loadedClip;
    private static long clipPos;
    private static boolean soundStarted = false;

    private static boolean nameSel = false;
    private static String nameString = "";
    private static Mode modeSel = Mode.ELFENLAND;
    private static boolean destinationTownSel = false;
    private static int numRoundsSel = 3;
    private static TownGoldOption townGoldOption = TownGoldOption.NO;
    private static boolean witchSel = false;
    private static boolean modeDropdownActive = false;
    private static boolean sizeDropdownActive = false;
    private static boolean destinationDropdownActive = false;
    private static boolean roundsDropdownActive = false;
    private static boolean witchDropdownActive = false;
    private static boolean townGoldDropdownActive = false;
    private static LobbyServiceGameSession gameToJoin;

    // create window that will contain our game - stays in Main (or not lol)
    public static final MinuetoWindow WINDOW = new MinuetoFrame(1024, 768, true);;

    // for lobbyMouseHandler
    private static ArrayList<AbstractMap.SimpleEntry<ImmutableList<Integer>, LobbyServiceGameSession>> joinButtonCoordinates = new ArrayList<>();

    // for savedGamesMouseHandler
    private static ArrayList<AbstractMap.SimpleEntry<ImmutableList<Integer>, LobbyServiceGameSession>> savedGameButtonCoordinates = new ArrayList<>();

    // for chooseBootMouseHandler
    private static ArrayList<AbstractMap.SimpleEntry<ImmutableList<Integer>, Color>> colorButtonCoordinates = new ArrayList<>();
    private static Color colorChosen;

    // ******************************************MAIN CODE STARTS
    // HERE********************************************
    public static void main(String[] args) throws IOException {

        try {
            elfengoldImage = new MinuetoImageFile("images/elfengold.png");
            elfenlandImage = new MinuetoImageFile("images/elfenland.png");
            playScreenImage = new MinuetoImageFile("images/play.png");
            loginScreenImage = new MinuetoImageFile("images/login.png");
            whiteBoxImage = new MinuetoRectangle(470, 50, MinuetoColor.WHITE, true);
            // choose boot
            chooseBootBackground = new MinuetoImageFile("images/choose-boot-screen.png");
            redBoppel = new MinuetoImageFile("images/böppels-and-boots/böppel-red.png");
            blueBoppel = new MinuetoImageFile("images/böppels-and-boots/böppel-blue.png");
            greenBoppel = new MinuetoImageFile("images/böppels-and-boots/böppel-green.png");
            blackBoppel = new MinuetoImageFile("images/böppels-and-boots/böppel-black.png");
            yellowBoppel = new MinuetoImageFile("images/böppels-and-boots/böppel-yellow.png");
            purpleBoppel = new MinuetoImageFile("images/böppels-and-boots/böppel-purple.png");
            // lobby
            lobbyBackground = new MinuetoImageFile("images/open-lobbies.png");
            lobbyElfenlandBackground = new MinuetoImageFile("images/game-lobby-elfenland.png");
            lobbyElfengoldBackground = new MinuetoImageFile("images/game-lobby-elfengold.png");
            readyGreen = new MinuetoImageFile("images/ready-button-green.png");
            readyWhite = new MinuetoImageFile("images/ready-button-white.png");
            startButton = new MinuetoImageFile("images/blue-launch-button.png");
            waitingForLaunch = new MinuetoImageFile("images/waiting-for-launch.png");
            // saved game
            saveGameBackground = new MinuetoImageFile("images/saved-games.png");
            // Create Game
            createGameBackground = new MinuetoImageFile("images/create-game-elfenland.png");
            createGameBackgroundElfengold = new MinuetoImageFile("images/create-game-elfengold.png");
            nameTextField = new MinuetoRectangle(412, 44, MinuetoColor.WHITE, true);
            elfenlandSelected = new MinuetoImageFile("images/create-game-elfenland.png");
            elfenGoldSelected = new MinuetoImageFile("images/create-game-elfengold.png");
            // Create Game dropdowns
            fontArial22Bold = new MinuetoFont("Arial", 22, true, false);
            modeDropdownRectangle = new MinuetoRectangle(313, 84, MinuetoColor.WHITE, true);
            sizeDropdownRectangle = new MinuetoRectangle(91, 225, MinuetoColor.WHITE, true);
            destinationTownDropdownRectangle = new MinuetoRectangle(324, 88, MinuetoColor.WHITE, true);
            roundsDropdownRectangle = new MinuetoRectangle(137, 86, MinuetoColor.WHITE, true);
            townGoldDropdownRectangle = new MinuetoRectangle(326, 129, MinuetoColor.WHITE, true);
            witchDropdownRectangle = new MinuetoRectangle(114, 92, MinuetoColor.WHITE, true);
            modeElfenlandText = new MinuetoText("Elfenland", fontArial22Bold, MinuetoColor.BLACK);
            modeElfengoldText = new MinuetoText("Elfengold", fontArial22Bold, MinuetoColor.BLACK);
            destinationTownNoText = new MinuetoText("No", fontArial22Bold, MinuetoColor.BLACK);
            destinationTownYesText = new MinuetoText("Yes", fontArial22Bold, MinuetoColor.BLACK);
            rounds3Text = new MinuetoText("3", fontArial22Bold, MinuetoColor.BLACK);
            rounds4Text = new MinuetoText("4", fontArial22Bold, MinuetoColor.BLACK);
            size2Text = new MinuetoText("2", fontArial22Bold, MinuetoColor.BLACK);
            size3Text = new MinuetoText("3", fontArial22Bold, MinuetoColor.BLACK);
            size4Text = new MinuetoText("4", fontArial22Bold, MinuetoColor.BLACK);
            size5Text = new MinuetoText("5", fontArial22Bold, MinuetoColor.BLACK);
            size6Text = new MinuetoText("6", fontArial22Bold, MinuetoColor.BLACK);
            townGoldNoText = new MinuetoText("No", fontArial22Bold, MinuetoColor.BLACK);
            townGoldYesText = new MinuetoText("Yes: default values", fontArial22Bold, MinuetoColor.BLACK);
            townGoldYesRandText = new MinuetoText("Yes: random values", fontArial22Bold, MinuetoColor.BLACK);
            witchNoText = new MinuetoText("No", fontArial22Bold, MinuetoColor.BLACK);
            witchYesText = new MinuetoText("Yes", fontArial22Bold, MinuetoColor.BLACK);
            winnerScreen = new MinuetoImageFile("images/winner-screen.png");
            loserScreen = new MinuetoImageFile("images/loser-screen.png");
            // mute button
            soundOnButton = new MinuetoImageFile("images/SoundImages/muted.png");
            soundOffButton = new MinuetoImageFile("images/SoundImages/unmuted.png");
            played = false;

        } catch (MinuetoFileException e) {
            System.out.println("Could not load image file");
            return;
        }

        // Play Music
        if (!soundStarted) {
            playSound("music/flute.mid");
            soundStarted = true;
        }

        gui = new GUI(WINDOW, GUI.Screen.MENU);
        WINDOW.setMaxFrameRate(0);

        // make window visible
        gui.window.setVisible(true);

        // create entry screen mouse handler
        entryScreenQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(entryScreenMouseHandler, entryScreenQueue);

        // create login screen keyboard handler
        loginScreenQueue = new MinuetoEventQueue();
        gui.window.registerKeyboardHandler(loginScreenKeyboardHandler, loginScreenQueue);
        gui.window.registerMouseHandler(loginScreenMouseHandler, loginScreenQueue);
        elfenlandQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(elfenlandMouseHandler, elfenlandQueue);

        // lobby screen mouse handler
        lobbyScreenQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(lobbyMouseHandler, lobbyScreenQueue);

        // saved game screen mouse handler
        savedGamesScreenQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(savedGamesMouseHandler, savedGamesScreenQueue);

        // create game screen keyboard handler
        createGameQueue = new MinuetoEventQueue();
        gui.window.registerKeyboardHandler(gameScreenKeyboardHandler, createGameQueue);

        // create game screen mouse handler
        gui.window.registerMouseHandler(gameScreenMouseHandler, createGameQueue);

        // mouse handler for choose boot
        chooseBootQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(chooseBootMouseHandler, chooseBootQueue);

        // mouse handler for elfenland lobby
        elfenlandLobbyQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(elfenLandLobbyMouseHandler, elfenlandLobbyQueue);

        // mouse handler for place counter
        placeCounterQueue = new MinuetoEventQueue();
        gui.window.registerMouseHandler(placeCounterMouseHandler, placeCounterQueue);

        int once = 1;
        // draw on the window
        while (true) {
            if (gui.currentBackground == GUI.Screen.MENU) {
                gui.window.draw(playScreenImage, 0, 0);
                while (entryScreenQueue.hasNext()) {
                    entryScreenQueue.handle();
                }
            } else if (gui.currentBackground == GUI.Screen.LOGIN) {
                gui.window.draw(loginScreenImage, 0, 0);
                while (loginScreenQueue.hasNext()) {
                    loginScreenQueue.handle();
                }

            } else if (gui.currentBackground == GUI.Screen.LOBBY) {
                while (lobbyScreenQueue.hasNext()) {
                    lobbyScreenQueue.handle();
                }

            } else if (gui.currentBackground == GUI.Screen.SAVEDGAMES) {
                while (savedGamesScreenQueue.hasNext()) {
                    savedGamesScreenQueue.handle();
                }

            } else if (gui.currentBackground == GUI.Screen.CREATELOBBY) {
                gui.window.draw(createGameBackground, 0, 0);
                while (createGameQueue.hasNext()) {
                    createGameQueue.handle();
                }

                // display current Mode
                if (modeSel.equals(Mode.ELFENLAND)) {
                    gui.window.draw(modeElfenlandText, 285, 180);
                    // display current rounds option
                    if (numRoundsSel == 3) {
                        gui.window.draw(rounds3Text, 384, 388);
                    } else if (numRoundsSel == 4) {
                        gui.window.draw(rounds4Text, 384, 388);
                    }
                } else if (modeSel.equals(Mode.ELFENGOLD)) {
                    gui.window.draw(modeElfengoldText, 285, 180);
                    // display current Town Gold option
                    if (townGoldOption.equals(TownGoldOption.NO)) {
                        gui.window.draw(townGoldNoText, 388, 389);
                    } else if (townGoldOption.equals(TownGoldOption.YESDEFAULT)) {
                        gui.window.draw(townGoldYesText, 388, 389);
                    } else if (townGoldOption.equals(TownGoldOption.YESRANDOM)) {
                        gui.window.draw(townGoldYesRandText, 388, 389);
                    }
                    // display current Witch option
                    if (witchSel) {
                        gui.window.draw(witchYesText, 178, 458);
                    } else {
                        gui.window.draw(witchNoText, 178, 458);
                    }
                }

                // display current size
                if (numberPlayers == 2) {
                    gui.window.draw(size2Text, 152, 240);
                } else if (numberPlayers == 3) {
                    gui.window.draw(size3Text, 152, 240);
                } else if (numberPlayers == 4) {
                    gui.window.draw(size4Text, 152, 240);
                } else if (numberPlayers == 5) {
                    gui.window.draw(size5Text, 152, 240);
                } else if (numberPlayers == 6) {
                    gui.window.draw(size6Text, 152, 240);
                }

                // display current destination option
                if (destinationTownSel) {
                    gui.window.draw(destinationTownYesText, 389, 310);
                } else {
                    gui.window.draw(destinationTownNoText, 389, 310);
                }

                // display dropdowns
                if (modeDropdownActive) {
                    gui.window.draw(modeDropdownRectangle, 268, 215);
                    gui.window.draw(modeElfenlandText, 285, 231);
                    gui.window.draw(modeElfengoldText, 285, 273);
                } else if (sizeDropdownActive) {
                    gui.window.draw(sizeDropdownRectangle, 142, 275);
                    gui.window.draw(size2Text, 152, 300);
                    gui.window.draw(size3Text, 152, 342);
                    gui.window.draw(size4Text, 152, 384);
                    gui.window.draw(size5Text, 152, 426);
                    gui.window.draw(size6Text, 152, 468);
                } else if (destinationDropdownActive) {
                    gui.window.draw(destinationTownDropdownRectangle, 376, 347);
                    gui.window.draw(destinationTownNoText, 389, 359);
                    gui.window.draw(destinationTownYesText, 389, 400);
                } else if (roundsDropdownActive) {
                    gui.window.draw(roundsDropdownRectangle, 375, 418);
                    gui.window.draw(rounds3Text, 384, 435);
                    gui.window.draw(rounds4Text, 384, 477);
                } else if (townGoldDropdownActive) {
                    gui.window.draw(townGoldDropdownRectangle, 375, 418);
                    gui.window.draw(townGoldNoText, 388, 433);
                    gui.window.draw(townGoldYesText, 388, 475);
                    gui.window.draw(townGoldYesRandText, 388, 517);
                } else if (witchDropdownActive) {
                    gui.window.draw(witchDropdownRectangle, 168, 487);
                    gui.window.draw(witchNoText, 178, 500);
                    gui.window.draw(witchYesText, 178, 542);
                }

            } else if (gui.currentBackground == GUI.Screen.CHOOSEBOOT) {
                while (chooseBootQueue.hasNext()) {
                    chooseBootQueue.handle();
                }

            } else if (gui.currentBackground == GUI.Screen.LOBBYELFENLAND) {
                gui.window.draw(lobbyElfenlandBackground, 0, 0);
                if (currentSession.isLaunchable()) {
                    if (currentUser.getName().equals(currentSession.getCreator())) {
                        lobbyElfenlandBackground.draw(startButton, 822, 580);
                    } else {
                        ClientMain.gui.window.draw(ClientMain.waitingForLaunch, 822, 580);
                    }
                }
                while (elfenlandLobbyQueue.hasNext()) {
                    elfenlandLobbyQueue.handle();
                }

            } else if (gui.currentBackground == GUI.Screen.LOBBYELFENGOLD) {
                gui.window.draw(lobbyElfengoldBackground, 0, 0);
                if (currentSession.isLaunchable()) {
                    lobbyElfengoldBackground.draw(startButton, 822, 580);
                }
                while (elfenlandLobbyQueue.hasNext()) {
                    elfenlandLobbyQueue.handle();
                }

            } else if (gui.currentBackground == GUI.Screen.ELFENLAND || gui.currentBackground == GUI.Screen.ELFENGOLD) {
                while (elfenlandQueue.hasNext()) {
                    elfenlandQueue.handle();
                }
            }

            // Add a button in the bottom right to pause the music
            if (soundOn) {
                gui.window.draw(soundOffButton, 1000, 745);
            } else {
                gui.window.draw(soundOnButton, 1000, 745);
            }

            WINDOW.render();
            Thread.yield();
        }
    }

    /**
     * TODO: alex/dijian put a mute/unmute button
     * Play Music
     * 
     * @param soundFile sound file to play
     */
    static void playSound(String soundFile) {
        if (played == false) {
            File f = new File("./" + soundFile);
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
                loadedClip = AudioSystem.getClip();
                loadedClip.open(audioIn);
                loadedClip.start();
                played = true;
            } catch (Exception e) {
                throw new Error("Unable to play sound file");
            }
        }
    }

    /*
     * Records the duration of the clip elapsed and pauses the audio file
     */
    static void pauseSound() {
        clipPos = loadedClip.getMicrosecondPosition();
        loadedClip.stop();
    }

    /*
     * Resumes the sound from a recorded clipPos
     */
    static void resumeSound() {
        loadedClip.setMicrosecondPosition(clipPos);
        loadedClip.start();
    }

    /**
     * Displays game board elements
     * 
     * @throws MinuetoFileException
     */
    public static void displayBoardElements() throws MinuetoFileException {
        MinuetoImage currentBackground = null;
        if (gui.currentBackground == GUI.Screen.ELFENLAND) {
            currentBackground = elfenlandImage;
        } else if (gui.currentBackground == GUI.Screen.ELFENGOLD) {
            currentBackground = elfengoldImage;
        }

        System.out.println("displaying board elements");

        // REDRAW CLEAN BACKGROUND
        gui.window.draw(currentBackground, 0, 0);

        // ADD ELEMENTS

        // organize tokens in inventory
        List<TokenSprite> listOfTokens = currentPlayer.getTokensInHand();
        List<CardSprite> listOfCards = currentPlayer.getCardsInHand();
        if (listOfTokens.size() == 1) {
            MinuetoImage p1 = listOfTokens.get(0);
            gui.window.draw(p1, 642, 640);
        } else if (listOfTokens.size() == 2) {
            MinuetoImage p1 = listOfTokens.get(0);
            MinuetoImage p2 = listOfTokens.get(1);
            gui.window.draw(p1, 587, 640);
            gui.window.draw(p2, 695, 640);
        } else if (listOfTokens.size() == 3) {
            MinuetoImage p1 = listOfTokens.get(0);
            MinuetoImage p2 = listOfTokens.get(1);
            MinuetoImage p3 = listOfTokens.get(2);
            gui.window.draw(p1, 615, 636);
            gui.window.draw(p2, 709, 636);
            gui.window.draw(p3, 663, 698);
        } else if (listOfTokens.size() == 4) {
            MinuetoImage p1 = listOfTokens.get(0);
            MinuetoImage p2 = listOfTokens.get(1);
            MinuetoImage p3 = listOfTokens.get(2);
            MinuetoImage p4 = listOfTokens.get(3);
            gui.window.draw(p1, 615, 636);
            gui.window.draw(p2, 709, 636);
            gui.window.draw(p3, 615, 698);
            gui.window.draw(p4, 709, 698);

        } else if (listOfTokens.size() == 5) {
            MinuetoImage p1 = listOfTokens.get(0);
            MinuetoImage p2 = listOfTokens.get(1);
            MinuetoImage p3 = listOfTokens.get(2);
            MinuetoImage p4 = listOfTokens.get(3);
            MinuetoImage p5 = listOfTokens.get(4);
            gui.window.draw(p1, 592, 636);
            gui.window.draw(p2, 663, 636);
            gui.window.draw(p3, 734, 636);
            gui.window.draw(p4, 615, 698);
            gui.window.draw(p5, 709, 698);
        }

        // organize cards in inventory
        if (listOfCards.size() == 1) {
            MinuetoImage p1 = listOfCards.get(0).getMediumImage();
            gui.window.draw(p1, 314, 634);
        } else if (listOfCards.size() == 2) {
            MinuetoImage p1 = listOfCards.get(0).getMediumImage();
            MinuetoImage p2 = listOfCards.get(1).getMediumImage();
            gui.window.draw(p1, 258, 634);
            gui.window.draw(p2, 370, 634);
        } else if (listOfCards.size() == 3) {
            MinuetoImage p1 = listOfCards.get(0).getMediumImage();
            MinuetoImage p2 = listOfCards.get(1).getMediumImage();
            MinuetoImage p3 = listOfCards.get(2).getMediumImage();
            gui.window.draw(p1, 202, 634);
            gui.window.draw(p2, 314, 634);
            gui.window.draw(p3, 426, 634);
        } else if (listOfCards.size() == 4) {
            MinuetoImage p1 = listOfCards.get(0).getMediumImage();
            MinuetoImage p2 = listOfCards.get(1).getMediumImage();
            MinuetoImage p3 = listOfCards.get(2).getMediumImage();
            MinuetoImage p4 = listOfCards.get(3).getMediumImage();
            gui.window.draw(p1, 153, 634);
            gui.window.draw(p2, 261, 634);
            gui.window.draw(p3, 369, 634);
            gui.window.draw(p4, 477, 634);
        } else if (listOfCards.size() == 5) {
            MinuetoImage p1 = listOfCards.get(0).getMediumImage();
            MinuetoImage p2 = listOfCards.get(1).getMediumImage();
            MinuetoImage p3 = listOfCards.get(2).getMediumImage();
            MinuetoImage p4 = listOfCards.get(3).getMediumImage();
            MinuetoImage p5 = listOfCards.get(4).getMediumImage();
            gui.window.draw(p1, 150, 634);
            gui.window.draw(p2, 232, 634);
            gui.window.draw(p3, 314, 634);
            gui.window.draw(p4, 396, 634);
            gui.window.draw(p5, 478, 634);
        } else if (listOfCards.size() == 6) {
            MinuetoImage p1 = listOfCards.get(0).getSmallImage();
            MinuetoImage p2 = listOfCards.get(1).getSmallImage();
            MinuetoImage p3 = listOfCards.get(2).getSmallImage();
            MinuetoImage p4 = listOfCards.get(3).getSmallImage();
            MinuetoImage p5 = listOfCards.get(4).getSmallImage();
            MinuetoImage p6 = listOfCards.get(5).getSmallImage();
            gui.window.draw(p1, 235, 605);
            gui.window.draw(p2, 348, 605);
            gui.window.draw(p3, 461, 605);
            gui.window.draw(p4, 235, 685);
            gui.window.draw(p5, 348, 685);
            gui.window.draw(p6, 461, 685);
        } else if (listOfCards.size() == 7) {
            MinuetoImage p1 = listOfCards.get(0).getSmallImage();
            MinuetoImage p2 = listOfCards.get(1).getSmallImage();
            MinuetoImage p3 = listOfCards.get(2).getSmallImage();
            MinuetoImage p4 = listOfCards.get(3).getSmallImage();
            MinuetoImage p5 = listOfCards.get(4).getSmallImage();
            MinuetoImage p6 = listOfCards.get(5).getSmallImage();
            MinuetoImage p7 = listOfCards.get(6).getSmallImage();
            gui.window.draw(p1, 235, 605);
            gui.window.draw(p2, 318, 605);
            gui.window.draw(p3, 414, 605);
            gui.window.draw(p4, 235, 685);
            gui.window.draw(p5, 318, 685);
            gui.window.draw(p6, 414, 685);
            gui.window.draw(p7, 510, 646);
        } else if (listOfCards.size() == 8) {
            MinuetoImage p1 = listOfCards.get(0).getSmallImage();
            MinuetoImage p2 = listOfCards.get(1).getSmallImage();
            MinuetoImage p3 = listOfCards.get(2).getSmallImage();
            MinuetoImage p4 = listOfCards.get(3).getSmallImage();
            MinuetoImage p5 = listOfCards.get(4).getSmallImage();
            MinuetoImage p6 = listOfCards.get(5).getSmallImage();
            MinuetoImage p7 = listOfCards.get(6).getSmallImage();
            MinuetoImage p8 = listOfCards.get(7).getSmallImage();
            gui.window.draw(p1, 222, 605);
            gui.window.draw(p2, 318, 605);
            gui.window.draw(p3, 414, 605);
            gui.window.draw(p4, 510, 605);
            gui.window.draw(p5, 222, 685);
            gui.window.draw(p6, 318, 685);
            gui.window.draw(p7, 414, 685);
            gui.window.draw(p8, 510, 685);
        }

        // whose turn it is
        if (currentPlayer.isTurn()) {
            MinuetoText itsYourTurnText = new MinuetoText("It's your turn", fontArial22Bold, MinuetoColor.BLACK);
            gui.window.draw(itsYourTurnText, 836, 504);
        } else {
            for (ClientPlayer p : currentGame.getPlayers()) {
                if (p.isTurn) {
                    MinuetoText otherPlayerTurnText = new MinuetoText("It is " + p.getName() + "'s turn",
                            fontArial22Bold, MinuetoColor.BLACK);
                    gui.window.draw(otherPlayerTurnText, 836, 504);
                }
            }
        }

        // round card
        int roundNumber = currentGame.getCurrentRound();
        if (roundNumber == 1) {
            roundNumberImage = new MinuetoImageFile("images/elfenroads-sprites/R1small.png");
        } else if (roundNumber == 2) {
            roundNumberImage = new MinuetoImageFile("images/elfenroads-sprites/R2small.png");
        } else if (roundNumber == 3) {
            roundNumberImage = new MinuetoImageFile("images/elfenroads-sprites/R3small.png");
        } else if (roundNumber == 4) {
            roundNumberImage = new MinuetoImageFile("images/elfenroads-sprites/R4small.png");
        }
        gui.window.draw(roundNumberImage, 719, 40);

        // indication on all of the routes
        MinuetoCircle turnIndicator = new MinuetoCircle(10, MinuetoColor.BLUE, true);
        if (currentPlayer.isTurn) {
            // turn indicators (can travel here)
            for (ClientRoute r : currentPlayer.getCurrentLocation().getRoutes()) {
                gui.window.draw(turnIndicator, r.getMinX(), r.getMinY());
            }
        }

        // draw the tokens onto the route
        for (ClientRoute r : Game.getAllRoutes()) {
            if (r.getTokenOnRoute().isEmpty() == false) {
                for (TokenSprite tok : r.getTokenOnRoute()) {
                    gui.window.draw(tok.getRouteImage(), r.getMinX(), r.getMinY());
                }
            }
        }

        // boots
        for (int i = 0; i < players.size(); i++) {
            ClientPlayer player = players.get(i);
            player.drawBoot(i);
        }
        currentPlayer.drawBoot(players.size());

        // gold value if elfengold
        if (currentGame.getMode() == Mode.ELFENGOLD) {
            MinuetoCircle goldValueCircle = new MinuetoCircle(20, MinuetoColor.YELLOW, true);
            gui.window.draw(goldValueCircle, 792, 522);
            MinuetoText goldAmnt = new MinuetoText(String.valueOf(currentPlayer.getGoldAmount()), fontArial20,
                    MinuetoColor.BLACK);
            gui.window.draw(goldAmnt, 806, 530);
        }
    }

    /**
     * Displays the winner's name and their boot color
     * 
     * @param winnerName winner's username
     * @throws MinuetoFileException if an image file is not found
     */
    public static void displayWinnerByString(String winnerName) throws MinuetoFileException {
        MinuetoFont font = new MinuetoFont("Arial", 22, true, false);
        MinuetoText winnerText = new MinuetoText(winnerName, font, MinuetoColor.WHITE);
        User userWinner = User.getUserByName(winnerName);
        Color colorWinner = userWinner.getColor();
        MinuetoImage bootWinner = null;
        if (colorWinner.equals(Color.BLACK)) {
            bootWinner = new MinuetoImageFile("images/choose-boot-black.png/");
        } else if (colorWinner.equals(Color.BLUE)) {
            bootWinner = new MinuetoImageFile("images/choose-boot-blue.png/");
        } else if (colorWinner.equals(Color.GREEN)) {
            bootWinner = new MinuetoImageFile("images/choose-boot-green.png/");
        } else if (colorWinner.equals(Color.YELLOW)) {
            bootWinner = new MinuetoImageFile("images/choose-boot-yellow.png/");
        } else if (colorWinner.equals(Color.RED)) {
            bootWinner = new MinuetoImageFile("images/choose-boot-red.png/");
        } else if (colorWinner.equals(Color.PURPLE)) {
            bootWinner = new MinuetoImageFile("images/choose-boot-purple.png/");
        }

        if (winnerName.equals(currentUser.getName())) {
            // winner
            gui.window.draw(winnerScreen, 0, 0);
        } else {
            // loser
            gui.window.draw(loserScreen, 0, 0);
        }
        gui.window.draw(bootWinner, 0, 0);
        gui.window.draw(winnerText, 0, 0);

        gui.window.render();

    }

    /**
     * Display boot colors
     * 
     * @param colors available colors to display
     * @throws MinuetoFileException when an image file is not found
     */
    public static void displayColors(ArrayList<String> colors) throws MinuetoFileException {

        gui.currentBackground = GUI.Screen.CHOOSEBOOT;
        gui.window.draw(chooseBootBackground, 0, 0);

        int counter = 0; // how many colors are displayed so far

        System.out.println(colors.toString());

        for (String colorString : colors) {
            // get the image + enum
            MinuetoImage boot = new MinuetoImageFile("images/choose-boot-" + colorString.toLowerCase() + ".png");
            Color c = null;
            for (Color color : Color.values()) {
                if (color.toString().equals(colorString)) {
                    c = color;
                    break;
                }
            }
            if (c == null) {
                throw new IllegalArgumentException(colorString + " does not match any colors");
            }

            // display the boot
            gui.window.draw(boot, 75 + counter * 150, 300);

            // keep track of the button location
            Integer maxX = 150 + (counter * 150);
            Integer minX = 75 + (counter * 150);
            Integer maxY = 410;
            Integer minY = 300;
            ImmutableList<Integer> listOfCoordinates = ImmutableList.of(maxX, minX, maxY, minY);
            AbstractMap.SimpleEntry<ImmutableList<Integer>, Color> entry = new AbstractMap.SimpleEntry<>(
                    listOfCoordinates, c);
            colorButtonCoordinates.add(entry);

            counter++;
        }
    }

    /**
     * Display an error message saying that the game name is already taken (on game
     * creation screen)
     */
    public static void displayNameTaken() {
        // reset name to empty
        nameString = "";
        // cover the last entry
        createGameBackground.draw(nameTextField, 168, 101);
        // display error message
        MinuetoText nameIsTaken = new MinuetoText("Name already taken.", fontArial22Bold, MinuetoColor.RED);
        createGameBackground.draw(nameIsTaken, 178, 120);
    }

    public static void displayOriginalBoard() {
        // display background depending on the mode
        Mode currentMode = currentGame.getMode();
        MinuetoImage currentBackground = null;
        if (currentMode.equals(Mode.ELFENLAND)) {
            gui.currentBackground = GUI.Screen.ELFENLAND;
            currentBackground = elfenlandImage;
        } else if (currentMode.equals(Mode.ELFENGOLD)) {
            gui.currentBackground = GUI.Screen.ELFENGOLD;
            currentBackground = elfengoldImage;
        }

        System.out.println("displaying original game board");

        // draw opponent inventory boxes
        int numberPlayers = players.size();
        for (int i = 0; i < numberPlayers; i++) {
            ClientPlayer opponent = players.get(i);
            int xName = 835;
            int yName = 70 + (i * 92);
            MinuetoRectangle playerBackground = new MinuetoRectangle(190, 85, MinuetoColor.WHITE, true);
            currentBackground.draw(playerBackground, xName - 10, yName - 10);
            MinuetoText pName = new MinuetoText(opponent.getName(), ClientMain.fontArial22Bold, MinuetoColor.BLACK);
            currentBackground.draw(pName, xName, yName);
            MinuetoText seeInv = new MinuetoText("See Inventory", ClientMain.fontArial20, MinuetoColor.BLACK);
            currentBackground.draw(seeInv, xName + 25, yName + 35);
            MinuetoImage bopp = opponent.getBoppel();
            currentBackground.draw(bopp, xName, yName + 35);
        }

        // display on the windows
        gui.window.draw(currentBackground, 0, 0);
    }

    /**
     * Display a game lobby's information (settings + name)
     */
    public static void displayLobbyInfo() {
        MinuetoFont font = new MinuetoFont("Arial", 22, true, false);
        String name = currentSession.getDisplayName();
        MinuetoText nameText = new MinuetoText(name, font, MinuetoColor.BLACK);
        Game game = currentSession.getGame();
        Mode currentMode = game.getMode();
        String size = String.valueOf(game.getNumberOfPlayers());
        MinuetoText sizeText = new MinuetoText(size, font, MinuetoColor.BLACK);
        boolean destinationEnabled = game.isDestinationTownEnabled();
        MinuetoText destText = null;
        if (destinationEnabled) {
            destText = new MinuetoText("Yes", font, MinuetoColor.BLACK);
        } else {
            destText = new MinuetoText("No", font, MinuetoColor.BLACK);
        }
        int numberRounds = game.getNumberOfRounds();
        MinuetoText numRoundsText = new MinuetoText(String.valueOf(numberRounds), font, MinuetoColor.BLACK);
        MinuetoImage background = null;
        MinuetoText modeText = null;
        if (currentMode.equals(Mode.ELFENLAND)) {
            background = lobbyElfenlandBackground;
            modeText = new MinuetoText("Elfenland", font, MinuetoColor.BLACK);

        } else if (currentMode.equals(Mode.ELFENGOLD)) {
            background = lobbyElfengoldBackground;
            modeText = new MinuetoText("Elfengold", font, MinuetoColor.BLACK);
            boolean witchEnabled = game.isWitchEnabled();
            MinuetoText witchText = null;
            if (witchEnabled) {
                witchText = new MinuetoText("Yes", font, MinuetoColor.BLACK);
            } else {
                witchText = new MinuetoText("No", font, MinuetoColor.BLACK);
            }
            background.draw(witchText, 787, 455);
            TownGoldOption townGoldOption = game.getTownGoldOption();
            MinuetoText townText = null;
            if (townGoldOption.equals(TownGoldOption.NO)) {
                townText = new MinuetoText("No", font, MinuetoColor.BLACK);
            } else if (townGoldOption.equals(TownGoldOption.YESDEFAULT)) {
                townText = new MinuetoText("Yes(default)", font, MinuetoColor.BLACK);
            } else {
                townText = new MinuetoText("Yes(random)", font, MinuetoColor.BLACK);
            }
            background.draw(townText, 856, 508);
        }

        background.draw(nameText, 480, 125);
        background.draw(modeText, 770, 185);
        background.draw(numRoundsText, 808, 385);
        background.draw(sizeText, 765, 255);
        background.draw(destText, 937, 321);

    }

    /**
     * Displays all users registered in a game session
     * 
     * @throws MinuetoFileException
     */
    public static void displayUsers() throws MinuetoFileException {
        MinuetoFont font = new MinuetoFont("Arial", 22, true, false);
        ArrayList<User> users = currentSession.getUsers();

        MinuetoImage background = null;
        Game game = currentSession.getGame();
        Mode currentMode = game.getMode();
        if (currentMode.equals(Mode.ELFENLAND)) {
            background = lobbyElfenlandBackground;
        } else if (currentMode.equals(Mode.ELFENGOLD)) {
            background = lobbyElfengoldBackground;
        }

        int counter = 0; // how many users are displayed so far

        for (User u : users) {
            String name = u.getName();
            if (name.equals(currentUser.getName())) {
                name = name + " (you)";
            }
            // truncate names that are too long
            if (name.length() > 15) {
                if (name.equals(currentUser.getName())) {
                    name = name.substring(0, 15) + "..." + " (you)";
                } else {
                    name = name.substring(0, 15) + "...";
                }

            }

            Color color = u.getColor();
            boolean ready = u.isReady();

            MinuetoText uName = new MinuetoText(name, font, MinuetoColor.BLACK);

            MinuetoImage uColor = null;
            MinuetoText uColorText = null;
            if (color == null) {
                uColorText = new MinuetoText("?", font, MinuetoColor.BLACK);
            } else {
                if (color.equals(Color.BLACK)) {
                    uColor = blackBoppel;
                } else if (color.equals(Color.RED)) {
                    uColor = redBoppel;
                } else if (color.equals(Color.BLUE)) {
                    uColor = blueBoppel;
                } else if (color.equals(Color.GREEN)) {
                    uColor = greenBoppel;
                } else if (color.equals(Color.YELLOW)) {
                    uColor = yellowBoppel;
                } else if (color.equals(Color.PURPLE)) {
                    uColor = purpleBoppel;
                }
            }

            /*
             * MinuetoText uReady;
             * if (ready) {
             * uReady = new MinuetoText("Ready", font, MinuetoColor.GREEN);
             * } else {
             * uReady = new MinuetoText("Not ready", font, MinuetoColor.BLACK);
             * }
             * background.draw(uReady, 475, 240 + counter * 50);
             */

            background.draw(uName, 45, 240 + counter * 50);
            if (uColor == null) {
                background.draw(uColorText, 290, 240 + counter * 50);
            } else {
                background.draw(uColor, 290, 240 + counter * 50);
            }

            counter++;
        }

        gui.window.draw(background, 0, 0);
    }

    /**
     * Displays all available game sessions (NOT game services)
     * If there are no sessions, then display a message saying so.
     */
    public static void displayAvailableGames() {
        // retrieve info on the server
        GetAvailableSessionsAction action = new GetAvailableSessionsAction(currentUser.getName());
        ACTION_MANAGER.sendAction(action);

        // reset buttons
        joinButtonCoordinates.clear();

        // display
        gui.currentBackground = GUI.Screen.LOBBY;
        gui.window.draw(lobbyBackground, 0, 0);
        MinuetoFont font = new MinuetoFont("Arial", 22, true, false);
        try {
            ArrayList<LobbyServiceGameSession> availableSessionsList = LobbyServiceGameSession.getAvailableSessions();

            // display a message when no sessions are available to join
            int nbAvailableGameSessions = availableSessionsList.size();
            if (nbAvailableGameSessions == 0) {
                MinuetoText noneAvailableText = new MinuetoText(
                        "There are no games yet. Please refresh or create a new game.", font, MinuetoColor.BLACK);
                gui.window.draw(noneAvailableText, 200, 340);
            }

            // display next button
            if (nbAvailableGameSessions > 9) {
                MinuetoImage nextButton = new MinuetoImageFile("images/next-button.png");
                gui.window.draw(nextButton, 700, 676);
            }

            int totalCounter = 0; // how many games are displayed so far
            int pageCounter = 0; // how many games are displayed on one page so far

            // display available game sessions (i.e. games with a creator)
            for (LobbyServiceGameSession gs : availableSessionsList) {
                if (!gs.isLaunched()) { // only show unlaunched sessions
                    String gsName = gs.getDisplayName();
                    String gsCreator = gs.getCreator();
                    String gsCurrentPlayerNumber = String.valueOf(gs.getNumberOfUsersCurrently());
                    String gsMaxPlayerNumber = String.valueOf(gs.getGame().getNumberOfPlayers());

                    MinuetoText displayName = new MinuetoText(gsName, font, MinuetoColor.BLACK);
                    MinuetoText creator = new MinuetoText(gsCreator, font, MinuetoColor.BLACK);
                    MinuetoText size = new MinuetoText(gsCurrentPlayerNumber + "/" + gsMaxPlayerNumber, font,
                            MinuetoColor.BLACK);
                    MinuetoRectangle joinButton = new MinuetoRectangle(100, 35, MinuetoColor.WHITE, true);
                    MinuetoText joinText = new MinuetoText("JOIN", font, MinuetoColor.BLACK);

                    gui.window.draw(displayName, 65, 215 + (pageCounter * 50));
                    gui.window.draw(creator, 350, 215 + (pageCounter * 50));
                    gui.window.draw(size, 655, 215 + (pageCounter * 50));
                    gui.window.draw(joinButton, 835, 210 + (pageCounter * 50));
                    gui.window.draw(joinText, 855, 215 + (pageCounter * 50));

                    // keep track of the button location
                    Integer maxX = 935;
                    Integer minX = 835;
                    Integer maxY = 245 + (pageCounter * 50);
                    Integer minY = 210 + (pageCounter * 50);
                    ImmutableList<Integer> listOfCoordinates = ImmutableList.of(maxX, minX, maxY, minY);
                    AbstractMap.SimpleEntry<ImmutableList<Integer>, LobbyServiceGameSession> entry = new AbstractMap.SimpleEntry<>(
                            listOfCoordinates, gs);
                    joinButtonCoordinates.add(entry);

                    pageCounter++;
                    totalCounter++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displaySavedGames() {
        // TODO: retrieve info on the server

        // TODO: reset buttons

        // display
        gui.window.draw(saveGameBackground, 0, 0);
        gui.currentBackground = GUI.Screen.SAVEDGAMES;

        MinuetoFont font = new MinuetoFont("Arial", 22, true, false);

        // TODO: display all saved games and keep track of the Join button location
    }

    public static void recievePhaseOne(HashMap<String, List<String>> cardsHashMap) throws MinuetoFileException {
        players.forEach((p) -> {
            try {
                p.addCardStringArray(cardsHashMap.get(p.getName()));
            } catch (MinuetoFileException e) {
                e.printStackTrace();
            }
        });
        currentPlayer.addCardStringArray(cardsHashMap.get(currentPlayer.getName()));
    }

    public static void receiveTokens(String playerString, List<String> tokenStrings) throws MinuetoFileException {
        ClientPlayer.getPlayerByName(playerString).addTokenStringList(tokenStrings);
    }

    public static void diaplayWinnerByString(String winner) {
        // draw on GUI
    }
}
