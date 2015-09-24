package tetris;

import java.util.ArrayList;

public class Lines {

	static void destroyLine() {
		// уничтожает все строки из fullLines    	
	    for (int i = 0; i < Tetris.fullLines.size(); i++) {
	        int h = Tetris.fullLines.get(i);
	        for (int j = 0; j < Tetris.bigGameArray[0].length; j++) {
	            if (Tetris.bigGameArray[h][j] != 0 & (h + Tetris.fullLines.size() < 20)) {         // если клетка не пустая, делаем ее пустой
	                Tetris.bigGameArray[h][j] = 0;
	            }
	        }
	    }
	
	    for (int i = 0; i < Tetris.fullLines.size(); i++) {                 
	    	// переносим все строки над уничтоженными строками на 1 уровень вниз, начиная с самой нижней
	        int h = Tetris.fullLines.get(i);
	        for (int j = 0; j < Tetris.bigGameArray[0].length; j++) {
	            for (int q = h; q > 0; q--) {
	                if (Tetris.bigGameArray[q][j] != 0 & q < 19) {
	                    Tetris.bigGameArray[q][j] = 0;
	                    Tetris.bigGameArray[q + 1][j] = 1;
	                }
	            }
	        }
	    }
	    for (int i = 0; i < Tetris.bigGameArray[0].length; i++) {
	        Tetris.bigGameArray[19][i] = 0;
	    }
	
	    Tetris.gameTimer.start();
	    Tetris.createCurrentFigure();
	}

	static void cleaningWave() {
		// реализует эффект заполнения игрового поля серыми блоками в случае проигрыша
	    for (int j = 0; j < Tetris.bigGameArray[0].length; j++) {
	        if (Tetris.bigGameArray[0][j] != 1) {
	            for (int i = 0; i < Tetris.bigGameArray.length; i++) {
	                Tetris.bigGameArray[i][j] = 1;
	            }
	            return;
	        }
	        if (j > 8) {
	            Tetris.fastTimer.stop();
	            try {
	                Thread.sleep(300);
	            } catch (InterruptedException ex) {
	                Thread.currentThread().interrupt();
	            }
	            Tetris.theEnd();
	        }
	    }
	}

	static void animatatedDelete() {
		// за каждый запуск закрашивает по 1 блоку слева в каждом уничтожаемом ряду в синий цвет 
	    int firstFullStr = Tetris.fullLines.get(0);
	    if (Tetris.bigGameArray[firstFullStr][9] != 2) {                     // если крайний правый блок в 0 ряду не синий
	        for (int j = 0; j < Tetris.bigGameArray[0].length; j++) {
	            int temp = 0;                                         // счетчик обработанных рядов
	            for (int i = 0; i < Tetris.fullLines.size(); i++) {
	                int h = Tetris.fullLines.get(i);
	                if (Tetris.bigGameArray[h][j] == 1) {                    //если блок в ряду серый, закрашиваем его синим
	                    Tetris.bigGameArray[h][j] = 2;
	                    temp++;
	                    if (temp == Tetris.fullLines.size()) {
	                        return;
	                    }
	                }
	            }
	        }
	    } else {                                // если все блоки закрашены синим
	        Tetris.lineTimer.stop(); 
	        destroyLine();
	        Tetris.fullLines.clear();
	    }
	}

	static void deleteLines(ArrayList<Integer> fullLines) {
		// запускает визуальный эффект уничтожения всех рядов в списке fullLines
	    Tetris.gameScore += fullLines.size() * fullLines.size() * 10;       // вычисляем новое значение игрового счета
	    Tetris.gameTimer.stop();
	    if (!Tetris.lineTimer.isRunning()) { 
	        Tetris.lineTimer.start();                  // запускает таймер для закрашивания синим цветом уничтожаемых рядов (animatatedDelete())
	    }
	}

	static void chekingLines() {
		// проверяет, есть ли заполненные ряды для последующего уничтожения
	    for (int i = 0; i < Tetris.bigGameArray.length; i++) {
	        int fullCellCounter = 0;
	        for (int j = 0; j < Tetris.bigGameArray[0].length; j++) {
	            if (Tetris.bigGameArray[i][j] != 1) {
	                break;                                            
	            } else {
	                fullCellCounter++;
	            }
	        }
	        if (fullCellCounter >= 10) {                              // если в ряду все блоки серые
	            Tetris.fullLines.add(i);                                     // добавляем номер ряда в список для уничтожения
	        }
	    }
	    if (!Tetris.fullLines.isEmpty()) {
	        deleteLines(Tetris.fullLines);
	    }
	}

}
