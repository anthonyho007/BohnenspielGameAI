package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;
import bohnenspiel.BohnenspielPlayer;
import bohnenspiel.BohnenspielMove.MoveType;
import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends BohnenspielPlayer {
    private static final int TIME_LIMIT_1 = 10000;
    private static final int TIME_LIMIT_2 = 700;
    public boolean stop_search = false;
    private static final int CUT_OFF = 500000;
    private static final int prob = 8;
    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { super("260501840"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class
bohnenspiel.RandomPlayer
     * for another example agent. */
    public BohnenspielMove chooseMove(BohnenspielBoardState board_state)
    {
        // Get the contents of the pits so we can use it to make decisions.
        //int[][] pits = board_state.getPits();

        // Use ``player_id`` and ``opponent_id`` to get my pits and opponent pits.
        //int[] my_pits = pits[player_id];
        //int[] op_pits = pits[opponent_id];

        // Use code stored in ``mytools`` package.
        //MyTools.getSomething();

        // Get the legal moves for the current board state.
        
//        ArrayList<BohnenspielMove> moves = board_state.getLegalMoves();
//        BohnenspielMove move = moves.get(0);
//  
//     
//        // We can see the effects of a move like this...
//        BohnenspielBoardState cloned_board_state = (BohnenspielBoardState) board_state.clone();
//        cloned_board_state.move(move);
//
//
//        // But since this is a placeholder algorithm, we won't act on that information.
//        return move;
        
        
        // AI using alpha beta pruning with iterative deepening
        int steps = 0;
        BohnenspielMove pickedMove = null;
        int bestScore = Integer.MIN_VALUE;
        
        ArrayList<BohnenspielMove> moves = board_state.getLegalMoves();
        Collections.shuffle(moves);
        for(BohnenspielMove move : moves)
        {
            BohnenspielBoardState cloned_state = (BohnenspielBoardState) board_state.clone();
            cloned_state.move(move);
            long allow_time = (TIME_LIMIT_2 - 75)/(moves.size());
//          if (steps == 0)
//          {
//              allow_time = (TIME_LIMIT_1 - 75)/(moves.size());
//          } else {
//              allow_time = (TIME_LIMIT_2 - 75)/(moves.size());
//          }
            
        
            int value = id_search(cloned_state, allow_time);
            
            if (value >= CUT_OFF)
            {
                return move;
            }
            
            if (value > bestScore)
            {
                bestScore = value;
                pickedMove = move;
            }
            
            
        }
        steps++;
        return pickedMove;
        
    }
    
    public int id_search(BohnenspielBoardState board_state, long limit)
    {
        int value = 0;
        int depth = 10;
        long start = System.currentTimeMillis();
        long end = start + limit;
        while (true){
            long current = System.currentTimeMillis();
            if (current >= end)
            {
                break;
            }
            int chance = 4;
            value = ab_search( board_state, depth, Integer.MIN_VALUE, Integer.MIN_VALUE, current, end -current, chance);
            
            if (stop_search || value >= CUT_OFF){
                break;
            }
            depth++;
        }
        
        return value;
    }
    
    public int ab_search( BohnenspielBoardState board_state, int depth, int a, int b, long start, long limit, int chance)
    {

        ArrayList<BohnenspielMove> moves = board_state.getLegalMoves();
        Collections.shuffle(moves);
        int current_id = board_state.getTurnPlayer();
        boolean opp_turn = ( current_id == opponent_id) ? true : false;
        int node_value = (opp_turn) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        int value = MyTools.getHeuristicScore(board_state, player_id, opponent_id);
        long current = System.currentTimeMillis();
        long past = current - start;
        if ( past >= limit)
        {
            stop_search = true;
        }
        
        if ( stop_search || moves.size() == 0 || depth == 0 || value >= CUT_OFF || value <= -CUT_OFF)
        {
            return value;
        }
        
        if (opp_turn){
            for(BohnenspielMove move : moves)
            {
                //System.out.println("opp");
                BohnenspielBoardState next_state = (BohnenspielBoardState) board_state.clone();
                next_state.move(move);
                
                b = Math.min(b, ab_search(next_state, depth -1, a, b, start, limit, chance));
                if ( b <= a){
                    break;
                }
            }
            return b;
        } else {
            for(BohnenspielMove move : moves)
            {
                //System.out.println("me");
                Random rand = new Random();
                BohnenspielBoardState next_state = (BohnenspielBoardState) board_state.clone();
                next_state.move(move);
                if (chance > 1)
                {
                    chance--;
                }
                
                if (rand.nextInt(10) > (prob + (10 - prob)/chance)){
                    a = Math.max(a, ab_search(next_state, depth -1, a, b, start, limit, chance));
                }
                
                if ( b <= a){
                    break;
                }
            }
            return a;
        }
        
    }

    
}