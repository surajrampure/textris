/*

Piece.java
Suraj Rampure

This class contains static information about each of the Tetris pieces.
Each "piece" is a 3 dimensional array of length 4 - in each spot is a 2 dimensional array
holding the arrangement of the given piece at that rotation (there are 4 rotations)

*/

import java.util.*;
import java.awt.*;

class Piece {

    // 4x4 arrays for each piece
    final public static int [][][] O_PIECE = {
        {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}};

    final public static int [][][] I_PIECE = {
        {{0, 0, 0, 0}, {2, 2, 2, 2}, {0, 0, 0, 0}, {0, 0, 0, 0}},
        {{0, 0, 2, 0}, {0, 0, 2, 0}, {0, 0, 2, 0}, {0, 0, 2, 0}},
        {{0, 0, 0, 0}, {0, 0, 0, 0}, {2, 2, 2, 2}, {0, 0, 0, 0}},
        {{0, 2, 0, 0}, {0, 2, 0, 0}, {0, 2, 0, 0}, {0, 2, 0, 0}}};

    final public static int [][][] S_PIECE = {
        {{0, 3, 3, 0}, {3, 3, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
        {{0, 3, 0, 0}, {0, 3, 3, 0}, {0, 0, 3, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {0, 3, 3, 0}, {3, 3, 0, 0}, {0, 0, 0, 0}},
        {{3, 0, 0, 0}, {3, 3, 0, 0}, {0, 3, 0, 0}, {0, 0, 0, 0}}};

    final public static int [][][] Z_PIECE = {
        {{4, 4, 0, 0}, {0, 4, 4, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
        {{0, 0, 4, 0}, {0, 4, 4, 0}, {0, 4, 0, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {4, 4, 0, 0}, {0, 4, 4, 0}, {0, 0, 0, 0}},
        {{0, 4, 0, 0}, {4, 4, 0, 0}, {4, 0, 0, 0}, {0, 0, 0, 0}}};

    final public static int [][][] L_PIECE = {
        {{0, 0, 5, 0}, {5, 5, 5, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
        {{0, 5, 0, 0}, {0, 5, 0, 0}, {0, 5, 5, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {5, 5, 5, 0}, {5, 0, 0, 0}, {0, 0, 0, 0}},
        {{5, 5, 0, 0}, {0, 5, 0, 0}, {0, 5, 0, 0}, {0, 0, 0, 0}}};

    final public static int [][][] J_PIECE = {
        {{6, 0, 0, 0}, {6, 6, 6, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
        {{0, 6, 6, 0}, {0, 6, 0, 0}, {0, 6, 0, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {6, 6, 6, 0}, {0, 0, 6, 0}, {0, 0, 0, 0}},
        {{0, 6, 0, 0}, {0, 6, 0, 0}, {6, 6, 0, 0}, {0, 0, 0, 0}}};

    final public static int [][][] T_PIECE = {
        {{0, 7, 0, 0}, {7, 7, 7, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
        {{0, 7, 0, 0}, {0, 7, 7, 0}, {0, 7, 0, 0}, {0, 0, 0, 0}},
        {{0, 0, 0, 0}, {7, 7, 7, 0}, {0, 7, 0, 0}, {0, 0, 0, 0}},
        {{0, 7, 0, 0}, {7, 7, 0, 0}, {0, 7, 0, 0}, {0, 0, 0, 0}}};


    final public static int [][][][] PIECES = {
        O_PIECE, I_PIECE, S_PIECE, Z_PIECE,
        L_PIECE, J_PIECE, T_PIECE};

    final public static Color [] COLORS1 = {
        new Color (193, 233, 43),           //O_PIECE
        new Color (25, 221, 255),           //I_PIECE
        new Color (51, 255, 211),           //S_PIECE
        new Color (255, 0, 0),              //Z_PIECE
        new Color (255, 204, 51),           //L_PIECE
        new Color (51, 140, 255),           //J_PIECE
        new Color (171, 39, 142),           //T_PIECE
        new Color (99, 99, 99)};            //Ghost Piece
}