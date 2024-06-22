import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    public MainFrame(String ipAddress, int port) {
        initClient(ipAddress, port);

        // Definir a fonte padrão maior
        defaultFont = new Font("Arial", Font.PLAIN, 20);

        setTitle("Player Management");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adicionar WindowListener para fechar o socket quando a janela for fechada
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeClient();
            }
        });

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
                        out.print(query);
                        out.flush();
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
                        out.print(query);
                        out.flush();

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
                        scrollPane.setPreferredSize(new Dimension(1200, 800));

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

    private void initClient(String ipAddress, int port) {
        try {
            client = new Socket(ipAddress, port);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Failed to connect to the server: " + ex.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void closeClient() {
        try {
            if (client != null && !client.isClosed()) {
                client.close();
                System.out.println("Client socket closed.");
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class InputDialog extends JDialog {
        private JTextField ipField;
        private JTextField portField;
        private boolean confirmed;

        public InputDialog(JFrame parent) {
            super(parent, "Server Connection", true);
            setSize(600, 300);
            setLayout(new GridBagLayout());
            Font dialogFont = new Font("Arial", Font.PLAIN, 20);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10);
            add(new JLabel("Server IP:"), gbc);
            gbc.gridx = 1;
            ipField = new JTextField(15);
            ipField.setFont(dialogFont);
            add(ipField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(new JLabel("Server Port:"), gbc);
            gbc.gridx = 1;
            portField = new JTextField(15);
            portField.setFont(dialogFont);
            add(portField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            JButton confirmButton = new JButton("Connect");
            confirmButton.setFont(dialogFont);
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmed = true;
                    setVisible(false);
                }
            });
            add(confirmButton, gbc);
        }

        public String getIpAddress() {
            return ipField.getText().trim();
        }

        public int getPort() {
            try {
                return Integer.parseInt(portField.getText().trim());
            } catch (NumberFormatException e) {
                return -1; // Invalid port
            }
        }

        public boolean isConfirmed() {
            return confirmed;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame parentFrame = new JFrame();
                InputDialog inputDialog = new InputDialog(parentFrame);
                inputDialog.setVisible(true);

                if (inputDialog.isConfirmed()) {
                    String ipAddress = inputDialog.getIpAddress();
                    int port = inputDialog.getPort();

                    if (ipAddress.isEmpty() || port == -1) {
                        JOptionPane.showMessageDialog(null, "Invalid IP address or port number.", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    } else {
                        new MainFrame(ipAddress, port).setVisible(true);
                    }
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
