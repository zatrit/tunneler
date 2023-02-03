package net.zatrit.tunneler.interfaces;

import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

/**
 * Интерфейс, позволяющий указать для экрана (т.е. Screen)
 * принудительно указать следующий экран. См. {@link net.zatrit.tunneler.mixin.client.ChatScreenMixin}
 */
public interface NextScreen {
    /**
     * Поле, содержащее экран, к которому произойдёт переход после
     * закрытия данного экрана
     */
    void setNextScreen(Function<Screen, Screen> screen);
}
