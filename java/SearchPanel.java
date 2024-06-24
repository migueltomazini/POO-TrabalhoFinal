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

public class SearchPanel extends JPanel {
    private JTextField idField, ageField, nameField, nationalityField, clubField;// Campos de texto para entrada de dados
    private JList<Player> resultList;// Lista de resultados da busca
    private DefaultListModel<Player> listModel;// Modelo da lista de resultados
    private String fileToEdit;// Arquivo que será editado
    private Socket client; // Conexão com o cliente (socket)
    private Font defaultFont;// Fonte padrão para os componentes

    // Construtor do painel de busca
    public SearchPanel(String file, Socket clientReceived) {
        defaultFont = new Font("Arial", Font.PLAIN, 18);

        client = clientReceived;
        fileToEdit = file;
        setLayout(new BorderLayout());

        // Painel de entrada de dados
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setFont(defaultFont);

        // Label e campo de texto para ID
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(defaultFont);
        inputPanel.add(idLabel);
        idField = new JTextField();
        idField.setFont(defaultFont);
        inputPanel.add(idField);

         // Label e campo de texto para idade
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(defaultFont);
        inputPanel.add(ageLabel);
        ageField = new JTextField();
        ageField.setFont(defaultFont);
        inputPanel.add(ageField);

        // Label e campo de texto para nome
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(defaultFont);
        inputPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setFont(defaultFont);
        inputPanel.add(nameField);

        // Label e campo de texto para nacionalidade
        JLabel nationalityLabel = new JLabel("Nationality:");
        nationalityLabel.setFont(defaultFont);
        inputPanel.add(nationalityLabel);
        nationalityField = new JTextField();
        nationalityField.setFont(defaultFont);
        inputPanel.add(nationalityField);

        // Label e campo de texto para clube
        JLabel clubLabel = new JLabel("Club:");
        clubLabel.setFont(defaultFont);
        inputPanel.add(clubLabel);
        clubField = new JTextField();
        clubField.setFont(defaultFont);
        inputPanel.add(clubField);

        // Botão de busca
        JButton searchButton = new JButton("Search");
        searchButton.setFont(defaultFont);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPlayers();
            }
        });
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        // Modelo e lista de resultados
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setFont(defaultFont);
        resultList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                 // Verificar se houve clique duplo
                if (e.getClickCount() == 2) {
                    int index = resultList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Player player = listModel.getElementAt(index);
                        showPlayerDialog(player);
                    }
                }
            }
        });
        add(new JScrollPane(resultList), BorderLayout.CENTER);
    }

    // Método para buscar jogadores
    private void searchPlayers() {
        int id = idField.getText().isEmpty() ? -1 : Integer.parseInt(idField.getText());
        int age = ageField.getText().isEmpty() ? -1 : Integer.parseInt(ageField.getText());
        String name = nameField.getText();
        String nationality = nationalityField.getText();
        String club = clubField.getText();

        try {
            PrintStream out = new PrintStream(client.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Construir a consulta
            String query = "3 " + fileToEdit + "1\n";
            int ct = 0;
            if (id != -1)
                ct++;
            if (age != -1)
                ct++;
            if (!name.isEmpty())
                ct++;
            if (!nationality.isEmpty())
                ct++;
            if (!club.isEmpty())
                ct++;

            query += ct + " ";

            if (id != -1)
                query += "id " + id + " ";
            if (age != -1)
                query += "idade " + age + " ";
            if (!name.isEmpty())
                query += "nomeJogador \"" + name + "\" ";
            if (!nationality.isEmpty())
                query += "nacionalidade \"" + nationality + "\" ";
            if (!club.isEmpty())
                query += "nomeClube \"" + club + "\" ";

            out.print(query);

            // Limpar a lista de resultados
            listModel.clear();
            String response;
            while (!(response = in.readLine()).equals("")) {
                System.out.println(response);
                String[] playerForm = response.split("/");
                Player player = new Player(
                        Integer.parseInt(playerForm[0]),
                        Integer.parseInt(playerForm[1]),
                        playerForm[2],
                        playerForm[3],
                        playerForm[4]);
                listModel.addElement(player);
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para exibir o diálogo de edição do jogador
    private void showPlayerDialog(Player player) {
        PlayerDialog dialog = new PlayerDialog((Frame) SwingUtilities.getWindowAncestor(this), player, defaultFont,
                fileToEdit, client);
        dialog.setVisible(true);
        // Atualizar a lista de jogadores após a edição
        searchPlayers();
    }
}
