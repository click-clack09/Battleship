package battleship;

/**
 *
 * @author rmald09, hlind01, oalon01
 */
import javax.swing.JOptionPane;
import java.util.Random;

/**
 *
 * @author mysti
 */
public class Battleship {
    
    final static char SUNK_CHAR = 0x2620;
    final static String EMPTY_SPACE = " _ |";
    final static String MISS_SPACE = " o |";
    final static String HIT_SPACE = " x |";
    final static String SUNK_SPACE = " " + SUNK_CHAR + " |";
    final static int[] CARRIER = {5,5};
    final static int[] BATTLESHIP = {4,4};
    final static int[] DESTROYER = {3,3};
    final static int[] SUBMARINE = {3,2};
    final static int[] PT_BOAT = {2,1};
        
        

    /**Displays a series of dialog boxes to simulate the game Battleship.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        //Left as a variable to faciliate possible future user defined number of
        //turns
        int turns = 50;
        
        //Rows to be displayed as a game board
        String[] row1 = new String[10];  
        String[] row2 = new String[10];
        String[] row3 = new String[10];
        String[] row4 = new String[10];  
        String[] row5 = new String[10];
        String[] row6 = new String[10];
        String[] row7 = new String[10];  
        String[] row8 = new String[10];
        String[] row9 = new String[10];
        String[] row10 = new String[10];  
        String[] rowCoord = {" A", " B", " C", " D", " E", "  F", " G", " H",
            "   I", " J"};
                
        //Two dimensional array for ease of passing all rows as a single 
        //parameter
        String [][] allDisplayRows = new String[][]{row1, row2, row3, row4, 
            row5, row6, row7, row8, row9, row10, rowCoord};
        
        //initial display game board set up
        allDisplayRows = gameBoardInitialize(allDisplayRows);
        
        //List of sunk ships to be updated as gameplay progresses
        String shipList = "";

        //Game intro
        gameIntro(turns, allDisplayRows, shipList);
                
        //Actual game play
        gamePlay(turns, allDisplayRows, shipList);
        
        System.exit(0);        
    }
    
    /**Initializes the initial display game board to an empty grid.
     * 
     * @param allDisplayRows A two dimensional array used to display the game 
     * board
     * 
     * @return An empty display game board grid
     */
    public static String[][] gameBoardInitialize(String allDisplayRows[][])
    {
        for (int i = 0; i <10; i++)
        {
            for (int index = 0; index < 10; index++)
                allDisplayRows[i][index] = EMPTY_SPACE;
        }
        return allDisplayRows;
    }
    
    /**This method displays the introductions and player instructions.
     * 
     * @param turns The number of "shots" the player has 
     * @param allDisplayRows The display game board grid
     * @param shipList A String concatenated to the output string. Lists ships 
     * 
     */
    public static void gameIntro(int turns, String[][] allDisplayRows, 
            String shipList)
    {
        //left allign and min-width to format better?
        //let user pick difficulty? change to int return type?
        
        //Introduction and instructions
        outputScreen("BBBBBBBBB              AAAAAA           TTTTTTTTTT     TT"
                + "TTTTTTTT        LL                           EEEEEEEEE      "
                + "    SSSSSSSS         HH               HH       IIIIIIIIII   "
                + "   PPPPPPPP\nBB                BB       AAA           AAA   "
                + "           TT                       TT                  LL  "
                + "                         EE                       SS        "
                + "        SS       HH               HH          II           P"
                + "P             PP\nBB                BB       AA             "
                + "   AA              TT                       TT              "
                + "    LL                           EE                       SS"
                + "                            HH                HH          II"
                + "          PP             PP\nBBBBBBBB            AAAAAAAAAA "
                + "             TT                       TT                  LL"
                + "                           EEEEEEE                SSSSSSSS  "
                + "      HHHHHHHHHH          II          PPPPPPPP\nBB          "
                + "      BB       AA                AA              TT         "
                + "              TT                  LL                        "
                + "   EE                                              SS     HH"
                + "                HH          II          PP\nBB              "
                + "  BB       AA                AA              TT             "
                + "          TT                  LL                           E"
                + "E                        SS                SS      HH       "
                + "        HH           II          PP\nBBBBBBBBB          AA  "
                + "             AA               TT                       TT   "
                + "               LLLLLLLLLL        EEEEEEEEE          SSSSSSSS"
                + "         HH               HH       IIIIIIIIII      PP");
        
        outputScreen("Welcome to Battleship!");
        
        outputScreen("The game board will be displayed like this...");
        
        outputScreen(gameBoardPrint(turns, allDisplayRows, shipList));
        
        outputScreen("The enemy has the following ships:\n\nAircraft Carrier - "
                + "5 spaces\nBattleship - 4 spaces\nDestroyer - 3 spaces\nSubma"
                + "rine - 3 spaces\nPatrol Boat - 2 spaces");
        
        outputScreen("You will have " + turns + " turns to sink your opponent's "
                + "ships.");
        
        outputScreen("Hits will be displayed with an 'x'.\nMisses will be "
                + "displayed with an 'o'.");
        
        outputScreen("Good Luck!");
    }
    
    /**This method handles the actual game play logic flow, calling the
     * fireShotX and fireShotY methods, applying a switch to call the 
     * updateBoard method, and calling the gameBoardPrint method.
     * 
     * @param turns The number of game play turns
     * @param allDisplayRows The array of all the row arrays
     * @param shipList A String concatenated to the output string. Lists ships 
     * that have been sunk, as they are sunk.
     */
    public static void gamePlay (int turns, String[][] allDisplayRows, String 
            shipList)
    {        
        int[][][] allShipRows; 
        
        //Initializes enemy ship positions, status and ID array. 
        allShipRows = new int[10][10][2];
        
        /*enemyShipBoard places enemy ships, one at a time using the shipPlacer
            method. Each ship is assigned a unique ID value, from 1-5, that 
        represents that specific ship on Z-coordinate 0 and Z-coordinate 1 of 
        the 3D array. These values are changed to 6, meaning a hit, at 
        Z-coordinate 0, but do not change on Z-coordinate 1. This allows a later
        method to change the display screen from an 'x' to a skull & crossbones 
        when the ship is sunk, based on the 6 status at Z-coordinate 0 and the 
        unique ship ID value at Z-coordinate 1, preventing ships that have been 
        hit, but not sunk, from being changed.*/
        allShipRows = enemyShipBoard(allShipRows);
        
        //Used to track the number of hits for each ship
        int[] shipHits = new int[5];

        //Runs the playerTurn method for the set number of turns        
        for (int i = 0; i < turns; i++)
        {
            allShipRows = playerTurn(allDisplayRows, allShipRows, shipHits);
            shipList += updateShipList(shipHits, allShipRows);
            
            outputScreen(gameBoardPrint(turns - i - 1, 
                    sunkShipGridUpdate (allShipRows, allDisplayRows), shipList));
            
            /*Ends the game if all ships are sunk, displays differentend of game
            messages depending on whether the play won or lost*/
            if (shipList.length() > 59)
            {
                outputScreen("YOU WON!!!");
                i = turns;
            }    
            if(i == turns - 1 )
                outputScreen("You lost, try again!");
        }
    }
    
    /**Handles collecting the x and y coordinates from the player, calls the 
     * updateBoard method to account for hits and misses.
     * 
     * @param allDisplayRows The two dimensional array for the game board 
     * display
     * @param allShipRows The three dimensional array containing the placement 
     * of the enemy ships, status of the ship and identity of the ship
     * @param shipHits The array which tracks how many hits a ship has sustained
     * 
     * @return Updated enemy ship 3D array
     */
    public static int[][][] playerTurn(String[][] allDisplayRows,
            int[][][] allShipRows, int[] shipHits)
    {
        //Gets y and x coordinates from the player
        char yCoord = fireShotY();
        int xCoord = fireShotX();
        
        //determines if it was a hit or a miss
        int hitType = hit(xCoord, yCoord, allShipRows, shipHits);
        
        //changes updates display board and hit status on 3D ship array
            switch (yCoord)
            {
                case 'a':
                case 'A':
                    updateBoard (allDisplayRows[0], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[0][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'b':
                case 'B':
                    updateBoard (allDisplayRows[1], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[1][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'c':
                case 'C':
                    updateBoard (allDisplayRows[2], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[2][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'd':
                case 'D':
                    updateBoard (allDisplayRows[3], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[3][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'e':
                case 'E':
                    updateBoard (allDisplayRows[4], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[4][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'f':
                case 'F':
                    updateBoard (allDisplayRows[5], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[5][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'g':
                case 'G':
                    updateBoard (allDisplayRows[6], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[6][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'h':
                case 'H':
                    updateBoard (allDisplayRows[7], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[7][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'i':
                case 'I':
                    updateBoard (allDisplayRows[8], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[8][xCoord-1][0] = 6;
                    }
                    break;
                
                case 'j':
                case 'J':
                    updateBoard (allDisplayRows[9], hitType, xCoord);
                    if (hitType == 1)
                    {
                        allShipRows[9][xCoord-1][0] = 6;
                    }
                    break;
            
                default:
                    break;
            }
            return allShipRows;
    }
    
    /**Will print out a dialog box containing the message String parameter.
     * 
     * @param message The String to be output by the outputScreen method
     */
    public static void outputScreen (String message)
    {
        JOptionPane.showMessageDialog(null,message);
    }
    

    /*fireShotX and fireShotY left as separate methods to letter number 
    coordinates allow true to the original game*/
    
    /**Prints message dialog box and collects the x coordinate from the user.
     * 
     * @return the X coordinate
     */
    public static int fireShotX()
    {
        String input;
        int xCoord;
        boolean validInput = false;
        char inputChecker;
        
        do/*input validation, checks for none number strings and out of bounds 
            input*/
        {
            do//input validation, checks for null values
            {
                input = JOptionPane.showInputDialog("Enter x coordinate (1 "
                        + "through 10): ");
            }while(input.equals(""));
            
            inputChecker = input.charAt(0);
            
            if (input.length() == 1)
            {
                switch (inputChecker)
                {
                case '1':
                    validInput = true;
                    break;
                case '2':
                    validInput = true;
                    break;
                case '3':
                    validInput = true;
                    break;
                case '4':
                    validInput = true;
                    break;
                case '5':
                    validInput = true;
                    break;
                case '6':
                    validInput = true;
                    break;
                case '7':
                    validInput = true;
                    break;
                case '8':
                    validInput = true;
                    break;
                case '9':
                    validInput = true;
                    break;
                default:
                    break;
                }
            }
            if (input.length() == 2)
            {
                if(inputChecker == '1' && input.charAt(1) == '0')
                    validInput = true;
            }
        }while (!validInput);
        xCoord = Integer.parseInt(input);
        
        return xCoord;
    }
    
    /**Prints message dialog box and collects the y coordinate from the user.
     * 
     * @return The Y coordinate
     */
    public static char fireShotY()
    {
        String input;
        char yCoord;
        boolean validInput = false;
        
        do/*input validation, checks for strings longer than 1 character and 
            out of bounds input*/
        {
            do//input validation, checks for null values
            {
                input = JOptionPane.showInputDialog("Enter y co-ordinate (\'A\'"
                        + " through \'J\'): ");
            }while(input.equals(""));
            
            yCoord = input.charAt(0);
            
            if (input.length() == 1)
            {
                switch (yCoord)
                {
                    case 'a':
                    case 'A':
                        validInput = true;
                        break;
                    case 'b':
                    case 'B':
                        validInput = true;
                        break;
                    case 'c':
                    case 'C':
                        validInput = true;
                        break;
                    case 'd':
                    case 'D':
                        validInput = true;
                        break;
                    case 'e':
                    case 'E':
                        validInput = true;
                        break;
                    case 'f':
                    case 'F':
                        validInput = true;
                        break;
                    case 'g':
                    case 'G':
                        validInput = true;
                        break;
                    case 'h':
                    case 'H':
                        validInput = true;
                        break;
                    case 'i':
                    case 'I':
                        validInput = true;
                        break;
                    case 'j':
                    case 'J':               
                        validInput = true;
                        break;
                    default:
                        break;
                }
            }
        }while(!validInput);
        
        return yCoord;
    }
    
    /**Takes the gameGrid, created by the two dimensional array allDisplayRows
     * and concatenates it with a turn counter, a stylized top boarder element, 
     * x and y coordinate identifiers and an updated listing of sunk ships to 
     * create the completed game board.
     * 
     * @param turns The number of game play turns
     * @param allDisplayRows The two dimensional array of containing hits, 
     * misses and empty spaces
     * @param shipList A String concatenated to the output string. Lists ships 
     * that have been sunk, as they are sunk.
     * 
     * @return A String of the completed game board
     */
    public static String gameBoardPrint(int turns, String[][] allDisplayRows,
            String shipList)
    {
        String name = "                 Battleship\n\n";
        String board;
        String topCoord;
        String edgeBoard = "   ";
        String gameGrid = "";
        
        /*Prints the interior game grid of the board*/
        for (int i = 0; i<10; ++i)
        {
            gameGrid = gameGrid + "\n  "+ allDisplayRows[10][i] +"|";
      
            for (int index = 0; index<10; ++index)
                gameGrid = gameGrid + allDisplayRows[i][index];
        }
        
        topCoord = "     You have " + turns + " turns remaining.\n\n      1   2"
                + "   3   4   5   6   7   8   9  10\n";
            
        //Prints top boarder edge
        for (int i = 0; i < 25; i++)
        {
            edgeBoard = edgeBoard + "=";
        }
        
        board = name + topCoord + edgeBoard + gameGrid + 
                sunkShipsList(shipList);        
        
        return board;
    }   
    
    /**Will determine if there is a ship at the user supplied coordinates, if so
     * calls hitAccumulator to track hits per ship.
     * 
     * @param xCoord The x coordinate entered by the user
     * @param yCoord The y coordinate entered by the user
     * @param allShipRows The 3D array used to track enemy ship placement, 
     * status and ID
     * @param shipHits Array used to track hits per ship
     * 
     * @return 0 if a miss, 1 if a hit, 2 if a duplicate hit, 3 if hit on a 
     * sunk ship.
     */
    public static int hit (int xCoord, char yCoord, int[][][] allShipRows,
            int[] shipHits)
    {
        int hit = 0;
        
        switch (yCoord)
        {
            case 'a':
            case 'A':
                /*Each ship is assigned a unique value between 1 and 5 used to
                track it's placement in the 3D array using using X and Y 
                coordinates at Z-coordinate 0. If there is a value between 1 and
                5 at the x and y coordinates on Z-coordinate 0, a ship has been 
                hit.*/
                if (allShipRows[0][xCoord-1][0] > 0 && 
                        allShipRows[0][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[0][xCoord-1][0]);
                }
                
                /*Once a ship has been hit, the int at the x and y coordinates, 
                Z-coordinate 0, is changed to 6 to indicate hit status.*/
                else if (allShipRows[0][xCoord-1][0] == 6)
                    hit = 2;
                
                /*Once a ship has been hit, the int at the x and y coordinates, 
                Z-coordinate 0, is changed to 7 to indicate sunk status.*/
                else if (allShipRows[0][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
            
            case 'b':
            case 'B':
                if (allShipRows[1][xCoord-1][0] > 0 && 
                        allShipRows[1][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[1][xCoord-1][0]);
                }
                
                else if (allShipRows[1][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[1][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
            
            case 'c':
            case 'C':
                if (allShipRows[2][xCoord-1][0] > 0 && 
                        allShipRows[2][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[2][xCoord-1][0]);
                }
                
                else if (allShipRows[2][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[2][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
            
            case 'd':
            case 'D':
                if (allShipRows[3][xCoord-1][0] > 0 && 
                        allShipRows[3][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[3][xCoord-1][0]);
                }
                
                else if (allShipRows[3][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[3][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
        
            case 'e':
            case 'E':
                if (allShipRows[4][xCoord-1][0] > 0 && 
                        allShipRows[4][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[4][xCoord-1][0]);
                }
                
                else if (allShipRows[4][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[4][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}    
                
                break;
            
            case 'f':
            case 'F':
                if (allShipRows[5][xCoord-1][0] > 0 && 
                        allShipRows[5][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[5][xCoord-1][0]);
                }
                
                else if (allShipRows[5][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[5][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}    
                
                break;
            
            case 'g':
            case 'G':
                if (allShipRows[6][xCoord-1][0] > 0 && 
                        allShipRows[6][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[6][xCoord-1][0]);
                }
                else if (allShipRows[6][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[6][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}    
                
                break;
            
            case 'h':
            case 'H':
                if (allShipRows[7][xCoord-1][0] > 0 && 
                        allShipRows[7][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[7][xCoord-1][0]);
                }
                
                else if (allShipRows[7][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[7][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
            
            case 'i':
            case 'I':
                if (allShipRows[8][xCoord-1][0] > 0 && 
                        allShipRows[8][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[8][xCoord-1][0]);
                }
                else if (allShipRows[8][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[8][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
                
            case 'j':
            case 'J':
                if (allShipRows[9][xCoord-1][0] > 0 && 
                        allShipRows[9][xCoord-1][0] < 6)
                {
                    hit = 1;
                    hitAccumulator(shipHits, allShipRows[9][xCoord-1][0]);
                }
                
                else if (allShipRows[9][xCoord-1][0] == 6)
                    hit = 2;
                
                else if (allShipRows[9][xCoord-1][0] == 7)
                    hit = 3;
                
                else
                {}
                
                break;
            
            default:
                break;
        }
        
        return hit;
    }
    /**Will update the display array for the appropriate row to indicate a hit 
     * or a miss
     * 
     * @param updateRow The array to be updated with hit/miss string
     * @param hit Whether the shot was a hit or a miss
     * @param xCoord The X-Coordinate for this specific shot
     * 
     * @return The updated array to be printed
     */
    public static String[] updateBoard (String [] updateRow, int hit, int 
            xCoord)
    {
        switch (hit)
        {
            case 0:
                updateRow[(xCoord - 1)] = MISS_SPACE;
                outputScreen("Miss!");
                break;
        
            case 1:
                updateRow[(xCoord - 1)] = HIT_SPACE;
                outputScreen("Hit!");
                break;
        
            case 2:
                outputScreen("You hit the same space!");
                break;
            
            case 3:
                outputScreen("You already sunk that ship!");
                break;
            
            default:
                break;
        }
        
        return updateRow;
    }
    
    /**Creates the enemy ship board using the shipPlacer method and the array
     * constants (consisting of the ship size [0] and the unique ship designator
     * [1], for each ship.
     * 
     * @param allShipRows The 3D array which will contain the ship placement and
     * status on the X- and Y-coordinates at Z-coordinate 0 and ship ID at 
     * Z-coordinate 1.
     * @return The final 3D ship array.
     */
    public static int[][][] enemyShipBoard (int[][][] allShipRows)
    {         
        return shipPlacer(PT_BOAT, shipPlacer(SUBMARINE, shipPlacer(DESTROYER, 
                shipPlacer(BATTLESHIP, shipPlacer(CARRIER, allShipRows)))));       
    }
    
    /**Randomly places the ships on the X- and Y-coordinates of the 3D array, 
     * guarding against placing a ship off of the board and placing a ship on 
     * another ship.
     * 
     * @param shipSize Predefined ship sizes used to place the ships.
     * @param allShipRows The 3D array containing ship placement, status and ID
     * data.
     * 
     * @return The updated array, having successfully placed the new ship.
     */
    public static int[][][] shipPlacer(int shipSize[], int[][][] allShipRows)
    {       
        Random random = new Random();
        
        int startX = 0;
        int startY = 0;       
        int orientation = random.nextInt(2);
        
        
        if (orientation == 0) //horizontal
        {
            int sum;
            do
            {
                sum = 0;
                
                //Guards against going off board
                startX = random.nextInt(10-shipSize[0]);
                startY = random.nextInt(10-shipSize[0]);
                
                /*Creates a checksum for the X- and Y- coordinates the method is
                attempting to place the ship at*/
                for (int i = 0; i < shipSize[0]; i++)
                {
                    sum += allShipRows[startY][startX +i][0];
                }
            /*Iterates while the checksum is not 0, preventing ship overwrite*/
            }while (sum != 0);
            
            //Writes ship to Z-coordinate plane 0 and 1 
            for (int i = 0; i < shipSize[0]; i++)
            {
                allShipRows[startY][startX + i][0] = shipSize[1];
                allShipRows[startY][startX + i][1] = shipSize[1];
            }
        }
    
        else //vertical
        {
            int sum;
            do
            {
                sum = 0;
                
                //Guards against going off board
                startX = random.nextInt(10-shipSize[0]);
                startY = random.nextInt(10-shipSize[0]);
    
                for (int i = 0; i < shipSize[0]; i++)
                    sum += allShipRows[startY+i][startX][0];
                    
            }while (sum != 0);
            
            
            
//            for (int i = 0; i < shipSize; i++)
//            {   
//                do
//                    {
//                        //Guards against going off board
//                        startX = random.nextInt(10-shipSize);
//                        startY = random.nextInt(10-shipSize);
//                    }while (allShipRows[startY+i][startX] != 0);
//            }    

            for (int i = 0; i < shipSize[0]; i++)
            {
                allShipRows[startY+i][startX][0] = shipSize[1];
                allShipRows[startY+i][startX][1] = shipSize[1];
            }
        }
        return allShipRows;
    }
    
    /**Creates a String of sunk ships, adding ships as they are sunk.
     * 
     * @param shipList The current list of sunk ships and a header to be 
     * displayed.
     * 
     * @return The updated shipsSunk String. 
     */
    public static String sunkShipsList(String shipList)
    {
        String shipsSunk = "\n\n               Ships Sunk:\n";
        
        shipsSunk = shipsSunk + shipList;
        
        return shipsSunk;
    }
    
    /**Accumulates and tracks hits for each ship
     * 
     * @param shipHits The array containing the current number of times a ship 
     * has been hit
     * @param ship An integer representing a specific ship ID
     * @return 
     */
    public static int[] hitAccumulator(int[]shipHits, int ship)
    {
        switch (ship)
        {
            case 5:
                ++shipHits[0];
                break;
            case 4:
                ++shipHits[1];
                break;
            case 3:
                ++shipHits[2];
                break;
            case 2:
                ++shipHits[3];
                break;
            case 1:
                ++shipHits[4];
                break;
            default:
                break;
        }        
        
        return shipHits;
    }
    /**Using the 3D enemy ship array and the array containing the number hits 
     * per ship, changes the ship status to 7 to indicate that it is sunk, 
     * displays an dialog message box informing the player of which ship was 
     * sunk and returning a String with the name of the sunk ship.
     * 
     * @param enemyShipHits An array containing the current number of times a
     * ship has been hit
     * @param allShipRows The 3D array containing ship placement, status and ID
     * 
     * @return A String containing the name of the ship when sunk or a null
     * String if nothing was sunk when the method was called.
     */
    public static String updateShipList(int[]enemyShipHits, 
            int[][][] allShipRows)
    {
        //Default return value
        String newShip = "";
        
        /*Initial if-statement checks for enough hits to have sunk a particular
        ship.*/
        if (enemyShipHits[0] == 5)
        {
            //Returns String of ship type
            newShip ="Aircraft Carrier\n";
            
            /*Resets the number of hits to prevent the message from printing 
            more than once*/ 
            enemyShipHits[0] = 0;
            
            /*Scans entire Z-coordinate 0 place looking for hit ships that have
            an ID of 5 (carrier ID). When one is found, the status is changed to
            7, indicating the ship is sunk.*/
            for (int i = 0; i < 10; ++i)
            {
                for (int index = 0; index < 10; ++  index)
                {
                    if (allShipRows[i][index][0] == 6 && 
                            allShipRows[i][index][1] == 5)
                        allShipRows[i][index][0] = 7;
                }
            }
            outputScreen("You sank the enemy's Aircraft Carrier!");
        }
        if (enemyShipHits[1] == 4)
        {
            newShip = "Battleship\n";
            enemyShipHits[1] = 0;
            for (int i = 0; i < 10; ++i)
            {
                for (int index = 0; index < 10; ++  index)
                {
                    if (allShipRows[i][index][0] == 6 && 
                            allShipRows[i][index][1] == 4)
                        allShipRows[i][index][0] = 7;
                }
            }
            outputScreen("You sank the enemy's Battleship!");
        }
        if (enemyShipHits[2] == 3)
        {
            newShip = "Destroyer\n";
            enemyShipHits[2] = 0;
            for (int i = 0; i < 10; ++i)
            {
                for (int index = 0; index < 10; ++  index)
                {
                    if (allShipRows[i][index][0] == 6 && 
                            allShipRows[i][index][1] == 3)
                        allShipRows[i][index][0] = 7;
                }
            }
            outputScreen("You sank the enemy's Destroyer!");
        }
        if (enemyShipHits[3] == 3)
        {
            newShip = "Submarine\n";
            enemyShipHits[3] = 0;
            for (int i = 0; i < 10; ++i)
            {
                for (int index = 0; index < 10; ++  index)
                {
                    if (allShipRows[i][index][0] == 6 && 
                            allShipRows[i][index][1] == 2)
                        allShipRows[i][index][0] = 7;
                }
            }
            outputScreen("You sank the enemy's Submarine!");
        }
        if (enemyShipHits[4] == 2)
        {
            newShip = ("Patrol Boat\n");
            enemyShipHits[4] = 0;
            for (int i = 0; i < 10; ++i)
            {
                for (int index = 0; index < 10; ++  index)
                {
                    if (allShipRows[i][index][0] == 6 && 
                            allShipRows[i][index][1] == 1)
                        allShipRows[i][index][0] = 7;
                }
            }
            outputScreen("You sank the enemy's Patrol Boat!");
        }
                
        return newShip;
    }
    
    /**Changes the display to indicate a ship is no longer hit but instead sunk
     * using a special character.
     * 
     * @param allShipRows The 3D array containing ship placement, status and ID.
     * @param allDisplayRows The 2D array used to display the game board.
     * @return 
     */
    public static String[][] sunkShipGridUpdate (int[][][] allShipRows, 
            String[][] allDisplayRows)
    {       
        for (int i = 0; i < 10; ++i)
        {
            for (int index = 0; index < 10; ++  index)
            {
                if (allShipRows[i][index][0] == 7)
                    allDisplayRows[i][index] = ("" + SUNK_CHAR + "|");
            }
        }
        
        return allDisplayRows;
    }
}