import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
public class NavigationBar extends JPanel{
    private INavButtonCreator creator;
    public NavigationBar(INavButtonCreator creator) {
        this.creator = creator;
        setBackground(new Color(249, 249, 249));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        displayAllActions();
    }

        public void displayAllActions() {
            List<TabViewNavigationButton> navButtons = creator.createNavButtons();
            for (TabViewNavigationButton b : navButtons) {
                add(b);
                add(Box.createHorizontalGlue());
            }
            //remove last glue
            remove(getComponentCount() - 1);
        }
}
