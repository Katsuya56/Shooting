package io.github.my;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

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

        // GAME
        int player_x = 0, player_y = 0;
        int bullet_interval = 5;
        int score = 0;
        int life = 0;
        int level = 0;
        long level_timer = 0;

        ArrayList<Bullet> bullets_player = new ArrayList<>();
        ArrayList<Bullet> bullets_enemy = new ArrayList<>();
        ArrayList<Enemy> enemies = new ArrayList<>();
        Random random = new Random();

        while (loop) {
            if (System.currentTimeMillis() - fpsTime >= 1000) {
                fpsTime = System.currentTimeMillis();
                FPS = FPSCount;
                FPSCount = 0;
                // System.out.println(FPS);
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
                        bullets_player = new ArrayList<>();
                        bullets_enemy = new ArrayList<>();
                        enemies = new ArrayList<>();
                        life = 5;
                        score = 0;
                        player_x = 250;
                        player_y = 400;
                        level = 1;
                    }
                    break;

                case GAME:
                    int MIGRATION_LENGTH = 5;
                    if (System.currentTimeMillis() - level_timer > 10 * 1000) {
                        level_timer = System.currentTimeMillis();
                        level++;
                    }

                    // プレイヤーを表示
                    gra.setColor(Color.BLUE);
                    gra.fillRect(player_x + 10, player_y - 10, 10, 10);
                    gra.fillRect(player_x, player_y, 30, 10);
                    // プレイヤーの弾丸を表示
                    for (int i = 0; i < bullets_player.size(); i++) {
                        Bullet bullet = bullets_player.get(i);
                        gra.setColor(Color.BLUE);
                        gra.fillRect(bullet.x, bullet.y, 5, 5);
                        bullet.y -= 10;
                        if (bullet.y < 0) {
                            bullets_player.remove(i);
                            i--;
                        }

                        for (int j = 0; j < enemies.size(); j++) {
                            Enemy enemy = enemies.get(j);
                            if (bullet.x >= enemy.x && bullet.x <= enemy.x + 30 && bullet.y >= enemy.y
                                    && bullet.y <= enemy.y + 20) {
                                enemies.remove(j);
                                j--;
                                score += 10;
                            }
                        }
                    }

                    // エネミーを生成
                    if (random.nextInt(level < 30 ? 30 - level : 30) == 1) {
                        enemies.add(new Enemy(random.nextInt(450), 0));
                    }

                    // エネミーを表示
                    gra.setColor(Color.RED);
                    for (int i = 0; i < enemies.size(); i++) {
                        Enemy enemy = enemies.get(i);
                        gra.fillRect(enemy.x, enemy.y, 30, 10);
                        gra.fillRect(enemy.x + 10, enemy.y + 10, 10, 10);
                        enemy.y += 1;
                        if (enemy.y > 500) {
                            enemies.remove(i);
                            i--;
                        }
                        if (random.nextInt(50) == 1) {
                            bullets_enemy.add(new Bullet(enemy.x + 10, enemy.y));
                        }
                        if ((enemy.x >= player_x && enemy.x <= player_x + 30 && enemy.y >= player_y && enemy.y <= player_y + 20) ||
                                (enemy.x + 30 >= player_x && enemy.x + 30 <= player_x + 30 && enemy.y + 20 >= player_y && enemy.y + 20 <= player_y + 20)) {
                            life -= 1;
                            if (life == 0) {
                                screen = EnumShootingScreen.GAME_OVER;
                            } else {
                                enemies.remove(i);
                                i--;
                            }
                        }
                    }

                    // エネミーの弾丸を表示
                    for (int i = 0; i < bullets_enemy.size(); i++) {
                        Bullet bullet = bullets_enemy.get(i);
                        gra.setColor(Color.RED);
                        gra.fillRect(bullet.x, bullet.y, 5, 5);
                        bullet.y += 15 + Math.round(level / 10.0);
                        if (bullet.y < 0) {
                            bullets_enemy.remove(i);
                            i--;
                        }
                        if (bullet.x >= player_x && bullet.x <= player_x + 30 && bullet.y >= player_y
                                && bullet.y <= player_y + 20) {
                            life -= 1;
                            if (life == 0) {
                                screen = EnumShootingScreen.GAME_OVER;
                                score += level * 100;
                            } else {
                                bullets_enemy.remove(i);
                                i--;
                            }
                        }

                    }

                    // キー操作
                    if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT) && player_x < 455) {
                        player_x += MIGRATION_LENGTH;
                    } else if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT) && player_x > 0) {
                        player_x -= MIGRATION_LENGTH;
                    }

                    if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN) && player_y < 440) {
                        player_y += MIGRATION_LENGTH;
                    } else if (Keyboard.isKeyPressed(KeyEvent.VK_UP) && player_y > 30) {
                        player_y -= MIGRATION_LENGTH;
                    }

                    if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE) && bullet_interval == 0) {
                        bullets_player.add(new Bullet(player_x + 12, player_y - 10));
                        bullet_interval = 5;
                    }

                    if (Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                        screen = EnumShootingScreen.START;
                    }

                    if (bullet_interval > 0) {
                        bullet_interval--;
                    }

                    // テキスト表示
                    gra.setColor(Color.BLACK);
                    font = new Font("SansSerif", Font.PLAIN, 20);
                    metrics = gra.getFontMetrics(font);
                    gra.setFont(font);
                    gra.drawString("SCORE:" + score, 450 - metrics.stringWidth("SCORE:" + score), 450);
                    gra.drawString("LIFE:" + life, 400 - (metrics.stringWidth("SCORE:" + score) + metrics.stringWidth("LIFE:" + life)), 450);
                    gra.drawString("LEVEL:" + level, 350 - (metrics.stringWidth("SCORE:" + score) + metrics.stringWidth("LIFE:" + life) + metrics.stringWidth("LEVEL:" + level)), 450);
                    break;

                case GAME_OVER:
                    gra.setColor(Color.BLACK);

                    font = new Font("SansSerif", Font.PLAIN, 50);
                    gra.setFont(font);
                    metrics = gra.getFontMetrics(font);
                    gra.drawString("Game Over", 250 - metrics.stringWidth("Game Over") / 2, 100);

                    font = new Font("SansSerif", Font.PLAIN, 30);
                    gra.setFont(font);
                    metrics = gra.getFontMetrics(font);
                    gra.drawString("SCORE:" + score, 250 - metrics.stringWidth("SCORE:" + score) / 2, 200);

                    gra.drawString("Press ESP to Return Start Screen",
                            250 - metrics.stringWidth("Press ESP to Return Start Screen") / 2, 350);

                    if (Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                        screen = EnumShootingScreen.START;
                    }

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
