import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class YutGameBoard extends JPanel {

    //mid vertex idx
    public static final int MID_SLOT_IDX = 22;
    private static final int ROW_COUNT  = 6;
    private static final int DIAG_COUNT = 7;
    private static final int SLOT_RAD   = 60;
    private static final int VERT_RAD   = 70;

    private final List<PlayerInfo> players;

    private final List<BoardSlot> allSlots = new ArrayList<>();
    private BoardSlot midSlot;

    public BoardSlot defaultSlot;

    public YutGameBoard(int size, List<PlayerInfo> players) {
        setSize(size, size);
        setLayout(null);
        setOpaque(false);
        this.players = players;

        //gap calculation
        final int width = size;
        final int cellGap = width / ROW_COUNT;
        int diagGap = width / DIAG_COUNT;
        int offset = 50;
        
        //now start position
        var nowPos = coord(width - cellGap + offset, width - cellGap + offset);
        final var startSlot = new BoardSlot(0, Color.ORANGE, nowPos.clone(), VERT_RAD);
        allSlots.add(startSlot);

        defaultSlot = new BoardSlot(-1, Color.GRAY, coord(0, 0), 0);
        defaultSlot.front = startSlot;

        for (var player : players)
            for (var horse : player.horses)
                horse.currentSlot = defaultSlot;

        var prevSlot = startSlot;
        // Draw base board-->connect with front slot
        // Pseudo-LinkedList
        for (int n = 0; n < 4; ++n) {
            for (int i = 1; i < (n == 3 ? ROW_COUNT - 1 : ROW_COUNT); ++i) {
                moveCoord(nowPos, n, cellGap);
                int rad = i == ROW_COUNT - 1 ? VERT_RAD : SLOT_RAD;
                Color color = i == ROW_COUNT - 1 ? Color.DARK_GRAY : Color.GRAY;
                var nSlot = new BoardSlot(allSlots.size(), color, nowPos.clone(), rad);
                prevSlot.front = nSlot;

                allSlots.add(nSlot);
                prevSlot = nSlot;
            }
        }
        //prevSlot.front = startSlot;

        // Draw diag,connect with side slot
        prevSlot = null;
        for (int j = 1; j <= DIAG_COUNT - 2; ++j) {
            int pos = j * diagGap + 45;
            var nSlot = new BoardSlot(allSlots.size(), Color.GRAY, coord(pos, pos), 40);
            if (j == 3)
                midSlot = nSlot;
            if (prevSlot != null)
                prevSlot.front = nSlot;
            else
                allSlots.get(10).turn = nSlot;
            allSlots.add(nSlot);
            prevSlot = nSlot;
        }
        //prevSlot.front = allSlots.get(0);

        //midslot option
        midSlot.fillColor = Color.DARK_GRAY;
        midSlot.rad = VERT_RAD;

        prevSlot = null;
        for (int j = 1; j <= DIAG_COUNT - 2; ++j) {
            if (j == 3) {
                prevSlot.front = midSlot;
                prevSlot = midSlot;
                continue;
            }
            int pos = j * diagGap + 45;
            var nSlot = new BoardSlot(allSlots.size(), Color.GRAY, coord(width - pos - (SLOT_RAD / 2), pos), 40);
            if (prevSlot == null)
                allSlots.get(5).turn = nSlot;
            else if (j == 4)
                prevSlot.turn = nSlot;
            else
                prevSlot.front = nSlot;
            allSlots.add(nSlot);
            prevSlot = nSlot;
        }
        prevSlot.front = allSlots.get(15);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //antialiasing
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (var slot : allSlots) {
            slot.render(g);
        }
    }



    //board update
    public void notifyUpdated() {
        revalidate();
        repaint();
    }

    private Coord coord(int x, int y) {
        return new Coord(x, y);
    }

    private void moveCoord(Coord coord, int dir, int dist) {
        // 0 -> top, 1 -> left, 2 -> bottom, 3 -> right
        switch (dir) {
            case 0 -> coord.y -= dist;
            case 1 -> coord.x -= dist;
            case 2 -> coord.y += dist;
            case 3 -> coord.x += dist;
        }
    }

    public static class BoardSlot {
        final int idx;
        final Coord coord;
        Color fillColor;
        int rad;
        BoardSlot turn = null;
        BoardSlot front = null;
        Horse settledHorse = null;

        public BoardSlot(int idx, Color fillColor, Coord coord, int rad) {
            this.idx = idx;
            this.fillColor = fillColor;
            this.coord = coord;
            this.rad = rad;
        }

        //map drawing
        public void render(Graphics g) {
            int mid = rad / 2;
            if (hasHorse()) {
                g.setColor(settledHorse.color);
                g.fillOval(coord.x - mid, coord.y - mid, rad, rad);

                g.setColor(Color.BLACK);
                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 16));
                g.drawString(String.valueOf(settledHorse.idx + 1), coord.x + 15, coord.y - 22);

                //if horses are stacked write number
                if (settledHorse.stack > 1) {
                    g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 30));
                    g.drawString(String.valueOf(settledHorse.stack), coord.x - 8, coord.y + 10);
                }
            } else {
                g.setColor(fillColor);
                g.fillOval(coord.x - mid, coord.y - mid, rad, rad);
            }
        }

        //check turn
        public List<MoveOption> getMoveOptions() {
            var options = new ArrayList<MoveOption>();
            if (turn != null)
                options.add(MoveOption.TURN);
            if (front != null)
                options.add(MoveOption.FRONT);

            return options;
        }

        public boolean hasHorse() {
            return this.settledHorse != null;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof BoardSlot && ((BoardSlot) obj).idx == this.idx;
        }
    }

    public enum MoveOption {
        FRONT,
        TURN
    }

    public static class PlayerInfo {
        int id, score;
        Color color;
        Horse[] horses;

        public PlayerInfo(int id, int score, Color color, Horse[] horses) {
            this.id = id;
            this.score = score;
            this.color = color;
            this.horses = horses;
        }

        private static final AtomicInteger lastId = new AtomicInteger(0);

        public static PlayerInfo withName(Color color) {
            var nPlInfo = new PlayerInfo(lastId.getAndIncrement(),0, color, new Horse[4]);
            nPlInfo.initHorses();
            return nPlInfo;
        }
        
        public Horse[] getHorses() {
            return this.horses;
        }

        public int getScore() {
            return this.score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void removeHorseOfId(int horseId) {
            for (int i = 0; i < 4; ++i) {
                var nHorse = horses[i];
                if (nHorse != null && nHorse.id == horseId) {
                    horses[i] = null;
                    return;
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PlayerInfo && ((PlayerInfo) obj).id == this.id;
        }

        private void initHorses() {
            for (int i = 0; i < 4; ++i)
                horses[i] = Horse.ofPlayer(this, i);
        }
    }

    public static class Horse {
        int id, idx, playerId, stack;
        Color color;
        BoardSlot currentSlot = null;
        BoardSlot prevSlot = null;

        public Horse(
                int id,
                int idx,
                int playerId,
                int stack,
                Color color
        ) {
            this.id = id;
            this.idx = idx;
            this.playerId = playerId;
            this.stack = stack;
            this.color = color;
        }

        private static final AtomicInteger lastId = new AtomicInteger(0);

        //create horse object
        public static Horse ofPlayer(PlayerInfo player, int idx) {
            return new Horse(lastId.getAndIncrement(), idx, player.id, 1, player.color);
        }

        public BoardSlot getNextSlot(MoveOption option) {
            if (currentSlot.idx == MID_SLOT_IDX && prevSlot.idx != MID_SLOT_IDX - 1) {
                if (option == MoveOption.FRONT)
                    option = MoveOption.TURN;
                else
                    option = MoveOption.FRONT;
            }

            BoardSlot nextSlot = null;
            switch (option) {
                case TURN ->
                        nextSlot = currentSlot.turn;
                case FRONT ->
                        nextSlot = currentSlot.front;
            }

            return nextSlot;
        }

        public void move(MoveOption option) {
            var available = currentSlot.getMoveOptions();
            if (!available.contains(option))
                throw new IllegalArgumentException("Unable to move " + option.name());

            //midSlot turn&front option
            prevSlot = currentSlot;
            if (currentSlot.settledHorse != null && currentSlot.settledHorse.equals(this))
                currentSlot.settledHorse = null;

            currentSlot = getNextSlot(option);
            currentSlot.settledHorse = this;
        }

        public void moveFront() {
            move(MoveOption.FRONT);
        }

        public void moveTurn() {
            move(MoveOption.TURN);
        }

        private BoardSlot getCurrentSlot() {
            return this.currentSlot;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Horse && ((Horse) obj).id == this.id;
        }
    }

    public static class Coord {
        int x, y;

        //coordinate specifying
        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        //recent coordinate return
        public Coord clone() {
            return new Coord(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Coord && ((Coord) obj).getX() == this.x && ((Coord) obj).getY() == this.y;
        }
    }
}