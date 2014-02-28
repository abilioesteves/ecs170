import java.util.Random;

public class ABDirtyAI extends AIModule{
    private int our_player;
    public int column;

    @Override
    public void getNextMove(final GameStateModule state){
	int i = 1;
	final GameStateModule game = state.copy();

	while (!terminate){
	    System.out.print("depth: " + i + "\n\n");
	    interactiveDeepeningAlphaBetaSearch(game, i);
	    i++;
	}
    }

    public void interactiveDeepeningAlphaBetaSearch(final GameStateModule game, int depth){
	int v;
	our_player = game.getActivePlayer();

	v = maxValue(game, depth, -1*Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	chosenMove = this.column;
    }

    public int maxValue(final GameStateModule game, int depth, int alpha, int beta){
	if (depth <= 0 || game.isGameOver()) return evaluationFunction(game); // terminal test (a function of the depth)

	int v = -1*Integer.MAX_VALUE;
	int v_aux;
	int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    if(game.canMakeMove(i))
		moves[numLegalMoves++] = i;

	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    game.makeMove(moves[i]);
	    v_aux = minValue(game, depth - 1, alpha, beta);
	    if(v_aux>v){v=v_aux; this.column = moves[i];}
	    game.unMakeMove();
	    if (v >= beta)
		return v;
	    alpha = max(alpha, v);
	}

	if(terminate){
	    System.out.print("\n TERMINATE \n");
	    return Integer.MAX_VALUE;
	}else{
	    return v;
	}
    }

    public int minValue(final GameStateModule game, int depth, int alpha, int beta){
	if (depth <= 0 || game.isGameOver()) return evaluationFunction(game); // terminal test (a function of the depth)

	int v = -1*Integer.MAX_VALUE;
	int v_aux;
	int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    if(game.canMakeMove(i))
		moves[numLegalMoves++] = i;

	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    game.makeMove(moves[i]);
	    this.column = moves[i];
	    v_aux = maxValue(game, depth - 1, alpha, beta);
	    if(v_aux<v){v=v_aux; this.column = moves[i];}
	    game.unMakeMove();
	    if (v <= alpha)
		return v;
	    beta = min(beta, v);
	}

	if(terminate){
	    System.out.print("\n TERMINATE \n");
	    return -1*Integer.MAX_VALUE;
	}else{
	    return v;
	}
    }

    public int max(int a, int b){
	return (a>b)?a:b;
    }

    public int min(int a, int b){
	return (a<b)?a:b;
    }

    public int evaluationFunction(final GameStateModule game){
	int[] weight = {1,100,10000,1000000,10000,100,1};
	int result = r.nextInt(1000)*weight[this.column];
	return (game.getActivePlayer() == our_player)?-1*result:result;
    }
}
