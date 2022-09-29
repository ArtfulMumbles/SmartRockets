import java.awt.EventQueue;
import javax.swing.JFrame;

public class SmartRockets extends JFrame {

    public SmartRockets() {

        initUI();
    }
    
    private void initUI() {
        
        add(new GamePanel());

        setResizable(false);
        pack();
        
        setTitle("SimpleRockets");    
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
    }

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new SmartRockets();
            ex.setVisible(true);
        });
    }
}