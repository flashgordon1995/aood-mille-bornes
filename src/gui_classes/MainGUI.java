/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui_classes;

import baseclasses.Card;
import baseclasses.Game;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author xtian8741
 */
public final class MainGUI extends javax.swing.JFrame implements Runnable {
    
    private Thread t;
    private boolean stopFlag;
    
    private Game game;
    
    private GameComponent gameViewer;
    private MouseHandler mouseHandler;
    private LayeredComponent dragViewer;

    /**
     * Creates new form MainGUI
     */
    public MainGUI() {
        gameViewer = new GameComponent();
        dragViewer = new LayeredComponent();
        gameViewer.add(dragViewer, BorderLayout.CENTER);
        
        mouseHandler = new MouseHandler();
        game = new Game();
        game.drawAllCards();
        
        stopFlag = true;
        
        rootPane.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        rootPane.setLayout(new BorderLayout());
        rootPane.add(gameViewer, BorderLayout.CENTER);
        
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        initComponents();
        startGame();
    }
    
    private void startGame() {
        String name = JOptionPane.showInputDialog(this, "What is your name?", 
                "Name", JOptionPane.QUESTION_MESSAGE);
        game.setPlayerName(name);
        
        t = new Thread(this, "Game Loop");
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }

    @Override
    public void run() {
        while (stopFlag) {
            getUserAction(true);
            checkWin();
            makeAIMove(true);
            checkWin();
        }
    }
    
    private void getUserAction(boolean draw) {
        if (draw)
            game.drawCard(Game.HUMAN);
        
        int choice = -1;
        while (choice == -1) {
            choice = mouseHandler.getCardClick();
        }
        
        JLabel draggedCard = new JLabel(game.getCardIcon(choice));
        draggedCard.setSize(new Dimension(Card.CARD_WIDTH, Card.CARD_HEIGHT));
        dragViewer.add(draggedCard);
        Point point = mouseHandler.getDragPoint();
        
        while (!mouseHandler.getDoneDrag()) {
            point = mouseHandler.getDragPoint();
            
            draggedCard.setBounds(point.x, point.y, Card.CARD_WIDTH, Card.CARD_HEIGHT);
        }
        
        dragViewer.removeAll();
        dragViewer.repaint();
        
        if (point.y < 3 * Card.CARD_HEIGHT + 40) {
            try {
                game.makeMove(Game.HUMAN, choice);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Move!", "Error", JOptionPane.ERROR_MESSAGE);
                getUserAction(false);
            }
        } else {
            game.discard(Game.HUMAN, choice);
        }
        
        mouseHandler.resetClickPoint();
    }
    
    private void makeAIMove(boolean draw) {
        if (draw)
            game.drawCard(Game.CPU);
            
        int choice = game.generateCPUMove();
        if (choice == -1) {
            game.discard(Game.CPU, new Random().nextInt(6));
        } else {
            try {
                game.makeMove(Game.CPU, choice);
            } catch (Exception ex) {
                makeAIMove(false);
            }
        }
    }
    
    private void checkWin() {
        switch (game.isOver()) {
            case Game.HUMAN:
                JOptionPane.showMessageDialog(this, "Human Player Wins!", "Winner", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                break;
            case Game.CPU:
                JOptionPane.showMessageDialog(this, "CPU Wins!", "Loser", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                break;
        }
    }
    
    private void showDeadlyErrorMessage() {
        JOptionPane.showMessageDialog(this, "Deadly Error Encountered", "Deadly Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    
    private class GameComponent extends JComponent {
        public GameComponent() {
            super();
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        }
        
        @Override
        public void paintComponent(Graphics g) {
            Image offImage = createImage(Game.WIDTH, Game.HEIGHT);
            Graphics offGraphics = offImage.getGraphics();
            offGraphics.setColor(Game.BACKGROUND);
            offGraphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            
            game.draw(offGraphics);
            
            g.drawImage(offImage, 0, 0, null);
        }
    }
    
    private class LayeredComponent extends JComponent {
        public LayeredComponent() {
            super();
            setOpaque(false);
            setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        }
    }
    
    private class MouseHandler extends MouseAdapter {
        private Point drag;
        private int card;
        private boolean doneDrag;
        private final Object lock = new Object();
        
        public MouseHandler() {
            super();
            drag = new Point(-1, -1);
            card = -1;
            doneDrag = true;
        }
        
        @Override
        public void mouseDragged(MouseEvent me) {
            synchronized(lock) {
                drag = me.getPoint();
            }
        }
        
        @Override
        public void mousePressed(MouseEvent me) {
            synchronized(lock) {
                drag = me.getPoint();

                if (drag.y >= 4*Card.CARD_HEIGHT + 50 && drag.y <= 5*Card.CARD_HEIGHT + 50) {
                    int adjx = drag.x - 20;
                    card = adjx / (Card.CARD_WIDTH + 10);
                    if (card > 6)
                        card = -1;
                }
                
                doneDrag = false;
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent me) {
            synchronized(lock) {
                doneDrag = true;
                card = -1;
                drag = new Point(-1, -1);
            }
        }
        
        public boolean getDoneDrag() {
            synchronized(lock) {
                return doneDrag;
            }
        }
        
        public Point getDragPoint() {
            synchronized(lock) {
                return drag;
            }
        }
        
        public int getCardClick() {
            synchronized(lock) {
                return card;
            }
        }
        
        public void resetClickPoint() {
            synchronized(lock) {
                drag = new Point(-1,-1);
                card = -1;
                doneDrag = true;
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}