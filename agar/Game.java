import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JApplet;

// basic class to create and run game
public class Game extends JFrame {
    
    public Game() {
        initUI();
    }

    private void initUI() {
        
        Board b = new Board(1000,500);
        add(b);
        
        setSize(1000, 500);
        setResizable(false);

        setTitle("Agar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }   
   
    public static void main(String[] args) {
       
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame g = new Game();
                g.setVisible(true);
            } 
        });
    }
}
