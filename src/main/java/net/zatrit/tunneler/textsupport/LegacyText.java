package net.zatrit.tunneler.textsupport;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

class LegacyText implements TextSupport {
    @Override
    public Text ipText(String ip) {
        return Texts.bracketed(Text.literal(ip)
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
