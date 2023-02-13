package net.zatrit.tunneler.textsupport;

import net.minecraft.text.Text;
import net.minecraft.text.Texts;

class NewText implements TextSupport {
    @Override
    public Text ipText(String ip) {
        return Texts.bracketedCopyable(ip);
    }
}
