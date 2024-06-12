import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrame extends JFrame {
    // private PlayerManager playerManager;
    private String file;
    private Socket client;

    public MainFrame() {
        initClient();

        setTitle("Player Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        String[] fifaYears = { "2017", "2018", "2019", "2020", "2021", "2022", "2023" };
        for (String year : fifaYears) {
            JMenuItem menuItem = new JMenuItem("FIFA " + year);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    file = "fifa" + year + ".bin";
                    // playerManager = new PlayerManager(file);
                    showSearchPanel();
                }
            });
            fileMenu.add(menuItem);
        }

        JMenu listMenu = new JMenu("List");
        JMenuItem listMenuItem = new JMenuItem("List All Players");
        listMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (file != null) {
                    // chamar o socket para listar todos os registros de file
                    // e passar para o showMessageDialog
                    // MUDAR ESSA PARTE
                    String allPlayers = "";
                    try {
                        PrintStream out = new PrintStream(client.getOutputStream());
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        String query = "2 " + file;
                        out.println(query);

                        String response;
                        while (!(response = in.readLine()).equals("")) {
                            System.out.println(response);
                            allPlayers += response + '\n';
                        }
                        JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(allPlayers, 20, 40)));
                    } catch (IOException ex) {
                        Logger.getLogger(FClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No file loaded.");
                }
            }

        });
        listMenu.add(listMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(listMenu);

        setJMenuBar(menuBar);
    }

    private void showSearchPanel() {
        getContentPane().removeAll();
        add(new SearchPanel(file, client), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initClient() {
        try {
            client = new Socket("127.0.0.1", 12345);
        } catch (IOException ex) {
            Logger.getLogger(FClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
