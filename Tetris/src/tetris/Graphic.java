package tetris;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

import tetris.Tetris;

public class Graphic {


static class ControlGraphicLayer extends JPanel {
	private static final long serialVersionUID = 1L;

	// панель выбора скорости игры с указанием клавиш управления
    public void paint(Graphics g) {
        Graphics2D graph2D2 = (Graphics2D) g;
        graph2D2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(72, 72, 72));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        graph2D2.drawString("Use the arrow keys to play", 100, 50);
        g.setFont(new Font("Arial Unicode MS", Font.BOLD, 56));
        // данный шрифт содержит символы юникода, соответствующие клавишам со стрелками 
        graph2D2.drawString("\u2350", 175, 130);
        graph2D2.drawString("\u2347", 135, 168);
        graph2D2.drawString("\u2348", 215, 168);
        graph2D2.drawString("\u2357", 175, 168);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        if (Tetris.isFirstLaunch) {
            graph2D2.drawString("Select the speed", 130, 240);
        } else {
            graph2D2.drawString("You lost. Play again?", 120, 240);
        }
        showSpeedButtons();
    }

    private void showSpeedButtons() {
        JButton buttonEasy = new JButton("Easy");
        JButton buttonNormal = new JButton("Normal");
        JButton buttonHard = new JButton("Hard");
        buttonEasy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	Tetris.mainDelay = 500;
                Tetris.clearField();
            }
        });
        buttonNormal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	Tetris.mainDelay = 250;
            	Tetris.clearField();
            }
        });
        buttonHard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	Tetris.mainDelay = 150;
            	Tetris.clearField();
            }
        });
        buttonEasy.setBounds(150, 270, 85, 30);
        buttonNormal.setBounds(150, 320, 85, 30);
        buttonHard.setBounds(150, 370, 85, 30);
        Tetris.jfrm.setLayout(null);
        Tetris.jfrm.add(buttonEasy);
        Tetris.jfrm.add(buttonNormal);
        Tetris.jfrm.add(buttonHard);
        Tetris.jfrm.revalidate();
        buttonEasy.updateUI();
        buttonNormal.updateUI();
        buttonHard.updateUI();
    }
}


static class createFigure extends JPanel {
	private static final long serialVersionUID = 1L;

	//  отображает все блоки и игровой счет
    public void paint(Graphics g) {
        Graphics2D figure = (Graphics2D) g;
        figure.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        figure.setStroke(new BasicStroke(2.0F));
        g.setFont(new Font("Arial", Font.BOLD, 15));
        figure.drawString("Score", 325, 240);
        figure.drawString(String.valueOf(Tetris.gameScore), 340, 260);    // отображаем счет
        
        //для отображения базовой клетки  раскомментируйте  следующие строки 
        /*
        RoundRectangle2D lowerRectangle3 = new RoundRectangle2D.Double(101+18*relativeCoord[1], 70 + 18*relativeCoord[0], 16.0, 16.0, 1.0, 1.0);
        figure.setColor(new Color(200, 95, 88));
        figure.fill(lowerRectangle3);
        figure.setColor(new Color(72, 72, 72));
        figure.draw(lowerRectangle3);
        */

        int i, j;
        for (i = 0; i < Tetris.bigGameArray.length; i++) {
            for (j = 0; j < Tetris.bigGameArray[0].length; j++) {
                if (Tetris.bigGameArray[i][j] == 1) {    
                	// отображает серые блоки
                    RoundRectangle2D lowerRectangle2 = new RoundRectangle2D.Double(101 + 18 * j, 70 + 18 * i, 16.0, 16.0, 1.0, 1.0);
                    figure.setColor(new Color(200, 200, 200));
                    figure.fill(lowerRectangle2);
                    figure.setColor(new Color(72, 72, 72));
                    figure.draw(lowerRectangle2);
                }
                if (Tetris.bigGameArray[i][j] == 2) {   
                	// отображает синие блоки
                    RoundRectangle2D lowerRectangleMoving = new RoundRectangle2D.Double(101 + 18 * j, 70 + 18 * i, 16.0, 16.0, 1.0, 1.0);
                    figure.setColor(new Color(102, 97, 246));
                    figure.fill(lowerRectangleMoving);
                    figure.setColor(new Color(72, 72, 72));
                    figure.draw(lowerRectangleMoving);
                }
            }
        }
    }
}

static class PrimaryGraphicLayer extends JPanel {
	private static final long serialVersionUID = 1L;

	// основная игровая панель 
    public void paint(Graphics g) {
        Graphics2D graph2D = (Graphics2D) g;
        graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph2D.setStroke(new BasicStroke(20.0F));
        g.setColor(new Color(159, 186, 144));
        RoundRectangle2D lowerRectangle = new RoundRectangle2D.Double(90.0, 30.0, 200.5, 391.0, 1.0, 1.0);
        // рисует прямоугольник, внутри которого происходит игра
        graph2D.draw(lowerRectangle);
    }
}
}
