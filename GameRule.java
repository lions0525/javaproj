import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class GameRule {

    private ImageIcon doe = new ImageIcon("./images/doe.png");
    private ImageIcon gae = new ImageIcon("./images/gae.png");
    private ImageIcon girl = new ImageIcon("./images/girl.png");
    private ImageIcon yut = new ImageIcon("./images/yut.png");
    private ImageIcon moe = new ImageIcon("./images/moe.png");
    private ImageIcon throwing = new ImageIcon("./images/throwing.png");

    ImageIcon[] images = {doe, gae, girl, yut, moe};
    String[] musics = {"dou", "gae", "girl", "yut", "moe"};

    private Gamemap gamemap;
    int dice = -1;
    int playeridx = 0;
    List<Integer> yutlist = new ArrayList<>(4);
    Music gameEffect = new Music();

    List<YutGameBoard.PlayerInfo> players = null;

    public GameRule(Gamemap gamemap) {
        this.gamemap = gamemap;
    }


    public void throwsrule() {
        Random random = new Random();

        int randNum;
        do {
            randNum = getRandomYut();
            try {
                var image = images[randNum];
                var music = "./music/" + musics[randNum] + ".wav";

                gamemap.setImage(image);
                gameEffect.playMusic(music, false);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
                System.out.println("error");
            }
            dice = randNum;
            var image = images[randNum];
            gamemap.addImage(image);
            yutlist.add(randNum);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (randNum == 3 || randNum == 4);

        move();
    }

    public List<YutGameBoard.PlayerInfo> askAndCreateUsers() {
        int number = select() + 2;

        int colors[] = {0xFF8080, 0x79AC78, 0xBEADFA, 0xEBEF95};
        players = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            var color = colors[i];
            players.add(YutGameBoard.PlayerInfo.withName(new Color(color)));
        }

        return players;
    }

    public void game() {
        gamemap.bthrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                throwsrule();
            }
        });
        playeridx = 0;
        turnStart();
    }

    public void turnStart() {
        gamemap.bthrow.setVisible(true);
    }

    public void turnEnd() {

        checkEnd(players.get(playeridx));
        playeridx = playeridx == players.size() - 1 ? 0 : playeridx + 1;
        try {
            gameEffect.playMusic("./music/nextTurn.wav", false);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        gamemap.recordYut.removeAll();
        yutlist.clear();
        gamemap.scoreBoard.setCurrentTurn(playeridx);
        gamemap.scoreBoard.notifyDataUpdated();
        turnStart();
    }

    public void move() {
        gamemap.bthrow.setVisible(false);
        var nowPlayer = players.get(playeridx);

        List<String> availableOptions = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            if (nowPlayer.horses[i] != null)
                availableOptions.add(String.valueOf(i + 1));
        }
        String[] horsesOption = availableOptions.toArray(String[]::new);

        var completed = new CompletableFuture<Void>();
        completed.thenAccept((v) -> turnEnd());
        for (int n = 0; n < yutlist.size(); ++n) {
            int moveNums = yutlist.get(n);
            int selectedIdx = selectOption(
                    "select horse number",
                    "select horse number",
                    horsesOption,
                    true
            );
            int actualIndex = Integer.parseInt(horsesOption[selectedIdx]) - 1;
            var selectedHorse = nowPlayer.getHorses()[actualIndex];
            if (selectedHorse.currentSlot.idx == -1)
                selectedHorse.moveFront();

            new Thread(new PlayerMoveRunnable(n) {
                @Override
                public void run() {
                    int path = 0;
                    if (selectedHorse.currentSlot.getMoveOptions().contains(YutGameBoard.MoveOption.TURN)) {
                        String[] option = {"front","turn"};
                        path = selectOption("choose your path", "choose your path", option, true);
                    }

                    YutGameBoard.Horse savedHorse = null;
                    for (int i = 0; i <= moveNums; ++i) {
                        if (savedHorse != null) {
                            selectedHorse.currentSlot.settledHorse = savedHorse;
                            savedHorse = null;
                        }

                        YutGameBoard.MoveOption option = YutGameBoard.MoveOption.FRONT;
                        if (path == 1) {
                            option = YutGameBoard.MoveOption.TURN;
                            path = 0;
                        }

                        var nextSlot = selectedHorse.getNextSlot(option);
                        if (nextSlot == null) {
                            selectedHorse.currentSlot.settledHorse = null;
                            nowPlayer.setScore(nowPlayer.score += selectedHorse.stack);
                            nowPlayer.removeHorseOfId(selectedHorse.id);
                            gamemap.scoreBoard.notifyDataUpdated();
                            completed.complete(null);
                            //
                            return;
                        }

                        if (nextSlot.settledHorse != null)
                            savedHorse = nextSlot.settledHorse;

                        selectedHorse.move(option);
                        gamemap.yutGameBoard.notifyUpdated();
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //when eat our team
                    if (savedHorse != null) {
                        if (savedHorse.playerId == nowPlayer.id) {
                            selectedHorse.stack++;
                            savedHorse.currentSlot = null;
                            savedHorse.prevSlot = null;
                            nowPlayer.removeHorseOfId(savedHorse.id);
                            gamemap.yutGameBoard.notifyUpdated();
                        } else {
                            savedHorse.currentSlot = gamemap.yutGameBoard.defaultSlot;
                            savedHorse = null;
                        }
                    }

                    if (n == yutlist.size() - 1)
                        completed.complete(null);
                }
            }).start();
        }
    }

    public void checkEnd(YutGameBoard.PlayerInfo nowPlayer) {
        if (nowPlayer.score == 4) {
            voiceEnd(nowPlayer);
            JOptionPane.showOptionDialog(
                    null,
                    (nowPlayer.id+1)+"번이 이겼습니다.",
                    "Game end",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null, null, null
            );
            System.exit(0);
        }
    }

    public void voiceEnd(YutGameBoard.PlayerInfo nowPlayer) {
        var music = "./music/"+(nowPlayer.id + 1) + "player_win.wav";
        try {
            gameEffect.playMusic(music, false);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("error");
        }

    }

    public int select() {
        String[] options = {"2", "3", "4"};
        // 사용자의 선택값 반환
        return selectOption(
                "Select the player number:",
                "Game Start",
                options,
                true
        );
    }

    public int selectOption(String title, String message, String[] options, boolean requireSelection) {
        int selectedValue = -1;

        while (selectedValue == -1 && requireSelection) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    title,
                    message,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (result != JOptionPane.CLOSED_OPTION)
                selectedValue = result;
        }

        // 사용자의 선택값 반환
        return selectedValue;
    }

    private int getRandomYut() {
        Random random = new Random();
        int n = 0;
        for (int i = 0; i < 4; ++i)
            n += random.nextInt(2);
        return n;
    }

    private abstract class PlayerMoveRunnable implements Runnable {
        protected int n;

        public PlayerMoveRunnable(int n) {
            this.n = n;
        }
    }

}