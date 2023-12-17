import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;

import static java.lang.Thread.sleep;


public class Gamerule {
    Player[] players;
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    private ImageIcon doe = new ImageIcon("./images/doe.png");
    private ImageIcon gae = new ImageIcon("./images/gae.png");
    private ImageIcon girl = new ImageIcon("./images/girl.png");
    private ImageIcon yut = new ImageIcon("./images/yut.png");
    private ImageIcon moe = new ImageIcon("./images/moe.png");
    private ImageIcon throwing = new ImageIcon("./images/throwing.png");
    int dice=-1;
    int playeridx=0;
    private Gamemap gamemap;

    public Gamerule(Gamemap gamemap) {
        this.gamemap = gamemap;
    }

    public void throwsrule() {
        Random random = new Random();
        int d1 = random.nextInt(2);
        int d2 = random.nextInt(2);
        int d3 = random.nextInt(2);
        int d4 = random.nextInt(2);
        int i=d1+d2+d3+d4;
        try{
            Music Music1=new Music();
            if(i==1){
                gamemap.setImage(doe);
                Music1.sound_run("./music/dou.wav");
            }
            else if(i==2){
                gamemap.setImage(gae);
                Music1.sound_run("./music/gae.wav");

            }
            else if(i==3){
                gamemap.setImage(girl);
                Music1.sound_run("./music/girl.wav");

            }
            else if(i==4){
                gamemap.setImage(yut);
                Music1.sound_run("./music/yut.wav");
            }
            else if(i==0){
                gamemap.setImage(moe);
                Music1.sound_run("./music/moe.wav");
            }
        }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
            System.out.println("error");
        }
        dice=i;
        turnEnd();
    }
    public void game(){
        int number = select();
        if (number == 2) {
            player1 = new Player("red");
            player2 = new Player("blue");
            players = new Player[]{player1, player2};
        } else if (number == 3) {
            player1 = new Player("red");
            player2 = new Player("blue");
            player3 = new Player("green");
            players = new Player[]{player1, player2, player3};
        } else if (number == 4) {
            player1 = new Player("red");
            player2 = new Player("blue");
            player3 = new Player("green");
            player4 = new Player("yellow");
            players = new Player[]{player1, player2, player3, player4};
        }
        gamemap.bthrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                throwsrule();
            }
        });
        playeridx=0;
        turnStart();
    }
    public void turnStart(){
        gamemap.bthrow.setVisible(true);
    }
    public void turnEnd(){
        if(dice!=-1&&dice==0||dice==4){
            if(playeridx==players.length-1){
                playeridx=0;
            }
            else{
                playeridx++;
            }
            turnStart();
        }
    }

    public int select(){
        String[] options = {"2", "3", "4"};
        int selectedValue = -1;

        while (selectedValue == -1) {
            int result = JOptionPane.showOptionDialog(
                    null,
                    "Select the player number:",
                    "Game Start",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (result == JOptionPane.CLOSED_OPTION) {
                selectedValue = -1;
            } else {
                selectedValue = result + 2;
            }
        }

        // 사용자의 선택값 반환
        return selectedValue;
    }

}



