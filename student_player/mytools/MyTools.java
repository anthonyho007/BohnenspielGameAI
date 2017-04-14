package student_player.mytools;

import bohnenspiel.BohnenspielBoardState;

public class MyTools{
	private static BohnenspielBoardState board_state = null;
	private static int player;
	//private static int player_ind;
	//private static int opp_ind;
	private static int opp;
	private static int[][] board;
	
	public static void getSomething()
	{
		
	}
	
	private static int win_score(boolean turnPlay)
	{
		if (turnPlay)
		{
			if (board_state.getWinner() == player)
			{
				return 1;
			} else {
				return 0;
			}
		} else {
			if (board_state.getWinner() == opp){
				return 1;
			} else {
				return 0;
			}
		}
	}
	private static int num_stone(boolean turnPlay)
	{
		int num_stone =0;
		if (turnPlay)
		{
			for(int i = 0; i < board[0].length; i++)
			{
				num_stone += board[player][i];
			}
			return num_stone;
		} else {
			for(int i = 0; i < board[0].length; i++)
			{
				num_stone += board[opp][i];
			}
			return num_stone;
		}
	}
	
	public static int get_score(boolean turnPlay)
	{
		if (turnPlay)
		{
			return board_state.getScore(player);
		} else {
			return board_state.getScore(opp);
		}
		
	}
	public static int get_even()
	{
		int counter = 0;
		
		for(int i = 0;i < 2;i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				if(board[i][j] % 2 == 0)
				{
					counter++;
				}
			}
		}
		
		if (board_state.getTurnPlayer() == player)
		{
			return counter;
		} else {
			return 0 - counter;
		}
	}

    public static int getHeuristicScore(BohnenspielBoardState game_state, int player_id, int opp_id){
    	int score = 0;
    	board_state = game_state;
//    	if ( board_state.firstPlayer() == player_id)
//    	{
//    		player_ind = 0;
//    		opp_ind =1;
//    	} else {
//    		opp_ind = 0;
//    		player_ind = 1;
//    	}
    	player = player_id;
    	opp = opp_id;
    	board = game_state.getPits();
    	
    	int[] playerScore = new int[4];
    	int[] opponentScore = new int[4];
    	for(int i = 0; i < 4; i++)
    	{
    		playerScore[i] = 0;
    		opponentScore[i] = 0;
    	}
    	playerScore[0] = win_score(true);
    	opponentScore[0] = win_score(false);
    	playerScore[1] = num_stone(true);
    	opponentScore[1] = num_stone(false);
    	playerScore[2] = get_score(true);
    	opponentScore[2] = get_score(false);
    	playerScore[3] = get_even();
    	//1000000*(playerScore[0] - opponentScore[0]) + 
    	score =30* (playerScore[1] -opponentScore[1]) + 50 *(playerScore[2] -opponentScore[2]) + 15*(playerScore[3]);
    	//System.out.println(score);
        return score;
    }  
}
