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
import javax.swing.JComponent;
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

    /**
     * Creates new form MainGUI
     */
    public MainGUI() {
        gameViewer = new GameComponent();
        gameViewer.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        
        mouseHandler = new MouseHandler();
        game = new Game();
        game.drawAllCards();
        
        stopFlag = true;
        
        this.add(gameViewer, BorderLayout.CENTER);
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

            public void run() {
                new MainGUI().setVisible(true);
            }
        });
    }

    @Override
    public void run() {
        while (stopFlag) {
            getUserAction();
            makeAIMove();
        }
    }
    
    private void getUserAction() {
//        try {
//            game.drawCard(Game.HUMAN);
//        } catch (Exception ex) {
//            showDeadlyErrorMessage();
//        }
        
        int choice = -1;
        while (choice == -1) {
            choice = mouseHandler.getCardClick();
        }
        
        while (!mouseHandler.getDoneDrag()) {
            Point point = mouseHandler.getDragPoint();
            
            
        }
        
        repaint();
        
        //@TODO: process the action
        
        mouseHandler.resetClickPoint();
    }
    
    private void makeAIMove() {
        
    }
    
    private void showDeadlyErrorMessage() {
        JOptionPane.showMessageDialog(this, "Deadly Error Encountered", "Deadly Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private class GameComponent extends JComponent {
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
    
    private class MouseHandler extends MouseAdapter {
        private Point drag = new Point(-1, -1);
        private int card = -1;
        private boolean doneDrag = true;
        private final Object lock = new Object();
        
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

                if (drag.y > 4*Card.CARD_HEIGHT + 50 && drag.y < 5*Card.CARD_HEIGHT + 50) {
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
