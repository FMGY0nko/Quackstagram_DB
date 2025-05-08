import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TabViewNavigationButton extends JButton {

    private static final int NAV_ICON_SIZE = 20;

    public TabViewNavigationButton(String iconPath, Runnable action) {
        super();
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(iconScaled));
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        addActionListener(_ -> action.run());
    }
}




