package tetris;
// Игра в классический тетрис с использованием библиотеки java.awt

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import tetris.Graphic.ControlGraphicLayer;
import tetris.Graphic.PrimaryGraphicLayer;
import tetris.Moving;


public class Tetris {
    private static PrimaryGraphicLayer primaryGraphic;    
    private static ControlGraphicLayer controlGraphic;
    static JFrame jfrm;
    static Timer gameTimer;
    static Timer fastTimer;
    static Timer lineTimer;
    static ArrayList<Integer> fullLines;
    static int gameScore = 0;                    // игровой счет
    static int mainDelay;
    static int shiftOfRelativeCoord = 0;         // смещение базовой клетки, которая указывает на начало поля основной (движущейся) фигуры (строка 83 Graphic) 
    static int[][] bigGameArray;
    static int[][] currentFigure;
    final static int[] relativeCoord = {2, 4};   // координаты базовой клетки в начале игры  (строка 83 Graphic)
    private static boolean isGameStarted = false;        // флаг основного цикла, реализующего падение основной фигуры
    static boolean isReadyForRotation = true;    // флаг, указывающий возможность поворота
    static boolean isFirstLaunch = true;         // флаг первого запуска


    private Tetris() {
        JFrame jfrm = new JFrame("Tetris");
        jfrm.setSize(400, 550);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Tetris.jfrm = jfrm;
        primaryGraphic = new Graphic.PrimaryGraphicLayer();
        jfrm.setVisible(true);
        showControls();
        jfrm.setPreferredSize(new Dimension(400, 550));
        jfrm.pack();
        processKeys();
        Tetris.fullLines = new ArrayList<Integer>();
    }

    private static void showControls() {
        controlGraphic = new Graphic.ControlGraphicLayer();
        jfrm.add(controlGraphic);
    }


	private void processKeys() {
		// обработчик нажатия клавиш
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getID() == KeyEvent.KEY_PRESSED) {
							handleKeyPress(e.getKeyCode());
						}
						return false;
					}
				});
	}

    private void handleKeyPress(int keyCode) {

        if (isGameStarted) {
            isGameStarted = false;
            switch (keyCode) {
                case 37:                     // клавиша влево
                    Moving.moveMainFigureLeft();
                    keyFunction();
                    break;
                case 38:                     // клавиша вверх
                	Moving.rotateMainFigure();
                    keyFunction();
                    break;
                case 39:                     // клавиша вправо
                	Moving.moveMainFigureRight();
                    keyFunction();
                    break;
                case 40:                     // клавиша вниз
                    nextStep();
                    keyFunction();
                    break;
            }
        }
    }

    private void keyFunction() {
        jfrm.repaint();
        isGameStarted = true;
    }


    static void clearField() {
    	// "очищает" фрэйм от панели клавиш и показывает игровую панель   
        gameScore = 0;
        jfrm.getContentPane().removeAll();
        primaryGraphic.setVisible(true);
        isGameStarted = true;
        jfrm.setLayout(new BorderLayout());
        jfrm.add(primaryGraphic);
        jfrm.revalidate();
        jfrm.repaint();
        startGame();
        isGameStarted = true;
        isFirstLaunch = false;
    }

    private static void myArrayCopy(int[][] arrS, int[][] arrD, int insStrokeNum, int insRowNum) {
    	// копирует массив arrS в массив arrD (в соответствующие коодинаты insStrokeNum и insRowNum)
        for (int i = 0; i < arrS.length; i++) {
            for (int j = 0; j < arrS[0].length; j++) {
                if (arrS[i][j] > 0) {
                    if (arrD[insStrokeNum + i][insRowNum + j] == 0) {
                        arrD[insStrokeNum + i][insRowNum + j] = arrS[i][j];
                    }
                }
            }
        }
    }

    private static void startGame() {
        bigGameArray = createBigGameArray();
        JPanel playGraphic = new Graphic.createFigure();
        jfrm.add(playGraphic);
        jfrm.revalidate();
        jfrm.repaint();
        playingTimers();
        createCurrentFigure();
        jfrm.repaint();
    }

    private static void playingTimers() {
    	// определяет все таймеры
        Timer loc_gameTimer = new Timer(mainDelay, mainFigureDownFirst);   // основной таймер для падения фигуры
        gameTimer = loc_gameTimer;
        gameTimer.start();
        fastTimer = new Timer(100, endWave);   // таймер для эффекта волны в случае проигрыша
        lineTimer = new Timer(50, lineWave);   // таймер для эффекта исчезновения ряда в случае его заполнения 
    }

    static void createCurrentFigure() {
    	//создает новую фигуру на поле
        chekForLosing();
        if (!gameTimer.isRunning()) {
            return;
        }
        int[][] loc_currentFigure = enumOfFigures.getRandomFigure();   // получаем новую фигуру 
        currentFigure = loc_currentFigure;
        relativeCoord[0] = 2;     // возвращаем стартовые координаты базовой клетки фигуры
        relativeCoord[1] = 4;
        myArrayCopy(currentFigure, bigGameArray, relativeCoord[0], relativeCoord[1]);   // вставляем фигуру на поле
        isReadyForRotation = true;
        shiftOfRelativeCoord = 0;
    }

    private static void chekForLosing() {
    	// проверка на случай проигрыша
        for (int i = 0; i < bigGameArray[0].length; ++i) {
            if (bigGameArray[4][i] == 1) {
                gameTimer.stop();
                fastTimer.start();   
                return;
            }
        }
    }

    static void theEnd() {
    	// в случае проигрыша (после финальной волны)
        isGameStarted = false;
        for (int[] matr : bigGameArray) {    // обнуляем рабочее поле
            Arrays.fill(matr, 0);
        }
        controlGraphic.removeAll();
        primaryGraphic.setVisible(false);
        controlGraphic.setVisible(false);
        showControls();                      // показываем окно выбора скорости для новой игры
        jfrm.repaint();
    }
   

    static int[][] getTempMatrix() {
    	// копирем в tempMatr действующую фигуру из рабочего поля
        int[][] tempMatr = new int[currentFigure.length][currentFigure.length];
        for (int[] matr : tempMatr) {
            Arrays.fill(matr, 0);
        }
        for (int i = 0; i < currentFigure.length; ++i) {
            for (int j = 0; j < currentFigure.length; ++j) {
                if (j + relativeCoord[1] < 10) {
                    if (bigGameArray[i + relativeCoord[0]][j + relativeCoord[1]] == 2) {
                        tempMatr[i][j] = 2;
                    }
                }
            }
        }
        return tempMatr;
    }


    private static int[][] createBigGameArray() {
    	/*создает игровое поле, где каждый элемент отражает состояние клетки
    	  0 - клетка пуста  (белая)
    	  1 - серый блок (неподвижный)
    	  2 - синий блок (управляются игровыми таймерами) 
    	 */
        int[][] emptyArray = new int[20][10];
        for (int[] matr : emptyArray) {
            Arrays.fill(matr, 0);                // изначально все поле пустое
        }
        return emptyArray;
    }

    private enum enumOfFigures {
    	// перечисление всех игровых фигур
    	 
        fig0(new int[][]{
                {0, 2, 0, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0}
        }),
        fig1(new int[][]{
                {2, 2},
                {2, 2}
        }),
        fig2(new int[][]{
                {0, 2, 0},
                {2, 2, 2},
                {0, 0, 0}
        }),
        fig3(new int[][]{
                {0, 2, 0},
                {0, 2, 0},
                {2, 2, 0},
        }),
        fig4(new int[][]{
                {0, 2, 0},
                {0, 2, 0},
                {0, 2, 2},

        }),
        fig5(new int[][]{
                {0, 0, 0},
                {2, 2, 0},
                {0, 2, 2}

        }),
        fig6(new int[][]{
                {0, 0, 0,},
                {0, 2, 2,},
                {2, 2, 0,}

        });
        private final int[][] figureNum;

        enumOfFigures(int[][] p) {
            figureNum = p;
        }

        private int[][] getFigure() {
            return figureNum;
        }

        public static int[][] getRandomFigure() {
        	// возвращает случайную фигуру из перечисления
            int randomEnumNumber = (int) (Math.random() * values().length);
            return values()[randomEnumNumber].getFigure();
        }
    }

    private static void nextStep() {
        //  запускается основным таймером игры
        if (gameTimer.isRunning()) {
            for (int i = bigGameArray.length - 1; i > 0; i--) {
                for (int j = 0; j < bigGameArray[0].length; j++) {
                    if (bigGameArray[i][j] == 2) {
                        if ((i + 1) > 18) {                                 // если синий блок достиг дна 
                            nextFigure();
                            return;

                        } else if (bigGameArray[i + 1][j] == 1) {              // если под синим блоком находится серый
                            nextFigure();
                            return;
                        }
                    }
                }
            }
            shiftDown();
        }
    }

    private static void shiftDown() {
    	// опускает все синие блоки на 1 уровень вниз 
        for (int i = bigGameArray.length - 1; i > 0; i--) {
            for (int j = 0; j < bigGameArray[0].length; j++) {
                if (bigGameArray[i][j] == 2 & i < 18) {
                    bigGameArray[i][j] = 0;
                    bigGameArray[i + 1][j] = 2;
                }
            }
        }
        relativeCoord[0]++;
    }

    private static void nextFigure() {
    	// начинает подготовку к появлению новой фигуры 
        for (int i = 0; i < bigGameArray.length; i++) {
            for (int j = 0; j < bigGameArray[0].length; j++) {
                if (bigGameArray[i][j] == 2) {                    // заменяем все синие блоки серыми, т.к. рабочая фигура остановилась
                    bigGameArray[i][j] = 1;
                }
            }
        }
        Lines.chekingLines();
        createCurrentFigure();
    }

    private final static ActionListener mainFigureDownFirst = new ActionListener() {    
    	// основной таймер для падения фигуры
        public void actionPerformed(ActionEvent evt) {
            nextStep();
            jfrm.repaint();
        }
    };

    private final static ActionListener endWave = new ActionListener() {     
    	// таймер для эффекта волны в случае проигрыша
        public void actionPerformed(ActionEvent evt) {
            Lines.cleaningWave();
            jfrm.repaint();
        }
    };

    private final static ActionListener lineWave = new ActionListener() {    
    	// таймер для эффекта исчезновения ряда в случае его заполнения 
        public void actionPerformed(ActionEvent evt) {
            Lines.animatatedDelete();
            jfrm.repaint();
        }
    };

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Tetris();
            }
        });
    }
}