import java.awt.Point;
import java.util.*;

public class ABDirtyAI2 extends AIModule{
    private int our_player;
    public int column;
    public ArrayList<Point> player1Ats = new ArrayList<Point>();
    public ArrayList<Point> player2Ats = new ArrayList<Point>();

    @Override
    public void getNextMove(final GameStateModule state){
	int i = 1;
	//int i = 2;
	final GameStateModule game = state.copy();

	while (!terminate){
	    interactiveDeepeningAlphaBetaSearch(game, i);
	    i++;
	}
    }

    public void interactiveDeepeningAlphaBetaSearch(final GameStateModule game, int depth){
	int v;
	this.our_player = game.getActivePlayer();

	v = maxValue(game, depth, -1*Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	chosenMove = this.column;
    }
    
    public int makeMove(final GameStateModule game, int column){ 
	// store the move to a arraylist, reducing complexity of evaluation Function (also making things easier to deal with)
	int row = game.getHeightAt(column);
	if (game.getActivePlayer() == 1){
	    this.player1Ats.add(new Point(row,column));
	}else{
	    this.player2Ats.add(new Point(row,column));
	}
	System.out.print("\ncolumn: " + column + " row: " + row);
	game.makeMove(column);
	return row;
    }
    public void unmakeMove(final GameStateModule game, int column, int row){
	game.unMakeMove();
	if (game.getActivePlayer() == 1){
	    this.player1Ats.remove(new Point(row,column));
	}else{
	    this.player2Ats.remove(new Point(row,column));
	}
    }

    public int maxValue(final GameStateModule game, int depth, int alpha, int beta){
	boolean gameIsOver = game.isGameOver();
	if (depth <= 0 || gameIsOver) return evaluationFunction(game, gameIsOver); // terminal test (a function of the depth)
	System.out.print("\n\nMAX\n\n");

	int v = -1*Integer.MAX_VALUE;
	int v_aux;
	int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    {
		if(game.canMakeMove(i))
		    moves[numLegalMoves++] = i;
	    }

	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    int row = makeMove(game,moves[i]);
	    v_aux = minValue(game, depth - 1, alpha, beta);
	    if(v_aux>v){v=v_aux; this.column = moves[i];}
	    unmakeMove(game,moves[i],row);
	    if (v >= beta)
		return v;
	    alpha = max(alpha, v);
	}

	if(terminate){
	    //    System.out.print("\n TERMINATE \n");
	    return Integer.MAX_VALUE;
	}else{
	    return v;
	}
    }

    public int minValue(final GameStateModule game, int depth, int alpha, int beta){
	boolean gameIsOver = game.isGameOver();
	if (depth <= 0 || gameIsOver) return evaluationFunction(game, gameIsOver); // terminal test (a function of the depth)
	System.out.print("\n\nMIN\n\n");

	int v = -1*Integer.MAX_VALUE;
	int v_aux;
	int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    if(game.canMakeMove(i))
		moves[numLegalMoves++] = i;

	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    int row = makeMove(game,moves[i]);
	    this.column = moves[i];
	    v_aux = maxValue(game, depth - 1, alpha, beta);
	    if(v_aux<v){v=v_aux; this.column = moves[i];}
	    unmakeMove(game, moves[i], row);
	    if (v <= alpha)
		return v;
	    beta = min(beta, v);
	}

	if(terminate)
	    {
		System.out.print("\n TERMINATE \n");
		return -1*Integer.MAX_VALUE;
	    }
	else
	    {
		return v;
	    }
    }

    public int max(int a, int b){
	return (a>b)?a:b;
    }

    public int min(int a, int b){
	return (a<b)?a:b;
    }

    public int evaluationFunction(final GameStateModule game, boolean gameIsOver) {
	if(!gameIsOver) {
	    Iterator[] itr = {this.player1Ats.iterator(), this.player2Ats.iterator()};// 
	    int width = game.getWidth(), height = game.getHeight();
	    int x, y;
	    int[] score = {0,0}, weights = {1,2,3,4,3,2,1};
	    
	    
	    for (int p = 0; p < 2; p++) {// iterate for each player
		while(itr[p].hasNext()) {// computes the score for each player's board points. 
		    Point element = (Point)itr[p].next();
		    x = (int)element.getX();
		    y = (int)element.getY();
		    for(int dx = -1; dx <= 1; dx++) {
			for(int dy = -1; dy <= 1; dy++) {
			    if(x + dx < 0 || x + dx >= width || y + dy < 0 || y + dy >= height)
				// adjacent point out of bounds
				continue;
			    if(game.getAt(x+dx,y+dy) == p+1) {
				// adjacent point belongs to the player being evaluated
				if(game.getAt(x-dx, y-dy) == p+1) {
				    // (x+dx,y+dy) make three in a row with (x,y);
				    score[p] += 2*weights[y]; 
				    // it must be weighted in accordance with column, since it is known
				    // that who controls the middle has more advantage
				}else {
				    score[p] += 1*weights[]; // two in a row is better than nothing.
				}
			    }else{
				// the adjancent point or is empty or it belongs to another player, hence without importance
				continue;
			    }
			}
		    }
		}
	    }
	    if(our_player == 1){	
		System.out.print("\nscore: " + (score[0] - score[1]));
		return score[0] - score[1];
	    }else{
		System.out.print("\nscore: " + (score[1] - score[0]));
		return score[1] - score[0];
	    }
	}else {
	    // returns +infinity if  winner==ourPlayer; -infinity otherwise;
	    if(game.getWinner() == our_player){
		return Integer.MAX_VALUE;
	    }else{
		return -1*Integer.MAX_VALUE;
	    }
	}
    }
}

    // public int evaluationFunction(final GameStateModule game)
//     {
// 	int width = game.getWidth();
// 	int height = game.getHeight();
// 	int tmpPlayer = game.getActivePlayer();
// 	int tmp, k, twoSets, threeSets, opponentTwoSets, opponentThreeSets;
// 	twoSets = 0;
// 	threeSets = 0;
// 	opponentThreeSets = 0;
// 	opponentTwoSets = 0;
// 	// for each column
// 	for(int i = 0; i < width; i++)
// 	    // for each row
// 	    for(int j = 0; j < height; j++)
// 		// if not empty
// 		if(game.getAt(i,j) != 0)
// 		    {
// 			for(int dx = -1; dx <= 1; dx++)

// 			    for(int dy = -1; dy <= 1; dy++)
// 				{
// 				    if(dx == 0 && dy == 0)
// 					continue;

// 				    // If extent is out of bounds, abort.
// 				    if(i + 3 * dx < 0 || i + 3 * dx >= width || j + 3 * dy < 0 || j + 3 * dy >= height)
// 					continue;


// 				    k = 1;
// 				    for(int step = 1; step <= 3; step++)
// 					{
// 					    if(k == 2)
// 						{
// 						    if(tmpPlayer == game.getAt(i,j))
// 							twoSets++;
// 						    else
// 							opponentTwoSets++;
// 						}
// 					    else if(k == 3)
// 						{
// 						    if(tmpPlayer == game.getAt(i,j))
// 							threeSets++;
// 						    else
// 							opponentThreeSets++;
// 						}
// 					    if(game.getAt(i,j) != game.getAt(i + step * dx,j + step * dy))
// 						{
// 						    k = 1;
// 						    break;
// 						}

// 					    k++;
// 					}
// 				    if(k == 4)
// 					{
// 					    //		System.out.print("VIC");
// 					    if(game.getAt(i,j) == our_player)
// 						{
// 						    //			System.out.print("w1k");
// 						    tmp = 1000;
// 						}
// 					    else
// 						{
// 						    //			System.out.print("w-1k");
// 						    tmp = -1000;
// 						}
// 					    //		System.out.print("P");
// 					    return tmp;
// 					}
// 				}
// 		    }
// 	//	System.out.print("T" + threeSets+ "G" + twoSets);
// 	tmp = 3*threeSets + 2*twoSets - 3*opponentThreeSets - 2*opponentTwoSets;
// 	//	System.out.print("w" + tmp);
// 	return tmp;
//     }
// }
