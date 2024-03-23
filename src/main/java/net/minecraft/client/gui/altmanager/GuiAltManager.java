package net.minecraft.client.gui.altmanager;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiAltManager extends GuiScreen implements GuiYesNoCallback {
    private static final Logger logger = LogManager.getLogger();
    public AltSelectionList altSelector;
    private GuiButton btnAddAccount;

    @Override
    public void confirmClicked(boolean result, int id) {
        this.mc.displayGuiScreen(this);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        this.altSelector = new AltSelectionList(this.mc, this.width, this.height, 32, this.height - 64, 36);
        createButtons();
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.altSelector.handleMouseInput();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.altSelector.drawScreen(mouseX, mouseY, partialTicks);
        this.altSelector.registerScrollButtons(7, 8);
        this.drawCenteredString(this.fontRenderer, I18n.format("altmanager.title"), this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void createButtons() {
        this.buttonList.add(btnAddAccount = new GuiButton(0, width / 2 - 50, height - 30, I18n.format("altmanager.add")));

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.altSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                altSelector.loadAlts();
                break;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mc.client.altManager.save();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }
}