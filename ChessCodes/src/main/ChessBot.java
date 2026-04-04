package main;
import java.util.*;

public class ChessBot {
    private ChessBoard board; // Assuming ChessBoard is the class representing the board
    private String botColor;
    private  int depth = 4; // Depth limit for the search tree
    public static int noOfsearchTrees;
    public ChessBot(ChessBoard board, String botColor) {
        this.board = board;
        this.botColor = botColor;
    }

    /*public int[] findBestMove() {
        int[] bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        // Iterate over all possible moves for the bot color
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(i, j);
                if (piece != null && piece.getColor().equals(botColor)) {
                    List<int[]> legalMoves = piece.getLegalMoves(board);

                    for (int[] move : legalMoves) {
                        // Make the move
                        ChessPiece capturedPiece = board.getPiece(move[0], move[1]);
                        board.movePiece(i, j, move[0], move[1]);

                        // Evaluate the move using minimax with alpha-beta pruning
                        int moveValue = minimax(MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                        // Undo the move
                        board.movePiece(move[0], move[1], i, j);
                        board.setPiece(move[0], move[1], capturedPiece);

                        if (moveValue > bestValue) {
                            bestValue = moveValue;
                            bestMove = new int[]{i, j, move[0], move[1]};
                        }
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || isGameOver()) {
            return evaluateBoard();
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            // Iterate over all pieces for the bot's color
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece piece = board.getPiece(i, j);
                    if (piece != null && piece.getColor().equals(botColor)) {
                        List<int[]> moves = piece.getLegalMoves(board);
                        for (int[] move : moves) {
                            ChessPiece capturedPiece = board.getPiece(move[0], move[1]);
                            board.movePiece(i, j, move[0], move[1]);

                            int eval = minimax(depth - 1, alpha, beta, false);
                            maxEval = Math.max(maxEval, eval);
                            alpha = Math.max(alpha, eval);

                            board.movePiece(move[0], move[1], i, j);
                            board.setPiece(move[0], move[1], capturedPiece);

                            if (beta <= alpha) {
                                break; // Alpha-beta pruning
                            }
                        }
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            // Iterate over all pieces for the opponent's color
            String opponentColor = botColor.equals("White") ? "Black" : "White";
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece piece = board.getPiece(i, j);
                    if (piece != null && piece.getColor().equals(opponentColor)) {
                        List<int[]> moves = piece.getLegalMoves(board);
                        for (int[] move : moves) {
                            ChessPiece capturedPiece = board.getPiece(move[0], move[1]);
                            board.movePiece(i, j, move[0], move[1]);

                            int eval = minimax(depth - 1, alpha, beta, true);
                            minEval = Math.min(minEval, eval);
                            beta = Math.min(beta, eval);

                            board.movePiece(move[0], move[1], i, j);
                            board.setPiece(move[0], move[1], capturedPiece);

                            if (beta <= alpha) {
                                break; // Alpha-beta pruning
                            }
                        }
                    }
                }
            }
            return minEval;
        }
    }*/

    /*public int[] findBestMove(String currentPlayer) {
        List<int[]> bestMoves = new ArrayList<>();
        int bestEval = (currentPlayer.equals("White")) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(i, j);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    List<int[]> legalMoves = piece.getLegalMoves(board);
                    for (int[] move : legalMoves) {
                        // Try the move
                        ChessPiece capturedPiece = board.getPiece(move[0], move[1]);
                        board.movePiece(i, j, move[0], move[1]);

                        int evaluation = minimax(MAX_DEPTH - 1, alpha, beta, currentPlayer.equals("White") ? false : true);

                        // Undo the move
                        board.movePiece(move[0], move[1], i, j);
                        if (capturedPiece != null) {
                            board.setPiece(move[0], move[1], capturedPiece);
                        }

                        // Update best evaluation and alpha-beta values
                        if (currentPlayer.equals("White") && evaluation > bestEval) {
                            bestEval = evaluation;
                            alpha = Math.max(alpha, evaluation);
                            bestMoves.clear();
                            bestMoves.add(new int[]{i, j, move[0], move[1]});
                        } else if (currentPlayer.equals("Black") && evaluation < bestEval) {
                            bestEval = evaluation;
                            beta = Math.min(beta, evaluation);
                            bestMoves.clear();
                            bestMoves.add(new int[]{i, j, move[0], move[1]});
                        } else if (evaluation == bestEval) {
                            bestMoves.add(new int[]{i, j, move[0], move[1]});
                        }

                        // Prune branches
                        if (beta <= alpha) {
                            break; // Stop exploring further moves
                        }
                    }
                }
            }
        }

        // Pick a random move from the list of best moves
        if (!bestMoves.isEmpty()) {
            Random rand = new Random();
            return bestMoves.get(rand.nextInt(bestMoves.size()));
        }

        return null; // No legal move found
    }

    /*private int minimax(ChessBoard board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || isGameOver()) {
            //return evaluateBoard(board, isMaximizingPlayer ? "White" : "Black");
            return evaluateBoard();
        }

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getAllLegalMoves("White")) {
                board.makeMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                board.undoMove(move);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // Beta cutoff
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getAllLegalMoves("Black")) {
                board.makeMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                board.undoMove(move);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break; // Alpha cutoff
            }
            return minEval;
        }
    }*/

    public int[] findBestMove(int depth) {
        noOfsearchTrees=0;
        int bestEval = Integer.MIN_VALUE;
        List<int[]> bestMoves = new ArrayList<>();

        for (int[] move : getAllLegalMoves(board, botColor)) {
            ChessBoard nextBoard = board.cloneBoard();
            nextBoard.movePieceWithValidation(move[0], move[1], move[2], move[3]);
            int eval = minimax(nextBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (eval > bestEval) {
                bestEval = eval;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (eval == bestEval) {
                bestMoves.add(move);
            }
        }
        System.out.println("NUmber of evaluvations: "+noOfsearchTrees);

        //ranodm bestmove
        if (!bestMoves.isEmpty()) {
            Random rand = new Random();
            return bestMoves.get(rand.nextInt(bestMoves.size()));
        }

        //shouldnt be called
        return null;
    }

    private int minimax(ChessBoard position, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || isGameOver(position)) {
            return evaluateBoard(position);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (int[] move : getAllLegalMoves(position, botColor)) {
                ChessBoard nextBoard = position.cloneBoard();
                nextBoard.movePieceWithValidation(move[0], move[1], move[2], move[3]);

                int eval = minimax(nextBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    return maxEval;
                }
            }
            return maxEval;

        } else {
            int minEval = Integer.MAX_VALUE;
            String opponentColor = botColor.equals("White") ? "Black" : "White";
            for (int[] move : getAllLegalMoves(position, opponentColor)) {
                ChessBoard nextBoard = position.cloneBoard();
                nextBoard.movePieceWithValidation(move[0], move[1], move[2], move[3]);

                int eval = minimax(nextBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) {
                    return minEval;
                }
            }
            return minEval;
        }
    }


    private boolean isGameOver(ChessBoard position) {
        return position.isCheckmate(botColor)
                || position.isCheckmate(getOpponentColor())
                || position.isStalemate(botColor)
                || position.isStalemate(getOpponentColor());
    }

    private String getOpponentColor() {
        return botColor.equals("White") ? "Black" : "White";
    }

    /*private int evaluateBoard() {
        int score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(i, j);
                if (piece != null) {
                    score += piece.getColor().equals(botColor) ? piece.getValue() : -piece.getValue();
                }
            }
        }
        return score;
    }*/

    private int evaluateBoard(ChessBoard position) {
        noOfsearchTrees++;
        int score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = position.getPiece(i, j);
                if (piece != null) {
                    int pieceValue = piece.getValue();
                    score += piece.getColor().equals(botColor) ? pieceValue : -pieceValue;
                }
            }
        } 

        String opponentColor = botColor.equals("White") ? "Black" : "White";

        if (position.isInCheck(opponentColor) && position.isCheckmate(opponentColor)) {
            return Integer.MAX_VALUE - 1;
        }

        if (position.isInCheck(botColor) && position.isCheckmate(botColor)) {
            return Integer.MIN_VALUE + 1;
        }
        return score;
    }

    private List<int[]> getAllLegalMoves(ChessBoard position, String color) {
        List<int[]> legalMoves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = position.getPiece(row, col);
                if (piece != null && piece.getColor().equals(color)) {
                    for (int[] move : piece.getLegalMoves(position)) {
                        if (position.isLegalMove(row, col, move[0], move[1], color)) {
                            legalMoves.add(new int[]{row, col, move[0], move[1]});
                        }
                    }
                }
            }
        }

        return legalMoves;
    }
}
