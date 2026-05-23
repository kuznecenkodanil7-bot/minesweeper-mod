cat > src/main/java/com/grok/minesweeper/MinesweeperScreen.java << 'EOF'
package com.grok.minesweeper;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MinesweeperScreen extends Screen {
    private final MinesweeperGame game = new MinesweeperGame();
    private static final int CELL_SIZE = 20;

    public MinesweeperScreen() {
        super(Text.literal("Сапёр"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        int startX = (width - MinesweeperGame.WIDTH * CELL_SIZE) / 2;
        int startY = (height - MinesweeperGame.HEIGHT * CELL_SIZE) / 2 - 20;

        context.drawCenteredTextWithShadow(textRenderer, "САПЁР", width/2, 20, 0xFFFFFF);

        for (int y = 0; y < MinesweeperGame.HEIGHT; y++) {
            for (int x = 0; x < MinesweeperGame.WIDTH; x++) {
                int px = startX + x * CELL_SIZE;
                int py = startY + y * CELL_SIZE;
                MinesweeperGame.Cell cell = game.grid[y][x];

                if (cell.revealed) {
                    if (cell.isMine) {
                        context.fill(px, py, px + CELL_SIZE, py + CELL_SIZE, 0xFFFF0000);
                        context.drawText(textRenderer, "💣", px + 6, py + 5, 0xFFFFFF, false);
                    } else {
                        context.fill(px, py, px + CELL_SIZE, py + CELL_SIZE, 0xFFAAAAAA);
                        if (cell.adjacentMines > 0) {
                            context.drawText(textRenderer, String.valueOf(cell.adjacentMines),
                                    px + 7, py + 5, getNumberColor(cell.adjacentMines), false);
                        }
                    }
                } else {
                    context.fill(px, py, px + CELL_SIZE, py + CELL_SIZE, 0xFF555555);
                    if (cell.flagged) {
                        context.drawText(textRenderer, "🚩", px + 4, py + 3, 0xFF0000, false);
                    }
                }
                context.drawBorder(px, py, CELL_SIZE, CELL_SIZE, 0xFF000000);
            }
        }

        if (game.gameOver) {
            context.drawCenteredTextWithShadow(textRenderer, "💥 ПРОИГРАЛИ", width/2, 50, 0xFF0000);
        } else if (game.won) {
            context.drawCenteredTextWithShadow(textRenderer, "🎉 ПОБЕДА!", width/2, 50, 0x00FF00);
        }
    }

    private int getNumberColor(int num) {
        return switch (num) {
            case 1 -> 0x0000FF;
            case 2 -> 0x00AA00;
            case 3 -> 0xFF0000;
            case 4 -> 0x0000AA;
            default -> 0x000000;
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (width - MinesweeperGame.WIDTH * CELL_SIZE) / 2;
        int startY = (height - MinesweeperGame.HEIGHT * CELL_SIZE) / 2 - 20;

        int gridX = (int) ((mouseX - startX) / CELL_SIZE);
        int gridY = (int) ((mouseY - startY) / CELL_SIZE);

        if (gridX >= 0 && gridX < MinesweeperGame.WIDTH && gridY >= 0 && gridY < MinesweeperGame.HEIGHT) {
            if (button == 0) {
                game.reveal(gridX, gridY);
            } else if (button == 1) {
                game.toggleFlag(gridX, gridY);
            }
        }
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
EOF
