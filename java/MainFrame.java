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
    private String file;
    private Socket client;
    private Font defaultFont;

    public MainFrame() {
        initClient();

        // Definir a fonte padrão maior
        defaultFont = new Font("Arial", Font.PLAIN, 18);

        setTitle("Player Management");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(defaultFont);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(defaultFont);

        String[] fifaYears = { "2017", "2018", "2019", "2020", "2021", "2022", "2023" };
        for (String year : fifaYears) {
            JMenuItem menuItem = new JMenuItem("FIFA " + year);
            menuItem.setFont(defaultFont);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    file = "fifa" + year + ".bin ";
                    try {
                        PrintStream out = new PrintStream(client.getOutputStream());

                        String query = "Carregar: " + file;
                        out.println(query);
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(null, err);
                    }

                    showSearchPanel();
                }
            });
            fileMenu.add(menuItem);
        }

        JMenu listMenu = new JMenu("List");
        listMenu.setFont(defaultFont);
        JMenuItem listMenuItem = new JMenuItem("List All Players");
        listMenuItem.setFont(defaultFont);
        listMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (file != null) {
                    String allPlayers = "";
                    try {
                        PrintStream out = new PrintStream(client.getOutputStream());
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        String query = "2 " + file;
                        out.println(query);

                        String response;
                        while (!(response = in.readLine()).equals("")) {
                            if (response.equals("Falha no processamento do arquivo."))
                                throw new Exception("Please select another file");
                            System.out.println(response);
                            allPlayers += response + '\n';
                        }

                        JTextArea textArea = new JTextArea(allPlayers);
                        textArea.setFont(defaultFont);
                        textArea.setColumns(40);
                        textArea.setRows(20);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(800, 600));

                        JOptionPane.showMessageDialog(null, scrollPane, "List of Players",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(null, err);
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
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
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
