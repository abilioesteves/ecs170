import java.util.Random;

public class DirtyAI2 extends AIModule{
    private final Random r = new Random(System.currentTimeMillis());

    @Override
    public void getNextMove(final GameStateModule state){
	int i = 1;
	final GameStateModule game = state.copy();
	while (!terminate){
	    System.out.print("depth: " + i + "\n\n");
	    interactiveDeepeningMiniMaxDecision(game, i);
	    i++;
	}
    }

    public void interactiveDeepeningMiniMaxDecision(final GameStateModule game, int depth){
	int result_value = -1*Integer.MAX_VALUE;
	int result_value_aux;
	int column = 0;
	final int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    if(game.canMakeMove(i))
		moves[numLegalMoves++] = i;
	
	// For each possible action, returns the maximum value given depth
	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    game.makeMove(moves[i]);
	    result_value_aux = minValue(game, depth - 1, moves[i]);
	    System.out.print("\n result_value_aux " + result_value_aux);
	    if (result_value_aux >= result_value){// update result if the new result is greater than the previous calculated max
		column = moves[i];
		result_value = result_value_aux;
	    }
	    game.unMakeMove();
	}

	chosenMove = (terminate)?chosenMove:column;
    }
    
    public int minValue(final GameStateModule game, int depth, int columm){
	int v = Integer.MAX_VALUE;
	final int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;
	int v_aux;

	if (depth <= 0 || game.isGameOver()) return evaluationFunction(game, 1, columm); // 1 refers to MAX; 2 refers to MIN
	

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    if(game.canMakeMove(i))
		moves[numLegalMoves++] = i;

	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    game.makeMove(moves[i]);
	    v_aux = maxValue(game, depth - 1, moves[i]);
	    if (v_aux <= v){
		v = v_aux;
	    }
	    game.unMakeMove();
	}

	if(terminate){
	    System.out.print("\n TERMINATE \n");
	    return Integer.MAX_VALUE;
	}else{
	    return v;
	}
    }

    public int maxValue(final GameStateModule game, int depth, int columm){
	int v = -1*Integer.MAX_VALUE;
	int[] moves = new int[game.getWidth()];
	int numLegalMoves = 0;
	int v_aux;

	if (depth <= 0 || game.isGameOver()) return evaluationFunction(game, 2, columm); // terminal test (a function of the depth)
	

	// Fill in what moves are legal.
	for(int i = 0; i < game.getWidth(); ++i)
	    if(game.canMakeMove(i))
		moves[numLegalMoves++] = i;

	for (int i = 0; i < numLegalMoves && !terminate; i++){
	    game.makeMove(moves[i]);
	    v_aux = minValue(game, depth - 1, moves[i]);
	    if (v_aux > v){
		v = v_aux;
	    }
	    game.unMakeMove();
	}
	if(terminate){
	    System.out.print("\n TERMINATE \n");
	    return Integer.MAX_VALUE;
	}else{
	    return v;
	}
    }

    public int evaluationFunction(final GameStateModule game, int player_index, int column){
	
	int[] weight = {1,10,100,1000,100,10,1};
	return (player_index == 1)?weight[column]:-1*weight[column];
    }
}
