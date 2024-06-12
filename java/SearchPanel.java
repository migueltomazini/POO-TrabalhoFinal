// Painel de busca para procurar jogadores.

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
    // private PlayerManager playerManager;
    private JTextField idField, ageField, nameField, nationalityField, clubField;
    private JList<Player> resultList;
    private DefaultListModel<Player> listModel;
    private String fileToEdit;
    private Socket client;
    private Font defaultFont;

    public SearchPanel(String file, Socket clientReceived) {
        defaultFont = new Font("Arial", Font.PLAIN, 18);

        client = clientReceived;
        fileToEdit = file;
        // this.playerManager = playerManager;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setFont(defaultFont);
        
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Nationality:"));
        nationalityField = new JTextField();
        inputPanel.add(nationalityField);

        inputPanel.add(new JLabel("Club:"));
        clubField = new JTextField();
        inputPanel.add(clubField);

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

        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setFont(defaultFont);
        resultList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
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

    private void searchPlayers() {
        int id = idField.getText().isEmpty() ? -1 : Integer.parseInt(idField.getText());
        int age = ageField.getText().isEmpty() ? -1 : Integer.parseInt(ageField.getText());
        String name = nameField.getText();
        String nationality = nationalityField.getText();
        String club = clubField.getText();

        try {
            PrintStream out = new PrintStream(client.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String query = "3 " + fileToEdit + " 1\n";
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

            query += ct;

            // 3 binario6.bin 3
            // 2 nomeClube "INTER" nacionalidade "ITALY"
            // 2 idade 22 nacionalidade "FRANCE"
            // 3 idade 27 nacionalidade "GERMANY" nomeClube "FC BAYERN MUNCHEN"

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

            out.println(query);

            listModel.clear();
            String response;
            while (!(response = in.readLine()).equals("")) {
                System.out.println(response);
                // tratar os dados do response pra criar um elemento da classe Player
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

    private void showPlayerDialog(Player player) {
        PlayerDialog dialog = new PlayerDialog((Frame) SwingUtilities.getWindowAncestor(this), player,
                defaultFont);
        dialog.setVisible(true);
        searchPlayers(); // Refresh the search results after the dialog is closed
    }
}
