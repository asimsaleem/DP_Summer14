public class SudokuSolver {

   // "Near worst case" example from
   // http://en.wikipedia.org/wiki/Sudoku_algorithms
  public static final String testCaseString =
				"153 " + "178 " + "185 " +
				 "221 " + "242 " +
			 	 "335 " + "357 " +
				 "424 " + "461 " +
				 "519 " +
				 "605 " + "677 " + "683 " +
				 "722 " + "741 " +
				 "844 " + "889 ";

  public static final int N = 9;
  
  public static void main(String[] args) {
    String result = solve( testCaseString );
    boolean correct = isLegalSolution( result, testCaseString );
      System.out.println("Your solver ouput is " + (correct ? "right" : "wrong"));
  }
  
  //TODOBEGIN(DP)
  
  public static String solve ( String s ){
	  /*** START ***/
	  String result = null;

	  //Read the Input Sudoku String that needs to be solved
	  int[][] matrixToSolve = readInput(s);
	  //print("Initial:", matrixToSolve);
	  
	  //Invoke the Solve method to figure out a solution for the passed in Sudoku Problem
	  if (solve(0,0,matrixToSolve))  
		  result = matrixToString(matrixToSolve);
	  
	  
	  //Return the Solved Sudoku problem to the Calling function
	  return result;
	  /**** END ***/
  }
  
  private static boolean solve(int i, int j, int[][] cells) {
	  
	  //Check if all the Rows and Columns have been processed
      if (i == 9) {
          i = 0;
          if (++j == 9)
              return true;
      }
      
      //Skip the already filled cells
      if (cells[i][j] != 0) 
          return solve(i+1,j,cells);
      
      //Solve for Cells that have not been already Filled
      for (int val = 1; val <= 9; ++val) {
          if (legal(i,j,val,cells)) {
              cells[i][j] = val;
              if (solve(i+1,j,cells))
                  return true;
          }
      }
      
      //Reset on backtrack
      cells[i][j] = 0; 
      return false;
  }
  
  
  private static boolean legal(int i, int j, int val, int[][] cells) {
     
	  //Row Checker
	  for (int k = 0; k < 9; ++k)  
          if (val == cells[k][j])
              return false;
	  
	  //Column Checker
      for (int k = 0; k < 9; ++k) 
          if (val == cells[i][k])
              return false;

      int boxRowOffset = (i / 3)*3;
      int boxColOffset = (j / 3)*3;
      for (int k = 0; k < 3; ++k){// box
    	  for (int m = 0; m < 3; ++m){
              if (val == cells[boxRowOffset+k][boxColOffset+m]){
            	  //Violation encountered. Indicates that it is not a valid solution
            	  return false;
              }
    	  }
      }
      
      //No violations encountered, so it's legal
      return true; 
  }
 //TODOEND(DP)

  public static boolean isLegalSolution( String solution, 
  				String initialConfig ) {

    int [][] solutionmatrix = readInput( solution );
    int [][] initialConfigMatrix = readInput( initialConfig );

    if ( !isValid (solutionmatrix) ) {
      return false;
    }

    // check that it's fully filled in
    for (int i = 0; i < N; ++i) {
      for (int j = 0; j < N; ++j) {
        if ( solutionmatrix[i][j] == 0 
                 || solutionmatrix[i][j] < 0 
                 || solutionmatrix[i][j] > 9 ) {
          return false;
        }
        // check that it matches with initialConfigMatrix
        if ( initialConfigMatrix[i][j] != 0  &&
             initialConfigMatrix[i][j] != solutionmatrix[i][j] ) {
           return false;
         }
      }
    }
    return true;
  }

  // Check if a partially filled matrix has any conflicts
  public static boolean isValid(int[][] matrix) {
    // Row constraints
    for (int i = 0; i < N; ++i) {
      boolean[] present = new boolean[N + 1];
      for (int j = 0; j < N; ++j) {
        if (matrix[i][j] != 0 && present[matrix[i][j]]) {
          return false;
        } else {
          present[matrix[i][j]] = true;
        }
      }
    }

    // Column constraints
    for (int j = 0; j < N; ++j) {
      boolean[] present = new boolean[N + 1];
      for (int i = 0; i < N; ++i) {
        if (matrix[i][j] != 0 && present[matrix[i][j]]) {
          return false;
        } else {
          present[matrix[i][j]] = true;
        }
      }
    }

    // Region constraints
    for (int I = 0; I < 3; ++I) {
      for (int J = 0; J < 3; ++J) {
        boolean[] present = new boolean[N + 1];
        for (int i = 0; i < 3; ++i) {
          for (int j = 0; j < 3; ++j) {
            if (matrix[3 * I + i][3 * J + j] != 0 &&
                present[matrix[3 * I + i][3 * J + j]]) {
              return false;
            } else {
              present[matrix[3 * I + i][3 * J + j]] = true;
            }
          }
        }
      }
    }
    return true;
  }

  public static int[][] readInput( String arg ) {
	String [] args = arg.split("\\s");
    return readInput( args );
  }

   public static int[][] readInput( String[] args ) {
     int[][] result = new int[N][];
     for ( int k = 0 ; k < N; k++ ) {
       result[k] = new int[N];
     }
     for ( int k = 0 ; k < args.length; k++ ) {
       int val = new Integer( args[k] );
       // format: 634 -> in row 6, col 4 value is 4
       result[val / 100][(val % 100) / 10] = (val % 10);
     }
     return result;
   }

   static String matrixToString(int[][] matrix) {
     String result = "";
     for ( int i  = 0 ; i < matrix.length; i++ ) {
       for ( int j  = 0 ; j < matrix[0].length; j++ ) {
         result = result + i + j + matrix[i][j] + " ";
       }
     }
     return result;
   }


   static void print(String msg, int[][] matrix) {
     System.out.println(msg);
     for ( int i  = 0 ; i < matrix.length; i++ ) {
       for ( int j  = 0 ; j < matrix[0].length; j++ ) {
         System.out.print(matrix[i][j] + ( (j < 8) ? " " : "\n" ) );
       }
     }
   }
}
