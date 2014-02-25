//DirtyAI

public class DirtyAI extends AIModule
{
	public class boardState
	{
		int last;
		int[7][6] boardBox;
		int refind;
		boardState()
		{
			boardBox = new int[7][6];
			for(int i = 0; i < getWidth(); i++)
				for(int j = 0; j < getHeight(); j++)
					boardBox[i][j] = 0;
			last = 0;
		}
		boardState(int move, int player, int refind)
		{
			boardBox = new int[7][6];
			for(int i = 0; i < 7; i++)
				System.arraycopy(reference.get(refind).boardBox[i], 0, boardBox[i], 0, boardBox[i].length());
			for(int j = 0; j < 6; j++)
			{
				if(boardBox[move][i] == 0)
				{
					boardBox[move][i] = player;
					continue;
				}
				else
				{
					//error
				}
			}
		}
	}

	ArrayList<boardState> reference;
	ArrayList<boardState> build;
	int alpha, beta, initDepth, depth, myMove, targetDepth;
	double rando;

	DirtyAI()
	{
		alpha = -infinity;
		beta = -infinity;
		reference = new ArrayList<boardState>();
		build = new ArrayList<boardState>();
		initDepth = 1;
		depth = 1;
	}

	@Override
	public void getNextMove(final GameStateModule state)
	{
		reference.clear();
		build.clear();
		refind = -1;
		newstate = new boardState();
		for(int i = 0; i < 7; i++)
			for (int j = 0; j < 6; j++)
				newstate.boardBox[i][j] = state.getAt(i,j);
		reference.add(newstate);

		abSearch();
	}
	private void abSearch()
	{
		targetDepth = 1;
		while(!terminate)
		{
			depth = 1;
			maxVal(alpha,beta);
			updateMove();
			targetDepth++;

			reference.clear();
			reference.addAll(build);
			build.clear();
		}
	}
	private int maxVal(int alpha, int beta, int trymove)
	{
		if(depth == targetDepth)
		{	// create new boardState object and add it to the list for next time. then evaluate.
			nextState boardState = new boardState(trymove, 1, refind);
			build.add(nextState);
			return eval(nextState);
		}
		if(depth == targetDepth - 1)
		{
			refind++;					// this is to keep track of which item from reference we will use
		}								// to build the next row.

		int v = -infinity;
		for(int i = 0; i < 7; i++)
		{
			v = max(v, minVal(alpha,beta,i),i)
			if(v >= beta)
				return v;
			alpha = max(alpha, v);
		}
		return v;
	}
	private int minVal(int alpha, int beta)
	{
		// need to mirror maxVal here with a few changes.
	}
	private int eval(int nextmove)
	{
		rando = Math.random();
		// need to write a simple evaluation function. shouldnt be too complicated,
		// we can always add to it later.
	}
	private void updateMove()
	{
		chosenMove = myMove;
	}
	private int max(int l, int r, int i)
	{											// clumsy way of keeping track of which move to make.
		if(l > r)								// when v is changed, this function sets myMove to 
			return l;							// remember which branch. we probably want a better
		else									// solution.
		{
			myMove = i;
			return r;
		}
	}
	private int min(int l, int r)
	{
		if(l < r)
			return l;
		else
			return r;
	}
}
