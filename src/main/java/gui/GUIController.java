package gui;

import client.Client;
import dataexchange.Request;
import gui.Panels.AuthorizationPanel;
import gui.Panels.CommandsPanel;
import gui.Panels.ContentPanel;
import gui.Panels.NavigatePanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class GUIController extends AbstractAction {
    private ClientConnectionGUI client;
    private JFrame frame;
    private JPanel commandsPanel;
    private JPanel navigateMenuPanel;
    private static final HashMap<String, AbstractAction> buttonModel = new HashMap<>();
    private ContentPanel contentPanel;
    private JMenuBar menuBar;

    private static Locale locale = new Locale("default");
    public static ResourceBundle resourceBundle = ResourceBundle.getBundle("localization");

    private AuthorizationPanel authorizationPanel;

    @Getter
    private String userName;

    private static GUIController controller;

    public void start(Client client, String title){
        this.client = new ClientConnectionGUI(client);

        this.authorizationPanel = new AuthorizationPanel(this.client);
        try {
            this.userName = this.authorizationPanel.auth();
        } catch (AuthorizationException e) {
            throw new RuntimeException(e);
        }

        this.frame = new JFrame(title);
        refresh();

    }

    private void setup(){
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLayout(new BorderLayout());

        this.frame.setSize(1035, 660);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        this.frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);


        this.commandsPanel = new CommandsPanel(buttonModel, this);


        this.contentPanel = new ContentPanel(userName, this.client);

        this.navigateMenuPanel = new NavigatePanel(this.contentPanel, userName);


        menuBar = new JMenuBar();
        menuBar.setLayout(new BorderLayout());
        JMenu languageMenu = new JMenu(resourceBundle.getString("language"));
        JMenuItem russian = new JMenuItem(resourceBundle.getString("rus"));
        JMenuItem english = new JMenuItem(resourceBundle.getString("eng"));
        JMenuItem macedonian = new JMenuItem(resourceBundle.getString("mk"));
        JMenuItem spanish = new JMenuItem(resourceBundle.getString("es"));
        JMenuItem greece = new JMenuItem(resourceBundle.getString("el"));
        russian.addActionListener((e) -> changeLocale("ru"));
        english.addActionListener((e) -> changeLocale("default"));
        macedonian.addActionListener((e) -> changeLocale("mk"));
        spanish.addActionListener((e) -> changeLocale("es_HN"));
        greece.addActionListener((e) -> changeLocale("el"));
        languageMenu.add(russian);
        languageMenu.add(macedonian);
        languageMenu.add(spanish);
        languageMenu.add(greece);

        menuBar.add(languageMenu, BorderLayout.WEST);
        menuBar.add(new JLabel(resourceBundle.getString("cur_user") + " " + userName), BorderLayout.EAST);
        this.frame.setJMenuBar(menuBar);

        this.frame.add(this.navigateMenuPanel, BorderLayout.NORTH);

        JSplitPane paneAndBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, contentPanel, this.commandsPanel);
        paneAndBottom.setResizeWeight(0.80);
        this.frame.add(paneAndBottom);

        this.frame.setVisible(true);
    }

    private GUIController(){

    }

    public static void createNewGuiController(Client client, String title){
        controller = new GUIController();
    }

    public static GUIController getAccess(){
        return controller;
    }


    public static void registerCommand(String name, AbstractAction action){
        buttonModel.put(name, action);
    }
    public void refresh(){
        this.client.sendRequest(new Request("show", new ArrayList<>(), userName, true));
        this.frame.getContentPane().removeAll();

        setup();


        frame.validate();
        frame.repaint();

        this.contentPanel.refresh(this.client.receiveResponse().getResponse());
    }

    private void changeLocale(String locale){
        GUIController.locale = new Locale(locale);
        resourceBundle = ResourceBundle.getBundle("localization", GUIController.locale);
        ResourceBundle.clearCache();
        refresh();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        refresh();
    }
}
