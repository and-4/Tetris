package tetris;

import tetris.Tetris;

public class Moving {
	
	static void rotateMainFigure() {
        // поворачивает основную фигуру по часовой стрелке
        if ((Tetris.relativeCoord[1] + Tetris.currentFigure.length) > 10) {
        	// если фигура касается правой стенки
        	Tetris.isReadyForRotation = false;
            return;
        } else if ((Tetris.relativeCoord[1] +Tetris.currentFigure.length) <= 10 & (Tetris.relativeCoord[1] +Tetris.currentFigure.length) > 5) { 
        	// если фигура отодвинулась от правой стенки
           Tetris.isReadyForRotation = true;
        }
        for (int i = Tetris.relativeCoord[0]; i < Tetris.relativeCoord[0] +Tetris.currentFigure.length; i++) {  
        	// проверяем, есть ли в поле фигуры другие блоки 
            for (int j = Tetris.relativeCoord[1]; j < Tetris.relativeCoord[1] +Tetris.currentFigure.length; j++) {
                if (Tetris.bigGameArray[i][j] == 1) {
                   Tetris.isReadyForRotation = false;
                    return;
                }
            }
        }
        if (!Tetris.isReadyForRotation) {             // если все условия поворота соблюдены
            return;        
            }
        int[][] tempMatr = Tetris.getTempMatrix();    // делаем копию рабочей фигуры
        tempMatr = rotateMatrix(tempMatr);     // поворачиваем копию рабочей фигуры

        for (int i = 0; i <Tetris.currentFigure.length; ++i) {
            for (int j = 0; j <Tetris.currentFigure.length; ++j) {
            	Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1]] = 0;     // стираем старую рабочую фигуру
                if (tempMatr[i][j] == 2) {
                	Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1]] = 2;  // вставляем повернутую фигуру 
                }
            }
        }
    }
	
	static void moveMainFigureLeft() {
    	// проверяет, можно ли сместить рабочую фигуру влево
        int counterForMove = 0;
        for (int i = 0; i <Tetris.currentFigure.length; ++i) {                 // расчет ведется для каждого из 4 блоков фигуры
            for (int j = 0; j <Tetris.currentFigure.length; ++j) {
                if (j + Tetris.relativeCoord[1] > 0 & (i + Tetris.relativeCoord[0]) < 20 & (j + Tetris.relativeCoord[1]) < 10) {
                    if (Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1]] == 2 &
                    		Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1] - 1] != 1) {
                        counterForMove++;      
                    }
                    if (counterForMove == 4) {             // если все четыре блока готовы к смещению
                        forceMoveFigureLeft();
                        return;
                    }
                }
            }
        }
    }
	
	static void forceMoveFigureLeft() {
    	// смещает фигуру влево
        for (int p = 0; p <Tetris.currentFigure.length; ++p) {
            for (int q = 0; q <Tetris.currentFigure.length; ++q) {
                if ((q + Tetris.relativeCoord[1]) < 10 & (p + Tetris.relativeCoord[0]) < 20) {
                    if (Tetris.bigGameArray[p + Tetris.relativeCoord[0]][q + Tetris.relativeCoord[1]] == 2) {
                    	Tetris.bigGameArray[p + Tetris.relativeCoord[0]][q + Tetris.relativeCoord[1] - 1] = 2;
                    	Tetris.bigGameArray[p + Tetris.relativeCoord[0]][q + Tetris.relativeCoord[1]] = 0;
                    }
                }
            }
        }
        Tetris.relativeCoord[1]--;                  
        if (Tetris.relativeCoord[1] < 0) {                       // если фигура уперлась в левую стенку
           Tetris.isReadyForRotation = false;
            Tetris.relativeCoord[1] = 0;                         // оставляем relativeCoord на 0 индексе  (строка 83 Graphic)
            Tetris.shiftOfRelativeCoord = 1;                     // и запоминаем это исключительное положение
        }
    }
	
	static void moveMainFigureRight() {
    	// проверяет, можно ли сместить рабочую фигуру вправо  (аналогично смещению влево)
        int counterForMove = 0;
        for (int i = 0; i <Tetris.currentFigure.length; ++i) {
            for (int j =Tetris.currentFigure.length; j >= 0; --j) {
                if ((j - Tetris.relativeCoord[1] +Tetris.currentFigure.length + 1) < Tetris.bigGameArray[0].length) {
                    if ((j + Tetris.relativeCoord[1]) < 9 & (i + Tetris.relativeCoord[0]) < 20) {
                        if (Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1]] == 2 &
                        		Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1] + 1] != 1) {
                            counterForMove++;
                        }
                    }
                    if (counterForMove == 4) {
                        forceMoveFigureRight();
                        return;
                    }
                }
            }
        }
    }

    static void forceMoveFigureRight() {
    	// смещает фигуру вправо
        for (int i = 0; i <Tetris.currentFigure.length; ++i) {
            for (int j =Tetris.currentFigure.length; j >= 0; --j) {
                if ((j + Tetris.relativeCoord[1]) < 9 & (i + Tetris.relativeCoord[0]) < 20) {
                    if (Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1]] == 2) {
                    	Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1] + 1] = 2;
                    	Tetris.bigGameArray[i + Tetris.relativeCoord[0]][j + Tetris.relativeCoord[1]] = 0;
                    }
                }
            }
        }
        if (Tetris.relativeCoord[1] < 9) {
            Tetris.relativeCoord[1]++;
        }
        if (Tetris.shiftOfRelativeCoord == 1) {         //  если это движение от левой стенки
        		Tetris.isReadyForRotation = true;
        		Tetris.relativeCoord[1]--;                  
        		// возвращаем относительную координату на место (из-за исключительного положения)
        		Tetris.shiftOfRelativeCoord = 0;
        }
    }
    
    private static int[][] rotateMatrix(int[][] matrix) {
    	// поворачивает фигуру matrix по часовой стрелке
        int n = matrix.length;
        int[][] rotatedMatr = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                rotatedMatr[i][j] = matrix[n - j - 1][i];
            }
        }
        return rotatedMatr;
    }

}
