// Diálogo para exibir e editar dados do jogador.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerDialog extends JDialog {
    private Player player;
    // private PlayerManager playerManager;
    private JTextField idField, ageField, nameField, nationalityField, clubField;

    public PlayerDialog(Frame owner, Player player) {
        super(owner, "Player Details", true);
        this.player = player;
        // this.playerManager = playerManager;
        setLayout(new GridLayout(6, 2));

        add(new JLabel("ID:"));
        idField = new JTextField(String.valueOf(player.getId()));
        idField.setEditable(false);
        add(idField);

        add(new JLabel("Age:"));
        ageField = new JTextField(String.valueOf(player.getAge()));
        add(ageField);

        add(new JLabel("Name:"));
        nameField = new JTextField(player.getName());
        add(nameField);

        add(new JLabel("Nationality:"));
        nationalityField = new JTextField(player.getNationality());
        add(nationalityField);

        add(new JLabel("Club:"));
        clubField = new JTextField(player.getClub());
        add(clubField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlayer(); 
            }
        });
        add(saveButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlayer();
            }
        });
        add(deleteButton);

        setSize(300, 200);
        setLocationRelativeTo(owner);
    }

    private void savePlayer() {
        // possuir o id do jogador
        // remover o id do arquivo
        // adicionar os valores

        int age = Integer.parseInt(ageField.getText());
        String name = nameField.getText();
        String nationality = nationalityField.getText();
        String club = clubField.getText();

        player.setAge(age);
        player.setName(name);
        player.setNationality(nationality);
        player.setClub(club);

        // playerManager.updatePlayer(player);
        dispose();
    }

    private void deletePlayer() {
        // chamar função para deletar

        // playerManager.deletePlayer(player.getId());
        dispose();
    }
}
