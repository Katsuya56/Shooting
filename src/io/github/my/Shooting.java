package io.github.my;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Shooting {
    public static ShootingFrame shootingFrame;
    public static boolean loop;

    public static void main(String[] args) {
        shootingFrame = new ShootingFrame();
        loop = true;

        Graphics gra = shootingFrame.panel.image.createGraphics();

        long startTime;
        long fpsTime = 0;
        int fps = 30;
        int FPS = 0;
        int FPSCount = 0;

        EnumShootingScreen screen = EnumShootingScreen.START;

//        GAME
        int player_x = 0, player_y = 0;
        ArrayList<Bullet> bullets;
        ArrayList<Enemy> enemies;

        while (loop) {
            if (System.currentTimeMillis() - fpsTime >= 1000) {
                fpsTime = System.currentTimeMillis();
                FPS = FPSCount;
                FPSCount = 0;
//                System.out.println(FPS);
            }
            FPSCount++;
            startTime = System.currentTimeMillis();

            gra.setColor(Color.WHITE);
            gra.fillRect(0, 0, 500, 500);

            switch (screen) {
                case START:
                    gra.setColor(Color.BLACK);

                    Font font = new Font("SansSerif", Font.PLAIN, 50);
                    gra.setFont(font);
                    FontMetrics metrics = gra.getFontMetrics(font);
                    gra.drawString("Shooting", 250 - metrics.stringWidth("Shooting") / 2, 100);

                    font = new Font("SansSerif", Font.PLAIN, 20);
                    gra.setFont(font);
                    metrics = gra.getFontMetrics(font);
                    gra.drawString("Press SPACE to Start!", 250 - metrics.stringWidth("Press SPACE to Start!") / 2, 150);
                    if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
                        screen = EnumShootingScreen.GAME;
                        bullets = new ArrayList<>();
                        enemies = new ArrayList<>();
                        player_x = 250;
                        player_y = 400;
                    }
                    break;
                case GAME:
                    int MIGRATION_LENGTH = 3;
                    gra.setColor(Color.BLUE);
                    gra.fillRect(player_x + 10, player_y, 10, 10);
                    gra.fillRect(player_x, player_y + 10, 30, 10);
                    if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
                        player_x += MIGRATION_LENGTH;
                    } else if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
                        player_x -= MIGRATION_LENGTH;
                    }

                    if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
                        player_y += MIGRATION_LENGTH;
                    } else if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
                        player_y -= MIGRATION_LENGTH;
                    }
                    break;
                case GAME_OVER:
                    break;

            }

            gra.setColor(Color.BLACK);
            gra.setFont(new Font("SansSerif", Font.PLAIN, 10));
            gra.drawString(FPS + "FPS", 0, 460);
            shootingFrame.panel.draw();

            try {
                long runTime = System.currentTimeMillis() - startTime;
                if (runTime < (1000 / fps)) {
                    Thread.sleep(1000 / fps - runTime);

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
