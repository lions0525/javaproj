import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScoreBoard extends JPanel {

    private List<YutGameBoard.PlayerInfo> players;
    private int currentTurn = 0;

    public ScoreBoard(List<YutGameBoard.PlayerInfo> players) {
        this.players = players;
    }

    public void setCurrentTurn(int turn) {
        this.currentTurn = turn;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        var bounds = g.getClipBounds();
        int panelWidth  = bounds.width;
        int panelHeight = bounds.height;

        g.setColor(new Color(0x88AB8E));

        var titleFont = new Font(g.getFont().getFontName(), Font.BOLD, 40);
        var innerFont = new Font(g.getFont().getFontName(), Font.PLAIN, 40);
        g.fillRect(0, 0, g.getClipBounds().width, 110);
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString((currentTurn + 1) + "번의 턴", panelWidth / 2 - 80, 75);

        var g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int baseOffset = 110;
        int cellHeight = 110;
        for (int i = 0; i < players.size(); ++i) {
            var player = players.get(i);
            int startY = baseOffset + (cellHeight * i);

            g2d.setColor(player.color);
            g2d.fillRoundRect(20, startY + 30, 9, 56, 5, 5);
            g2d.setColor(new Color(0xCFCFCF));

            var orgStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(10, startY + cellHeight, panelWidth - 10, startY + cellHeight);
            g2d.setStroke(orgStroke);

            g.setFont(innerFont);
            g.setColor(Color.BLACK);
            g.drawString((i + 1) + "번 : " + (4 - player.getScore()) + "점", 46, startY + cellHeight - 40);
        }
    }

    public void notifyDataUpdated() {
        revalidate();
        repaint();
    }
}
