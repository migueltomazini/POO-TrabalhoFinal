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
    private JTextArea resultArea;
    private String fileToEdit;
    Socket client;

    public SearchPanel(String file, Socket clientReceived) {
        client = clientReceived;
        fileToEdit = file;
        // this.playerManager = playerManager;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
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
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPlayers();
            }
        });
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
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

            // contar a quantidade de inputs válidos e fazer uma query válida
            String query = "3 " + fileToEdit + "1\n1 " + id + " " + age + " " + name + " " + nationality + " " + club;
            out.println(query);

            resultArea.setText("");
            String response;
            while (!(response = in.readLine()).equals("")) {
                System.out.println(response);
                // tratar os dados do response pra criar um elemento da classe Player
                String[] playerForm = response.split("/");
                Player player = new Player(Integer.parseInt(playerForm[0]), Integer.parseInt(playerForm[1]),
                        playerForm[2], playerForm[3],
                        playerForm[4]);
                resultArea.append(player.toString() + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(FClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
