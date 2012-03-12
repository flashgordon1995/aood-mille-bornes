/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author jacky
 */
public class NewJFrame extends javax.swing.JFrame implements Runnable {
    
    private Thread t;
    private MouseClickListener mcl;

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
        t = new Thread(this);
        mcl = new MouseClickListener();
        addMouseListener(mcl);
        startThread();
    }
    
    private void startThread() {
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

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jLabel1)
                .addContainerGap(276, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jLabel1)
                .addContainerGap(176, Short.MAX_VALUE))
        );

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            while (! mcl.getClick()) { }
            System.out.println("Clicked");
            MouseDragListener blah = new MouseDragListener();
            jLabel1.addMouseListener(blah);
            jLabel1.addMouseMotionListener(blah);
            
            while(!blah.isDone()) {
                
            }
            
            System.out.println("Done");
            mcl.reset();
        }
    }
    
    private class MouseDragListener extends MouseAdapter {
        private boolean endDrag;
        private final Object lock2 = new Object();
        
        public MouseDragListener() {
            endDrag = false;
        }
        
        @Override
        public void mouseDragged(MouseEvent me) {
            me.getComponent().setBounds(me.getXOnScreen(), me.getYOnScreen(), 100, 100);
        }
        
        @Override
        public void mouseReleased(MouseEvent me) {
            synchronized(lock2) {
                endDrag = true;
            }
        }
        
        public boolean isDone() {
            synchronized(lock2) {
                return endDrag;
            }
        }
    }
    
    private class MouseClickListener extends MouseAdapter {
        private boolean clicked;
        private final Object lock = new Object();
        
        public MouseClickListener() {
            super();
            clicked = false;
        }
        
        @Override
        public void mousePressed(MouseEvent me) {
            synchronized(lock) {
                clicked = true;
            }
        }
        
        public boolean getClick() {
            synchronized(lock) {
                return clicked;
            }
        }
        
        public void reset() {
            synchronized(lock) {
                clicked = false;
            }
        }
            
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
