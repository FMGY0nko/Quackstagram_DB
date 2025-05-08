
import java.awt.*;
import javax.swing.*;

public abstract class TabViewWithHeader extends TabView {

        String headerTitle;
        TabViewWithHeader(String headerTitle, String windowTitle) {
                super(windowTitle);
                this.headerTitle = headerTitle;
                JPanel headerPanel = createHeaderPanel(headerTitle);
                add(headerPanel, BorderLayout.NORTH);
            }
        
        private JPanel createHeaderPanel(String headerTitle) {

         JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         headerPanel.setBackground(new Color(51, 51, 51));
         JLabel hLabel = new JLabel(headerTitle);
         hLabel.setFont(new Font("Arial", Font.BOLD, 16));
         hLabel.setForeground(Color.WHITE); 
         headerPanel.add(hLabel);
         headerPanel.setPreferredSize(new Dimension(WIDTH, 40));
         return headerPanel;
   }

   @Override
   protected void redrawCommonComponents()
   {
    super.redrawCommonComponents();
    JPanel headerPanel = createHeaderPanel(headerTitle);
    add(headerPanel, BorderLayout.NORTH);
    revalidate();
    repaint();
   }
}

