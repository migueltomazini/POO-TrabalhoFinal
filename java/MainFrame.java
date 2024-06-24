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
    private String file; // Armazena o nome do arquivo selecionado
    private Socket client; // Socket para comunicação com o servidor
    private Font defaultFont;  // Fonte padrão usada na interface

    public MainFrame(String ipAddress, int port) {
        initClient(ipAddress, port); // Inicializa a conexão com o servidor

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

        JMenuBar menuBar = new JMenuBar(); // Cria a barra de menu
        menuBar.setFont(defaultFont);

        JMenu fileMenu = new JMenu("File"); // Cria o menu "File"
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

                        String query = "Carregar: " + file; // Comando para carregar o arquivo
                        out.print(query);
                        out.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(null, err);
                    }

                    showSearchPanel(); // Mostra o painel de busca
                }
            });
            fileMenu.add(menuItem); // Adiciona o item ao menu "File"
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

                        String query = "2 " + file;  // Comando para listar todos os jogadores
                        out.print(query);
                        out.flush();

                        String response;
                        while (!(response = in.readLine()).equals("")) {
                            if (response.equals("Falha no processamento do arquivo."))
                                throw new Exception("Please select another file");
                            System.out.println(response);
                            allPlayers += response + '\n';
                        }

                        JTextArea textArea = new JTextArea(allPlayers); // Área de texto para mostrar os jogadores
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
                    JOptionPane.showMessageDialog(null, "No file loaded.");  // Mensagem caso nenhum arquivo esteja carregado
                }
            }

        });
        listMenu.add(listMenuItem);

        menuBar.add(fileMenu); // Adiciona o menu "File" à barra de menu
        menuBar.add(listMenu); // Adiciona o menu "List" à barra de menu

        setJMenuBar(menuBar); // Configura a barra de menu na janela
    }

    private void showSearchPanel() {
        getContentPane().removeAll();  // Remove todos os componentes do painel
        add(new SearchPanel(file, client), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initClient(String ipAddress, int port) {
        try {
            client = new Socket(ipAddress, port); // Inicializa o socket com o servidor
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Failed to connect to the server: " + ex.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Encerra o programa caso não consiga conectar ao servidor
        }
    }

    private void closeClient() {
        try {
            if (client != null && !client.isClosed()) {
                client.close(); // Fecha o socket
                System.out.println("Client socket closed.");
            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static class InputDialog extends JDialog {
        private JTextField ipField; // Campo de texto para o IP do servidor
        private JTextField portField; // Campo de texto para a porta do servidor
        private boolean confirmed; // Indica se a conexão foi confirmada

        public InputDialog(JFrame parent) {
            super(parent, "Server Connection", true);
            setSize(600, 300); // Define o tamanho do diálogo
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
            add(new JLabel("Server Port:"), gbc); // Rótulo para o IP do servidor
            gbc.gridx = 1;
            portField = new JTextField(15); // Campo de texto para o IP do servidor
            portField.setFont(dialogFont);
            add(portField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.CENTER;
            JButton confirmButton = new JButton("Connect"); // Botão para confirmar a conexão
            confirmButton.setFont(dialogFont);
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    confirmed = true; // Marca como confirmado
                    setVisible(false); // Fecha o diálogo
                }
            });
            add(confirmButton, gbc);
        }

        public String getIpAddress() {
            return ipField.getText().trim(); // Retorna o IP do servidor
        }

        public int getPort() {
            try {
                return Integer.parseInt(portField.getText().trim()); // Retorna a porta do servidor
            } catch (NumberFormatException e) {
                return -1; // Porta inválida
            }
        }

        public boolean isConfirmed() {
            return confirmed; // Retorna se a conexão foi confirmada
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame parentFrame = new JFrame(); // Cria o frame principal
                InputDialog inputDialog = new InputDialog(parentFrame);
                inputDialog.setVisible(true);

                if (inputDialog.isConfirmed()) { // Se a conexão foi confirmada
                    String ipAddress = inputDialog.getIpAddress();
                    int port = inputDialog.getPort();

                    if (ipAddress.isEmpty() || port == -1) {
                        JOptionPane.showMessageDialog(null, "Invalid IP address or port number.", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.exit(1);  // Encerra o programa se IP ou porta são inválidos
                    } else {
                        new MainFrame(ipAddress, port).setVisible(true);// Cria e exibe a MainFrame
                    }
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
