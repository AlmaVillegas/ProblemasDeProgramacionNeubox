package ui;

import presentation.MainScreenPresenter;
import presentation.MainScreenPresenterContract;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public final class MainScreen extends JFrame implements MainScreenContract {

    // variables
    private JPanel rootPanel;
    private JLabel txtOutputPath;
    private JLabel txtInputFile;
    private JTextArea textAreaFileContent;
    private JLabel txtResult;

    private MainScreenPresenterContract presenter;

    private String currentStoredPath;

    public MainScreen() {
        super("Problema 1");

        presenter = new MainScreenPresenter(this);

        setUpWindow();

        initViews();

        presenter.windowStarted();
    }

    private void setUpWindow() {
        // set up window
        setContentPane(rootPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setSize(660, 520);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        // create menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setVisible(true);

        // create root menu item
        JMenu menuRoot = new JMenu("File");

        // bind menu to bar
        menuBar.add(menuRoot);

        // create output path item
        JMenuItem itemMenuOpenFile = new JMenuItem("Open");
        JMenuItem itemMenuOutputPath = new JMenuItem("Change output dir");
        JMenuItem itemMenuExit = new JMenuItem("Exit");

        // bind items to menu
        menuRoot.add(itemMenuOpenFile);
        menuRoot.add(itemMenuOutputPath);
        menuRoot.add(itemMenuExit);

        // set up menu bar
        menuBar.add(menuRoot);
        setJMenuBar(menuBar);

        textAreaFileContent.setLineWrap(true);

        // set component listeners
        itemMenuOpenFile.addActionListener(this::onActionItemMenuOpenFile);

        itemMenuOutputPath.addActionListener(this::onActionItemMenuOutputPath);

        itemMenuExit.addActionListener(e -> System.exit(0));
    }

    @Override
    public void showOutputPath(String outputPath) {
        txtOutputPath.setText(currentStoredPath = outputPath);
    }

    @Override
    public void showError(String errorMsg) {
        new JOptionPane(errorMsg, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION)
                .createDialog("Something went wrong")
                .setVisible(true);
    }

    private void onActionItemMenuOpenFile(@SuppressWarnings("unused") ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(currentStoredPath));
        fileChooser.setDialogTitle("Select the file for inspect");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);

        presenter.openFile(
                fileChooser.showDialog(this, "Open"),
                fileChooser.getSelectedFile() == null ? null : fileChooser.getSelectedFile().getAbsolutePath()
        );
    }

    private void onActionItemMenuOutputPath(@SuppressWarnings("unused") ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(currentStoredPath));
        fileChooser.setDialogTitle("Select a new folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        presenter.changeOutputPath(
                fileChooser.showDialog(this, "Change directory"),
                fileChooser.getSelectedFile() == null ? null : fileChooser.getSelectedFile().getAbsolutePath()
        );
    }

    @Override
    public void showFileContent(String path, String content) {
        txtInputFile.setText(path);
        textAreaFileContent.setText(content);
    }

    @Override
    public void showResults(String output) {
        txtResult.setText(output);
    }

}
