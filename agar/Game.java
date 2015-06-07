import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Game extends JFrame {
    
    boolean over;

    public Game() {
        over = false;
        initUI();
    }

    private void initUI() {
        
        Board b = new Board(1000,500);
        add(b);
        
        over = b.gameOver();

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
