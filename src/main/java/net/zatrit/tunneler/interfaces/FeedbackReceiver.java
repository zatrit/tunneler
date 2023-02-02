package net.zatrit.tunneler.interfaces;

import net.minecraft.text.Text;

/**
 * Интерфейс, представляющий собой получателя
 * любых сообщений, к примеру экран игрового чата.
 * Позволяет упростить абстракцию в проекте и
 * избежать повторяющегося кода. См.:
 * <ul>
 * <li>{@link net.zatrit.tunneler.mixin.client.FabricClientCommandSourceMixin}</li>
 * <li>{@link net.zatrit.tunneler.mixin.server.ServerCommandSourceMixin}</li>
 * <li>{@link net.zatrit.tunneler.ChatUtils}</li>
 * </ul>
 */
public interface FeedbackReceiver {
    void sendError(Text text);

    void sendFeedback(Text text);
}
