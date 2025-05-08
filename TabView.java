import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public abstract class TabView extends JFrame implements INavButtonCreator {
    protected static final int WIDTH = 300;
    protected static final int HEIGHT = 500;
    private TabViewController controller;
    TabView(String windowTitle) {
        setTitle(windowTitle);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        NavigationBar navigationPanel = new NavigationBar(this);
        controller = new TabViewController(new UsersDatabase());
        add(navigationPanel, BorderLayout.SOUTH);
    }

    // as to make sure the child's fields are initialized
    protected void showContentPanel() {
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    protected void redrawCommonComponents()
    {
        NavigationBar navigationPanel = new NavigationBar(this);
        add(navigationPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    protected void removeScreenContents()
    {
        getContentPane().removeAll();
    }

    protected void addCustomComponentNorth(Component c)
    {
        add(c, BorderLayout.NORTH);
    }

    protected void addCustomComponentCenter(Component c)
    {
        add(c, BorderLayout.CENTER);
    }

    protected void addCustomComponentSouth(Component c)
    {
        add(c, BorderLayout.SOUTH);
    }
 
    private void executeProfileUIAction() {
        this.dispose();
        User loggedIn = controller.getLoggedInUser(); 
        InstagramProfileUI profileUI = new InstagramProfileUI(loggedIn);
        profileUI.setVisible(true);
    }

    private void executeNotificationsUIAction() {
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }


    private void executeImageUploadUIAction() {
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    private void executeHomeUIAction() {
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    private void executeExploreUIAction() {
        this.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }

    @Override
    public List<TabViewNavigationButton> createNavButtons() {
        TabViewNavigationButton homeButton = new TabViewNavigationButton("img/icons/home.png", this::executeHomeUIAction);
        TabViewNavigationButton exploreButton = new TabViewNavigationButton("img/icons/search.png", this::executeExploreUIAction);
        TabViewNavigationButton uploadButton = new TabViewNavigationButton("img/icons/add.png", this::executeImageUploadUIAction);
        TabViewNavigationButton notificationsButton = new TabViewNavigationButton("img/icons/heart.png", this::executeNotificationsUIAction);
        TabViewNavigationButton profileButton = new TabViewNavigationButton("img/icons/profile.png", this::executeProfileUIAction);

        List<TabViewNavigationButton> buttons = new ArrayList<>();
        buttons.add(homeButton);
        buttons.add(exploreButton);
        buttons.add(uploadButton);
        buttons.add(notificationsButton);
        buttons.add(profileButton);

        return buttons;
    }


    protected abstract JPanel createContentPanel();
}
